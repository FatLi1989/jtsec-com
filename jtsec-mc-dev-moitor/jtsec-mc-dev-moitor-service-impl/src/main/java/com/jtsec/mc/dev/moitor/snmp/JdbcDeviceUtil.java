package com.jtsec.mc.dev.moitor.snmp;

import com.jtsec.common.Constants.LogCons;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.mc.dev.moitor.pojo.model.NemDeviceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class JdbcDeviceUtil {
	/**
	 * Logger for this class
	 */
	private static List<NemDeviceInfo> list = new ArrayList<NemDeviceInfo>();

	static{
		loadDevices();
	}

	/**
	 * 获得存储设备syslog信息的表名
	 * @param ip
	 * @param region
	 * @return
	 */
	public static String findDevTableName(String ip, String region){
		List<NemDeviceInfo> list = modifyList("get");
		for(NemDeviceInfo ndi : list){
			if(ip != null && ip.equals(ndi.getDevIp()) && region != null && region.equals(ndi.getRegionId())){
				String devType = ndi.getDevType();
				String tableName = ndi.getDevTableName();
				if("01".equals(devType)){
					tableName += "_gap_log";
				}else//如果为防火墙 
				    if(LogCons.DEV_TYPE_CODE_FW.equals(devType)){
					tableName += "_fw_log";
				}else//如果为IPS
				    if(LogCons.DEV_TYPE_CODE_IPS.equals(devType)){
					tableName += "_ips_log";
				}
				return tableName;
			}
		}
		return null;
	}

	/**
	 * 获得存储设备syslog信息的表名
	 * @param ip
	 * @return
	 */
	public static String findDevTableName(String ip){
		List<NemDeviceInfo> list = modifyList("get");
		for(NemDeviceInfo ndi : list){
			if(ip != null && ip.equals(ndi.getDevIp())){
				String devType = ndi.getDevType();
				String tableName = ndi.getDevTableName();
				if("01".equals(devType)){
                    tableName += "_gap_log";
                }else//如果为防火墙 
                    if(LogCons.DEV_TYPE_CODE_FW.equals(devType)){
                    tableName += "_fw_log";
                }else//如果为IPS
                    if(LogCons.DEV_TYPE_CODE_IPS.equals(devType)){
                    tableName += "_ips_log";
                }
				return tableName;
			}
		}
		return null;
	}

	/**
	 * 通过设备ip查询设备信息
	 * @param ip
	 * @param region
	 * @return
	 */
	public static String findDeviceByIpRegion(String ip, String region){
		List<NemDeviceInfo> list = modifyList("get");
		for(NemDeviceInfo ndi : list){
			if(ip != null && ip.equals(ndi.getDevIp()) && region != null && region.equals(ndi.getRegionId())){
				return ndi.getDevIdentify();
			}
		}
		return null;
	}


	/**
	 * 通过指定ip和区域标识是否存在
	 * @param ip
	 * @param region
	 * @return
	 */
	public static boolean isMonitorDev(String ip, String region){
		List<NemDeviceInfo> list = modifyList("get");
		for(NemDeviceInfo ndi : list){
			if(ip != null && ip.equals(ndi.getDevIp()) && region != null && region.equals(ndi.getRegionId())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过指定ip和区域标识是否存在
	 * @param ip
	 * @param
	 * @return
	 */
	public static boolean isMonitorDev(String ip){
		List<NemDeviceInfo> list = modifyList("get");
		//		for(NemDeviceInfo ndi : list){
		//			logger.debug("要监控的ip地址：" + ndi.getDevIp());
		//		}
		for(NemDeviceInfo ndi : list){
			if(ip != null && ip.equals(ndi.getDevIp()) && "1".equals(ndi.getDevSyslogGather())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 设备添加更新或是删除时，重新加载设备集合 
	 */
	public static void reloadDevice(){
		modifyList("modify");
		//设备更新时立即获得设备监控的最新数据
		SnmpGetThread.startSnmpGet();
	}
	
	/**
	 * 获得所有的管理的设备信息
	 * @return
	 */
	public static List<NemDeviceInfo> getAllDevice(){
		List<NemDeviceInfo> list = modifyList("get");
		return list;
	}
	
	/**
	 * 重新加载监控的设备配置信息
	 */
	private static void loadDevices(){
		String sql = "SELECT * FROM nem_device_info";
		Connection conn = null;
		list = new ArrayList<NemDeviceInfo>();
		try {
			conn = DbUtil.getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				NemDeviceInfo ndi = new NemDeviceInfo();
				ndi.setDevIdentify(rs.getString("dev_identify"));
				ndi.setDevIp(rs.getString("dev_ip"));
				ndi.setRegionId(rs.getInt("region_id"));
				ndi.setDevTableName(rs.getString("dev_table_name"));
				ndi.setDevType(rs.getString("dev_type"));
				ndi.setDevOsType(rs.getString("dev_os_type"));
				ndi.setDevStatus(rs.getString("dev_status"));
				ndi.setDevPort(rs.getString("dev_port"));
				ndi.setDevHostname(rs.getString("dev_hostname"));
				ndi.setDevSyslogGather(rs.getString("dev_syslog_gather"));
				//snmp连接信息
				ndi.setSnmpType(rs.getString("snmp_type"));
				ndi.setSnmpPort(rs.getString("snmp_port"));
				ndi.setSnmpRcommunity(rs.getString("snmp_rcommunity"));
				ndi.setSnmpWcommunity(rs.getString("snmp_wcommunity"));
				ndi.setSnmpDelay(rs.getInt("snmp_delay"));
				ndi.setSnmpReconnect(rs.getInt("snmp_reconnect"));
				list.add(ndi);
			}
			log.info ("list.size= {}", list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DbUtil.closeConn(conn);
		}
	}
	
	/**
	 * 
	 * @param oper
	 * @return
	 */
	private static synchronized List<NemDeviceInfo> modifyList(String oper){
		List<NemDeviceInfo> rsList = new ArrayList<NemDeviceInfo>();
		if("modify".equals(oper)){
			if(list != null && list.size() > 0){
				list.removeAll(list);
			}
			loadDevices();
		}else{
			rsList.addAll(list);
		}
		return rsList;
	}
}
