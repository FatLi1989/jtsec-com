package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Slf4j
public class SoftwareParse extends AbstractSnmpParse implements SnmpParseI {
	/**
	 * Logger for this class
	 */

	private static final String DEV_SOFTWARE_INFO = "dev_software_info";

	public SoftwareParse(){}
	
	public SoftwareParse(String devId){
		super.devId = devId;
	}
	
	@Override
	public void parseVb(Vector<?> vector) {
//		for(int j = 0; j < vector.size(); j++){
//			logger.debug(super.getVbOidValue(j, vector) + "===>" + super.getVbValue(j, vector));
//		}
		super.vectorToArr(vector);
		List<String> sqlList = new ArrayList<String>();
		for(int a = 0; a < arr.length; a++){
			String sql = "REPLACE INTO " + DEV_SOFTWARE_INFO + " VALUES('" + super.devId + "', 'PARAM_0', 'PARAM_1', 'PARAM_2')";
			String msg = arr[a][0];
			if(msg != null && msg.length() > 0){
				sql = this.parseWay2(a, sql);
//				logger.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
				sqlList.add(sql);
			}
		}
		super.execSqlList(sqlList, DEV_SOFTWARE_INFO);
	}
	
	/**
	 * 直接遍历根节点，从多列数组中解析指定数据
	 * @param a
	 * @param sql
	 * @return
	 */
	protected String parseWay1(int a, String sql){
		int i = 0;
		sql = sql.replace("PARAM_" + (i++), arr[a][1] == null ? "" : arr[a][1]);
		sql = sql.replace("PARAM_" + (i++), arr[a][3] == null ? "" : arr[a][3]);
		sql = sql.replace("PARAM_" + (i++), arr[a][4] == null ? "0000-01-01 00:00:01" : this.parseHexToTime(arr[a][4]));
		return sql;
	}
	
	/**
	 * 顺序替换数据信息
	 * @param a
	 * @param sql
	 * @return
	 */
	public String parseWay2(int a, String sql){
		int i = 0;
		sql = sql.replace("PARAM_" + (i++), arr[a][i-1] == null ? "" : super.preToStringHex(arr[a][i-1], "GBK"));
		sql = sql.replace("PARAM_" + (i++), arr[a][i-1] == null ? "" : arr[a][i-1]);
		sql = sql.replace("PARAM_" + (i++), arr[a][i-1] == null ? "0000-01-01 00:00:01" : this.parseHexToTime(arr[a][i-1]));
		return sql;
	}
	
	public static void main(String[] args) {
		String oid = "1.3.6.1.2.1.25.4.2.1.1.1";
		log.debug(oid.substring(oid.lastIndexOf(".") + 1));
	}
}
