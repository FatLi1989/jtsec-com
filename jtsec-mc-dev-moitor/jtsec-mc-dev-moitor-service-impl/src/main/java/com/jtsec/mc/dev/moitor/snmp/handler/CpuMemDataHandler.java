package com.jtsec.mc.dev.moitor.snmp.handler;

import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.mc.dev.moitor.snmp.AbstractDataHandler;
import com.jtsec.mc.dev.moitor.snmp.util.OidUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.snmp4j.smi.VariableBinding;
import java.util.List;

/**
 * CPU、MEM日志类型数据data解析类
 * 
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public class CpuMemDataHandler extends AbstractDataHandler {
	/**
	 * Logger for this class
	 */
	private static String HANDTYPE_CPUMEMUSE = "cpumemuse";

	public CpuMemDataHandler(){}
	
	public CpuMemDataHandler(String ip, String port, String monitorTime){
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
//		if(DataTypeCons.CPU_USE_TRAP.equals(dataType) || DataTypeCons.MEM_USE_TRAP.equals(dataType)){
//		{oid : 1.3.6.1.4.1.39056.2.4, tableName : lcaa_type_cpu_use}或{oid ：1.3.6.1.4.1.39056.2.5, tableName : lcaa_type_mem_use}
		if(HANDTYPE_CPUMEMUSE.equals(dataType)){
			for(int i = 2; i < vbList.size(); i++){
				VariableBinding vb = vbList.get(i);
//				dataType = super.mibMappingMap.get(vb.getOid().toString());
//				String tableName = super.getTableName(dataType);
//				if(tableName == null || tableName == "" || !this.canDealType(dataType)){
				String tableName = super.getTableName(vbList.get(1).getVariable().toString(), vb.getOid().toString());
				if(tableName == null || tableName == ""){
					continue;
				}
				log.info(tableName);
				String flag = OidUtil.getDataType(vb.getOid().toString());
				String variableMsg = super.getStringByEncode(vb.getVariable().toString(), null);
				String sql = "INSERT INTO " + tableName + "(ip_address, port, region, monitor_record,monitor_time, data_type) VALUES(";
				sql = DbUtil.bindSqlParam(sql, null, super.ip, super.port, super.region, variableMsg, super.monitorTime, flag);
				sqlList.add(sql);
			}
		}else{
			this.callNextHandler(dataType, sqlList, vbList);
		}
	}
	
	@Override
	public void handleRequest(String dataType, List<String> sqlList, String value) {
		if("CPUUSR".equals(dataType) || "MEMUSE".equals(dataType)){
			String tableName = dataType + "";
			if(!NotNullUtil.stringNotNull(tableName)){
				return;
			}
			String[] arr = valueDetail.split(":");
			if(!NotNullUtil.objectArrayNotNull(arr)){
				return;
			}
			String monitorRecord = arr[1];
			
			//将值传到warnDataHandler中判断内存或是CPU的使用率是否到达了临界值
			AbstractDataHandler warnDataHandler = new WarnDataHandler(super.ip, super.port, super.monitorTime);
			warnDataHandler.handleRequest(dataType, sqlList, monitorRecord);

			String useSql = "INSERT INTO " + tableName + "(ip_address, port, monitor_record, monitor_time) values(";
			useSql = DbUtil.bindSqlParam(useSql, null, super.ip, super.port, monitorRecord, super.monitorTime);
			sqlList.add(useSql);
		}else{
			this.callNextHandler(dataType, sqlList, value);
		}
	}
	
}
