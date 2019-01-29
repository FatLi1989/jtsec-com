package com.jtsec.mc.dev.moitor.snmp.handler;

import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.mc.dev.moitor.snmp.AbstractDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.VariableBinding;

import java.util.List;

/**
 * 服务流量数据解析，只有双向的设备才会将服务应用的服务流量通过snmp发送到集控中
 *
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 */
@Slf4j
public class ServFluxDataHandler extends AbstractDataHandler {
	/**
	 * Logger for this class
	 */
	private static String HANDTYPE_SERVFLUX = "servflux";

	public ServFluxDataHandler () {
	}

	public ServFluxDataHandler (String ip, String port, String monitorTime) {
		super.ip = ip;
		super.port = port;
		super.monitorTime = monitorTime;
	}

	@Override
	public void handleRequest (String dataType, List<String> sqlList, List<VariableBinding> vbList) {
//		if(DataTypeCons.SERV_FLUX_TRAP.equals(dataType)){
		if (HANDTYPE_SERVFLUX.equals (dataType)) {
			for (int i = 2; i < vbList.size (); i++) {
				VariableBinding vb = vbList.get (i);
//				oid : 1.3.6.1.4.1.39056.2.8, talbeName = lcaa_type_serv_flux
				String tableName = super.getTableName (vbList.get (1).getVariable ().toString (), vb.getOid ().toString ());
				log.info (vbList.get (1).getVariable ().toString () + "\t " + tableName);
				if (tableName == "" || tableName == null) {
					continue;
				}
				/**
				 * SNMP服务流量相关的接口说明 
				 * OID: 1.3.6.1.4.1.39056.1.61.0
				 * Variable：格式（以#号分隔，字段数为9位）
				 * 服务名#监听地址（IP）#监听端口#目标地址（IP）#目标端口#代理类型#服务描述（对应库中使用部门字段）#流入流量#流出流量
				 * 样例(eg)：
				 * tcp61001#192.168.8.80#61001#192.168.8.217#61001#tcp#tcp61001#0#0
				 * ftp#0.0.0.0#21#192.168.8.223#21#ftp#ftp#0#0
				 * http.#192.168.8.81#80#192.168.8.196#80#tcp#http.#0#0
				 * sock#0.0.0.0#1080#127.0.0.1#1080#socks5#sock#0#0
				 * tcp#192.168.8.80#5001#192.168.8.223#5001#tcp#tcp#0#0
				 */
				String variableMsg = super.getStringByEncode (vb.getVariable ().toString (), null);
				log.info ("variableMsg : " + variableMsg);
				if (variableMsg.indexOf ("#") < 0) {
					continue;
				}
				String sql = "INSERT INTO " + tableName + "(ip_address,port,region,monitor_time,server_name,listen_ip,listen_port,dst_ip,dst_port,proxy_type,user_dept,in_flux,out_flux) values(";
				sql = DbUtil.bindSqlParam (sql, variableMsg.split ("#"), super.ip, super.port, super.region, super.monitorTime);
				sqlList.add (sql);
			}
		} else {
			this.callNextHandler (dataType, sqlList, vbList);
		}
	}

	@Override
	public void handleRequest (String dataType, List<String> sqlList, String value) {
		/**
		 * 服务流量接口数据 valueDetail为(共12项)
		 * 192.168.0.171#2012-03-06 17:55:33#梅州海康应用服务#tcp#0#192.168.0.251#43200#125.89.152.106#9008#126794#26758#ll#192.168.0.249
		 * 设备ip#时间#服务名称#代理类型{tcp/udp}#代理方向{0:内->外 1:外->内}#监听地址#监听端口#目的地址#目的端口#流入流量#流出流量#用户#终端地址
		 * 参数之间以#分隔
		 */
		if ("SERVFLUX".equals (dataType)) {
			String tableName = dataType + "";
			if (!NotNullUtil.stringNotNull (tableName)) {
				return;
			}
			String[] servArr = valueDetail.split (",");
			if (!NotNullUtil.objectArrayNotNull (servArr)) {
				return;
			}
			String sql = "INSERT INTO " + tableName + "(port,ip_address,monitor_time,server_name,proxy_type,proxy_dir,listen_ip,listen_port,dst_ip,dst_port,in_flux,out_flux,user_id,termianl_ip) values(";
			sql = DbUtil.bindSqlParam (sql, servArr, port);
			sqlList.add (sql);
		} else {
			this.callNextHandler (dataType, sqlList, value);
		}
	}

	@Override
	public void setNextHandler (AbstractDataHandler abstractDataHandler) {
		super.abstractDataHandler = abstractDataHandler;
	}

	@Override
	public AbstractDataHandler getNextHandler () {
		return super.abstractDataHandler;
	}

}
