package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Slf4j
public class ProgressParse extends AbstractSnmpParse implements SnmpParseI {
	/**
	 * Logger for this class
	 */
	private static final String DEV_PROGRESS_INFO = "dev_progress_info";

	public ProgressParse(){}
	
	public ProgressParse(String devId){
		super.devId = devId;
	}
	
	@Override
	public void parseVb(Vector<?> vector) {
		super.vectorToArr(vector);
		List<String> sqlList = new ArrayList<String>();
		for(int a = 0; a < arr.length; a++){
			/**
			 * PID	prog_name	RUNID	prog_path		prog_param						RUNTYPE		RUNSTATUS	RUNREFCPU	RUNREFMEM
			 * 3140	gpm			0.0		gpm			-m /dev/input/mice -t exps2				4		2			0			472
			 */
			String sql = "INSERT INTO " + DEV_PROGRESS_INFO + " VALUES('" + super.devId + "', 'PARAM_0', 'PARAM_1', 'PARAM_2', 'PARAM_3', PARAM_4, PARAM_5)";
			String msg = arr[a][0];
			if(msg != null && msg.length() > 0){
				String encode = "UTF-8";
				if("100004".equalsIgnoreCase(super.devOsType)){
					encode = "GBK";
				}
				sql = sql.replace("PARAM_0", (arr[a][0]==null?"":arr[a][0])).replace("PARAM_1", (arr[a][1]==null?"":arr[a][1])).replace("PARAM_2", (arr[a][3]==null?"":super.preToStringHex(arr[a][3], encode)));
				sql = sql.replace("PARAM_3", (arr[a][4]==null?"":arr[a][4])).replace("PARAM_4", (arr[a][7]==null?"0":arr[a][7])).replace("PARAM_5", (arr[a][8]==null?"0":arr[a][8]));
//				int i = 0;
//				logger.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
//				logger.debug(arr[a][0] + "\t" + arr[a][1] + "\t" + arr[a][3] + "\t" + arr[a][4] + "\t" + arr[a][7] + "\t" + arr[a][8]);
				sqlList.add(sql);
			}
		}
//		for(String sql : sqlList){
//			logger.info("执行添加的SQL:" + sql);
//		}
		super.execSqlList(sqlList, DEV_PROGRESS_INFO);
	}
	
	
	public String removeSuffix(String msg){
		if(msg.endsWith("\\\\")){
			return msg.substring(0, msg.length() - 1);
		}
		return msg;
	}
	

	public static void main(String[] args) {
		String oid = "1.3.6.1.2.1.25.4.2.1.1.1";
		log.debug(oid.substring(oid.lastIndexOf(".") + 1));
	}
}
