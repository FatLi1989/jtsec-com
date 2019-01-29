package com.jtsec.mc.dev.moitor.snmp.parse;

import com.jtsec.mc.dev.moitor.pojo.model.NemDeviceInfo;
import com.jtsec.mc.dev.moitor.snmp.AbstractSnmpParse;
import com.jtsec.mc.dev.moitor.snmp.SnmpParseI;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * 采集MEM信息和硬盘的空间
 * @author zhanghx
 *
 */
@Slf4j
public class MemDiskParse extends AbstractSnmpParse implements SnmpParseI {
	/**
	 * Logger for this class
	 */
	private static final String DEV_DISK_INFO = "dev_disk_info";
	private static final String DEV_MEM_INFO = "dev_mem_info";
	
	public MemDiskParse(){}
	
	public MemDiskParse(String devId){
		super.devId = devId;
	}
	public MemDiskParse(NemDeviceInfo nemDeviceInfo){
	    super.nemDeviceInfo = nemDeviceInfo;
	    super.devId = nemDeviceInfo.getDevIdentify();
	}
	
	@Override
	public void parseVb(Vector<?> vector) {
		List<String> sqlList = new ArrayList<String>();
		super.vectorToArr(vector);
		super.showArrMsg();
		int memNum = 0;
		List<String> list = new ArrayList<String>();
		String tabPrefix = super.nemDeviceInfo.getDevTableName();
        String tableName = tabPrefix + "_" + DEV_MEM_INFO;
		//  PARAM_0 --> `mem_swap_total` 
		//  PARAM_1 --> `mem_swap_ava` 
		//  PARAM_2 --> `mem_total` 
		//  PARAM_3 --> `mem_ava` 
		//  PARAM_4 --> `mem_buf` 
		//  PARAM_5 --> `mem_cache` 
		String sql = "INSERT INTO " + tableName + " VALUES('" + super.devId + "', 'GATHER_TIME', PARAM_0, PARAM_1, PARAM_2, PARAM_3, PARAM_4, PARAM_5, MEM_RATIO, DATA_TYPE)";
		for(int a = 0; a < arr.length; a++){
			String msg = arr[a][0];
			if(msg != null && msg.length() > 0){
				if("100004".equals(this.devOsType) || "100005".equals(this.devOsType) ){	//Linux window
					String descr = arr[a][2];
					if(descr != null){
					    // 解析磁盘使用率信息
						if((descr.startsWith("/") || descr.indexOf(":") > -1)){					
							String tmpSql = this.parseToDisk(arr[a]);
							sqlList.add(tmpSql);
						// 解析内存使用率信息
						}else if(descr.toLowerCase().indexOf("Virtual Memory".toLowerCase()) > -1 || descr.toLowerCase().indexOf("Physical Memory".toLowerCase()) > -1){
							sql = this.parseToMem(arr[a], sql, descr, list);
							memNum++;
							this.parseMemWarnInfo(memNum, list, sql, sqlList);
						}
					}
				}else if("100003".equals(this.devOsType)){	//三层交换机
				     sql = this.parseSwitchesMem(a, sql);
				     sqlList.add(sql);
				}
			}
		}
		super.execSqlList(sqlList, DEV_DISK_INFO);
	}
	
	/**
	 * 
	 * @param arr
	 * @param sql
	 * @param flag
	 * @return
	 */
	private String parseToMem(String[] arr, String sql, String flag, List<String> list){
		int j = 0;
		if(flag.indexOf("Physical") > -1){
			j = 3;
		}
		sql = sql.replaceAll("PARAM_" + j++, arr[3] == null?"0":arr[3]);
		list.add(arr[3] == null?"0":arr[3]);
		sql = sql.replaceAll("PARAM_" + j++, arr[4] == null?"0":arr[4]);
		list.add(arr[4] == null?"0":arr[4]);
		sql = sql.replaceAll("PARAM_" + j++, arr[5] == null?"0":arr[5]);
		list.add(arr[5] == null?"0":arr[5]);
		return sql;
	}
	
	/**
	 * 解析内存告警信息
	 * @param memNum
	 * @param list
	 * @param sql
	 * @param sqlList
	 */
	public void parseMemWarnInfo(int memNum, List<String> list, String sql, List<String> sqlList){  
        if(memNum == 2){
            float units = Float.parseFloat(list.get(0));
            float size = Float.parseFloat(list.get(1));
            float used = Float.parseFloat(list.get(2));
            float units1 = Float.parseFloat(list.get(3));
            float size1 = Float.parseFloat(list.get(4));
            float used1 = Float.parseFloat(list.get(5));
            float memRatio = 0L;
            if((size * units + size1 * units1) > 0){
                memRatio = ((used * units + used1 * units1) * 100)/(size * units + size1 * units1);
            }                           
            sql = sql.replaceAll("GATHER_TIME", super.getNowGatherTime());
            sql = sql.replaceAll("MEM_RATIO", memRatio + "");
            sql = sql.replaceAll("DATA_TYPE", "0");
            //内存超过95%添加一条告警日志
            if(memRatio >= 95){
                String addSql = "INSERT INTO warning_event_info VALUES('" + UUID.randomUUID().toString()+ "', '" + super.devId + "', '0005', 'MEM使用率超过了" + memRatio + "%', " + System.currentTimeMillis() + ", '', 0)";
                sqlList.add(addSql);
            }
            sqlList.add(sql);
        }
	}
	
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	private String parseToDisk(String[] arr){
		String sql = "REPLACE INTO " + DEV_DISK_INFO + " VALUES('" + super.devId + "', 'PARAM_0', PARAM_1, PARAM_2, PARAM_3, 'PARAM_4')";
		String path = arr[2];
		if(path.indexOf(":") > -1){
			sql = sql.replaceAll("PARAM_0", path.substring(0, path.indexOf(":") + 1));
		}else{
			sql = sql.replaceAll("PARAM_0", path);
		}
		sql = sql.replaceAll("PARAM_1", arr[3] == null?"0":arr[3]);
		sql = sql.replaceAll("PARAM_2", arr[4] == null?"0":arr[4]);
		sql = sql.replaceAll("PARAM_3", arr[5] == null?"0":arr[5]);
		sql = sql.replaceAll("PARAM_4", "");
		return sql;
	}
	
	/**
	 * 解析三层交换机mem
	 * @param index
	 * @param sql
	 * @param
	 */
	public String parseSwitchesMem(int index, String sql){
        String processor = arr[index][0];
        if(processor != null && processor.equalsIgnoreCase("processor")){
            float used = Float.parseFloat(arr[index][1]);
            float free = Float.parseFloat(arr[index][2]);
            float memRatio = ((used) * 100)/(used + free);
            String tabPrefix = super.nemDeviceInfo.getDevTableName();
            String tableName = tabPrefix + "_" + DEV_MEM_INFO;
            sql = "INSERT INTO " + tableName + " VALUES('" + super.devId + "', 'GATHER_TIME', -1, " + used + ", " + free + ", -1, -1, -1, MEM_RATIO, DATA_TYPE)";
            sql = sql.replaceAll("GATHER_TIME", super.getNowGatherTime());
            sql = sql.replaceAll("MEM_RATIO", memRatio + "");
            sql = sql.replaceAll("DATA_TYPE", "0");
        }
        return sql;
	}
}
