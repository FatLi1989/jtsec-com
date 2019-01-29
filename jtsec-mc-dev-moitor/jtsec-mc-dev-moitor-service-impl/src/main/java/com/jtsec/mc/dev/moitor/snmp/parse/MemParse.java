package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 采集MEM模块
 * MEM利用率计算公式  (memTotal - memAva - memBuf - memCache) * 100)/memTotal
 * @author surpassE
 * @version 1.0.0
 * 已不用 2018-03-20 18:45 by lioncode
 */
@Slf4j
public class MemParse extends AbstractSnmpParse implements SnmpParseI {
	/**
	 * Logger for this class
	 */

	private static final String DEV_MEM_INFO = "dev_mem_info";
	
	public MemParse(){}
	
	public MemParse(String devId){
		super.devId = devId;
	}
	
	@Override
	public void parseVb(Vector<?> vector) {
		super.vectorToArr(vector);
		List<String> sqlList = new ArrayList<String>();
		for(int a = 0; a < arr.length; a++){
			String sql = "INSERT INTO " + super.getTableName(DEV_MEM_INFO) + " VALUES('" + super.devId + "', 'GATHER_TIME', PARAM_0, PARAM_1, PARAM_2, PARAM_3, PARAM_4, PARAM_5, MEM_RATIO, DATA_TYPE)";
			String msg = arr[a][0];
			if(msg != null && msg.length() > 0){
//				int i = 2;
//				long memTotal = (arr[a][i] == null || arr[a][i].length() <= 0) ? 0 : Long.parseLong(arr[a][i]);
//				i++;
//				long memAva = (arr[a][i] == null || arr[a][i].length() <= 0) ? 0 : Long.parseLong(arr[a][i]);
//				i++;
//				long memBuf = (arr[a][i] == null || arr[a][i].length() <= 0) ? 0 : Long.parseLong(arr[a][i]);
//				i++;
//				long memCache = (arr[a][i] == null || arr[a][i].length() <= 0) ? 0 : Long.parseLong(arr[a][i]);
//				float percent = ((memTotal - memAva - memBuf - memCache) * 100)/memTotal;	//cpu利用率计算公式
//
//				sql = sql.replaceAll("GATHER_TIME", super.getNowGatherTime());
//				for(int j = 0; j < 6; j++){
//					sql = sql.replaceAll("PARAM_" + j, arr[a][j]);
//				}
//				sql = sql.replace("MEM_RATIO", percent + "");
				
//				long memCache = this.parseVbToLong(i++, vector);
//				float percent = ((memTotal - memAva - memBuf - memCache) * 100)/memTotal;	//cpu利用率计算公式
//				sql = sql.replaceAll("GATHER_TIME", super.getNowGatherTime());
//				sql = sql.replace("PARAM_" + (i++), arr[a][0]).replace("PARAM_" + (i++), arr[a][1]).replace("PARAM_" + (i++), arr[a][2]);
//				sql = sql.replace("PARAM_" + (i++), arr[a][3]).replace("PARAM_" + (i++), arr[a][4]).replace("PARAM_" + (i++), arr[a][5]);
				//添加数据的类型
				sql = sql.replaceAll("DATA_TYPE", "0");
				int i = 0;
				log.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
				sqlList.add(sql);
			}
		}
//		super.execSqlList(sqlList);
	}

}
