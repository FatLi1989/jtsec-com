package com.jtsec.mc.dev.moitor.snmp.handler;

import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.mc.dev.moitor.snmp.AbstractDataHandler;
import com.jtsec.mc.dev.moitor.snmp.util.OidUtil;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.VariableBinding;
import java.util.List;

/**
 * 告警信息数据解析
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public class WarnDataHandler extends AbstractDataHandler {
	/**
	 * Logger for this class
	 */
	private static String HANDTYPE_WARN = "warn";
	public WarnDataHandler(){}
	
	public WarnDataHandler(String ip, String port, String monitorTime){
		super.ip = ip;
		super.port = port;
		super.monitorTime = monitorTime;
	}
	
	@Override
	public void setNextHandler(AbstractDataHandler abstractDataHandler) {
		super.abstractDataHandler = abstractDataHandler;
	}
	
	@Override
	public AbstractDataHandler getNextHandler() {
		return super.abstractDataHandler;
	}

	@Override
	public void handleRequest(String dataType, List<String> sqlList, List<VariableBinding> vbList) {
		if(HANDTYPE_WARN.equals(dataType)){
			for(int i = 2; i < vbList.size(); i++){
				VariableBinding vb = vbList.get(i);
				String tableName = super.getTableName(vbList.get(1).getVariable().toString(), vb.getOid().toString());
				log.info(vbList.get(1).getVariable().toInt() + "\t tableName=" + tableName);
				if(tableName == "" || tableName == null){
					continue;
				}
				String variableMsg = super.getStringByEncode(vb.getVariable().toString(), null);
				/**
				 * oid = 1.3.6.1.4.1.39056.2.25
				 * 内存、cpu使用的阀值
				 * 20-接口断开1 21-接口断开2 25-内存超出-1 26-内存超出2 28-cpu负载1 29-cpu内存负载2
				 */
				//oid最后的一组数字标识为告警标识
				String warnType = OidUtil.getDataType(vbList.get(1).getVariable().toString());
				String sql = "INSERT INTO " + tableName + "(ip_address, port, type, message, monitor_time) values(";
				sql = DbUtil.bindSqlParam(sql, null, super.ip, super.port, warnType, variableMsg, super.monitorTime);
				sqlList.add(sql);
			}
		}else{
			super.callNextHandler(dataType, sqlList, vbList);
		}
	}
	
	@Override
	public void handleRequest(String dataType, List<String> sqlList, String value) {
		int thresholdValue = 70;
		if(Integer.parseInt(value) > thresholdValue){
			String tableName = dataType + "";
			if(!NotNullUtil.stringNotNull(tableName)){
				return;
			}
			StringBuffer sb = new StringBuffer();
			String type = "";
			if("CPUUSE".equals(dataType)){
				sb.append("CPU使用率超过");
				type = "28";
			}else if("MEMUSE".equals(dataType)){
				sb.append("MEM使用率超过");
				type = "25";
			}
			sb.append(value).append("%");
			String sql = "INSERT INTO " + tableName + "(ip_address, port, type, message, monitor_time) values(";
			sql = DbUtil.bindSqlParam(sql, null, super.ip, super.port, type, sb.toString(), super.monitorTime);
			sqlList.add(sql);
		}else{
			super.callNextHandler(dataType, sqlList, value);
		}
	}
}
