package com.jtsec.mc.dev.moitor.snmp.handler;

import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.mc.dev.moitor.snmp.AbstractDataHandler;
import com.jtsec.mc.dev.moitor.snmp.util.OidUtil;
import org.snmp4j.smi.VariableBinding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据处理handler
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
public class NetFluxDataHandler extends AbstractDataHandler {

	private static String HANDTYPE_NET_FLUX = "netflux";
	public NetFluxDataHandler(){}
	
	public NetFluxDataHandler(String ip, String port, String monitorTime){
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
//		oid : 1.3.6.1.4.1.39056.2.6, tableName : lcaa_type_net_flux
		if(HANDTYPE_NET_FLUX.equals(dataType)){
			String tableName = OidUtil.getTableName(vbList.get(1).getVariable().toString());
			if(tableName == "" || tableName == null){
				return;
			}
			Map<String, String> map = new HashMap<String, String>();;
			for(int i = 2; i < vbList.size(); i++){
				VariableBinding vb = vbList.get(i);
				String variableMsg = super.getStringByEncode(vb.getVariable().toString(), null);
				String flag = OidUtil.getDataType(vb.getOid().toString());
				map.put(flag, variableMsg);
			}
			if(!"0".equals(this.getInOutFlux(map, "1")) || !"0".equals(this.getInOutFlux(map, "2"))){
				String sql = "INSERT INTO " + tableName + "(ip_address, port, region, data_type, in_flux, out_flux, monitor_time) VALUES(";
				sql = DbUtil.bindSqlParam(sql, null, super.ip, super.port, super.region, "1", this.getInOutFlux(map, "1"), this.getInOutFlux(map, "2"), super.monitorTime);
				sqlList.add(sql);
			}
			if(!"0".equals(this.getInOutFlux(map, "3")) || !"0".equals(this.getInOutFlux(map, "4"))){
				String sql = "INSERT INTO " + tableName + "(ip_address, port, region, data_type, in_flux, out_flux, monitor_time) VALUES(";
				sql = DbUtil.bindSqlParam(sql, null, super.ip, super.port, super.region, "2", this.getInOutFlux(map, "3"), this.getInOutFlux(map, "4"), super.monitorTime);
				sqlList.add(sql);
			}
		}else{
			super.callNextHandler(dataType, sqlList, vbList);
		}
	}
	
	@Override
	public void handleRequest(String dataType, List<String> sqlList, String value) {
		if(dataType.indexOf("NETFLUX") > -1 && (dataType.startsWith("I") || dataType.startsWith("O"))){
			String tableName = dataType + "";
			if(!NotNullUtil.stringNotNull(tableName)){
				return;
			}
			String[] fluxArr = valueDetail.split(" ");
			if(!NotNullUtil.objectArrayNotNull(fluxArr)){
				return;
			}
			String inFlux = "0";
			String outFlux = "0";
			for(String flux : fluxArr){
				if(flux.indexOf("=") > -1){
					String[] arr = flux.split("=");
					if(arr[0].startsWith("down")){
						inFlux = arr[1];
					}else if(arr[0].startsWith("out")){
						outFlux = arr[1];
					}
				}
			}
			String inFluxSql = "INSERT INTO " + tableName + "(ip_address,port,monitor_record,monitor_time) VALUES(";
			inFluxSql = DbUtil.bindSqlParam(inFluxSql, null, ip, port, monitorTime, inFlux);
			String outFluxSql = "INSERT INTO " + tableName + "(ip_address,port,monitor_record,monitor_time) VALUES(";
			outFluxSql = DbUtil.bindSqlParam(outFluxSql, null, ip, port, monitorTime, outFlux);
			sqlList.add(inFluxSql);
			sqlList.add(outFluxSql);
		}else{
			this.callNextHandler(dataType, sqlList, value);
		}
	}
	
	/**
	 * 判断是否可以处理的数据类型
	 * @param dataType
	 * @return
	 */
//	private boolean canDealType(String dataType){
//		if(DataTypeCons.NET_FLUX_IN.equals(dataType)
//				|| DataTypeCons.NET_FLUX_OUT.equals(dataType)
//				|| DataTypeCons.INET_FLUX_IN.equals(dataType)
//				|| DataTypeCons.INET_FLUX_OUT.equals(dataType)
//				|| DataTypeCons.ONET_FLUX_IN.equals(dataType)
//				|| DataTypeCons.ONET_FLUX_OUT.equals(dataType)
//				){
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 从map中获得流量值
	 * @param map
	 * @param key
	 * @return
	 */
	private String getInOutFlux(Map<String, String> map, String key){
		String num = map.get(key);
		if(num != null && num.length() > 0){
			return num;
		}
		return "0";
	}
}
