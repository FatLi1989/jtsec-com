package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.mc.dev.moitor.pojo.model.NemDeviceInfo;
import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * 采集CPU模块
 * @author surpassE
 * @version 1.0.0
 * @see 2015-09-24
 *
 */
public class CpuParse extends AbstractSnmpParse implements SnmpParseI {

	private static final String DEV_CPU_INFO = "dev_cpu_info";
	public CpuParse(){}
	
	public CpuParse(String devId){
		super.devId = devId;
	}
	public CpuParse(NemDeviceInfo nemDeviceInfo){
	    super.nemDeviceInfo = nemDeviceInfo;
	    super.devId = nemDeviceInfo.getDevIdentify();
	}
	
	@Override
	public void parseVb(Vector<?> vector) {
		super.vectorToArr(vector);
		super.showArrMsg();
		List<String> sqlList = new ArrayList<String>();
	    String tabPrefix = super.nemDeviceInfo.getDevTableName();
	    String tableName = tabPrefix + "_" + DEV_CPU_INFO;
		String sql = "INSERT INTO " + tableName + "(dev_id, gather_time, cpu_ava, cpu_core, data_type) VALUES('" + super.devId + "', 'GATHER_TIME', CPU_AVA, 'CPU_CORE', DATA_TYPE)";
		String cpuCore = "";
		float cpuAvaValue = 0;
		int num = 0;
		for(int a = 0; a < arr.length; a++){
			String msg = arr[a][0];
			if(msg != null && msg.length() > 0){
				cpuCore += arr[a][0] + "#";
				cpuAvaValue += Float.parseFloat(arr[a][0]);
				num++;
//				int i = 0;
//				logger.debug(cpuAva + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
			}
		}
		if("".equals(cpuCore)){
			cpuCore = "-1";
			cpuAvaValue = -1;
		}else{
			cpuAvaValue = (cpuAvaValue * 100)/(num * 100);
		}
		sql = sql.replaceAll("GATHER_TIME", super.getNowGatherTime());
		sql = sql.replace("CPU_AVA", cpuAvaValue + "");
		sql = sql.replace("CPU_CORE", cpuCore);
		//添加数据的类型
		sql = sql.replaceAll("DATA_TYPE", "0");
		if(cpuAvaValue >= 95){
			String addSql = "INSERT INTO warning_event_info VALUES('" + UUID.randomUUID().toString()+ "', '" + super.devId + "', '0005', 'CPU使用率超过了" + cpuAvaValue + "%', " + System.currentTimeMillis() + ", '', 0)";
			sqlList.add(addSql);
		}
		sqlList.add(sql);
		super.execSqlList(sqlList);
	}
	
	public static void main(String[] args) {
		float a = 2;
		System.out.println(a/10);
	}
}
