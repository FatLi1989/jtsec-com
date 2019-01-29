package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 已被废弃，被InterfaceEditParse中内存类取代
 * @author surpassE
 * @time 2015-12-17
 *
 */
@Slf4j
public class InterfaceEditParse extends AbstractSnmpParse implements SnmpParseI {
	/**
	 * Logger for this class
	 */
	private static final String DEV_INTERFACE_INFO = "dev_interface_info";

	public InterfaceEditParse(){}
	
	public InterfaceEditParse(String devId){
		super.devId = devId;
	}
	
	@Override
	public void parseVb(Vector<?> vector) {
		super.vectorToArr2(vector, 8);
		List<String> sqlList = new ArrayList<String>();
		for(int a = 0; a < arr.length; a++){
//			String sql = "INSERT INTO " + DEV_INTERFACE_INFO + " VALUES('" + super.devId + "', PARAM_0, 'PARAM_1', PARAM_2, 'PARAM_3', 'PARAM_4', 'PARAM_5', PARAM_6, PARAM_7)";
			String sql = "UPDATE " + DEV_INTERFACE_INFO + " SET if_addr='PARAM_0', if_netmask='PARAM_2' WHERE dev_id='" + this.devId + "' AND if_index=PARAM_1";
			String msg = arr[a][0];
			if(msg != null && msg.length() > 0){
				int i = 0;
				sql = sql.replace("PARAM_" + (i++), (arr[a][0] == null?"":arr[a][0])).replace("PARAM_" + (i++), (arr[a][1] == null?"-1":arr[a][1])).replace("PARAM_" + (i++), (arr[a][2] == null?"":arr[a][2]));
//				sql = sql.replace("PARAM_" + (i++), arr[a][3]);
//				logger.debug(arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++] + "\t" + arr[a][i++]);
				sqlList.add(sql);
			}
		}
		super.execSqlList(sqlList);
	}
}
