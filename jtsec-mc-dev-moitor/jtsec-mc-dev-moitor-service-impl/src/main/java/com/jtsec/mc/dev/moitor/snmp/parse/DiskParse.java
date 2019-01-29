package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 解析服务其磁盘空间
 * @author surpassE
 * @version 1.0.0
 * @see 2015-09-22
 * 已不用 2018-03-20 18:45 by lioncode
 */
@Slf4j
public class DiskParse extends AbstractSnmpParse implements SnmpParseI {
	/**
	 * Logger for this class
	 */
	private static final String DEV_DISK_INFO = "dev_disk_info";
	

	public DiskParse(){}
	
	public DiskParse(String devId){
		super.devId = devId;
	}
	
	@Override
	public void parseVb(Vector<?> vector) {
		List<String> sqlList = new ArrayList<String>();
		super.vectorToArr(vector);
		for(int a = 0; a < arr.length; a++){
			String sql = "REPLACE INTO " + DEV_DISK_INFO + " VALUES('" + super.devId + "', 'PARAM_0', 'PARAM_1', PARAM_2, PARAM_3)";
			String msg = arr[a][0];
			if(msg != null && msg.length() > 0 && (msg.startsWith("/") || msg.indexOf(":") > -1)){
				int i = 0;
				sql = sql.replace("PARAM_" + (i++), arr[a][0]).replace("PARAM_" + (i++), arr[a][1]).replace("PARAM_" + (i++), arr[a][2]).replace("PARAM_" + (i++), arr[a][3]);
//				logger.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
				sqlList.add(sql);
			}
		}
		super.execSqlList(sqlList, DEV_DISK_INFO);
	}

}
