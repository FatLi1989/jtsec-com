package com.jtsec.mc.dev.moitor.snmp;

import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.mc.dev.moitor.pojo.model.HandType;
import com.jtsec.mc.dev.moitor.pojo.model.NemDeviceInfo;
import com.jtsec.mc.dev.moitor.snmp.parse.*;
import com.jtsec.mc.dev.moitor.snmp.util.SnmpGetUtil;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * SNMP获得设备信息的线程
 * 2015-09-21
 * @author surpassE
 * 
 */
@Slf4j
public class SnmpGetThread implements Runnable{
	/**
	 * Logger for this class
	 */


	//	private static final int GAP_TIME = 60000;
	private int GAP_TIME = 60000;
	private final static String WARN_EVENT_INFO = "warning_event_info";
	public static void startGetThreadOpr(){
		new Thread(new SnmpGetThread()).start();
	}

	public void run(){
		while(true){
			try {
				Thread.sleep(GAP_TIME);
				startSnmpGet();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void startSnmpGet(){
		log.info ("开始执行轮询操作");
		//获得所有集控的设备
		List<NemDeviceInfo> list = JdbcDeviceUtil.getAllDevice();
		for(NemDeviceInfo ndi : list){
			//			 && ndi.getDevIdentify().indexOf("cisco") > -1
			if("1".equals(ndi.getDevStatus())){
				//加载MEM信息
				//				SnmpGetUtil.snmpWalkSync(ndi, new MemParse(ndi.getDevIdentify()), HandType.MEM);
				//获得硬盘的空间
				//				SnmpGetUtil.snmpWalkSync(ndi, new DiskParse(ndi.getDevIdentify()), HandType.DISK);
				SnmpGetUtil.snmpWalkSync(ndi, new MemDiskParse (ndi), HandType.MEM_DISK);
				//加载CPU信息
				SnmpGetUtil.snmpWalkSync(ndi, new CpuParse (ndi), HandType.CPU);
				//加载设备网卡信息
				SnmpGetUtil.snmpWalkSync(ndi, new InterfaceParse (ndi), HandType.INTERFACE);
//				SnmpGetUtil.snmpWalkSync(ndi, new InterfaceParse(ndi.getDevIdentify()), HandType.INTERFACE);
//				SnmpGetUtil.snmpWalkSync(ndi, new InterfaceEditParse(ndi.getDevIdentify()), HandType.INTERFACE_EDIT);
				//获得进程信息
				SnmpGetUtil.snmpWalkSync(ndi, new ProgressParse (ndi.getDevIdentify()), HandType.PROGRESS);
				//加载安装的软件信息
				SnmpGetUtil.snmpWalkSync(ndi, new SoftwareParse (ndi.getDevIdentify()), HandType.SOFTWARE);
			}
		}
	}

	/**
	 * 对以监控的设备按运行状态进行分组
	 * @param flag run可管理设备	stop 不可管理设备
	 * @return
	 */
	public static List<NemDeviceInfo> pollDevStatus(String flag){
		List<NemDeviceInfo> runList = new ArrayList<NemDeviceInfo>();
		List<NemDeviceInfo> stopList = new ArrayList<NemDeviceInfo>();
		List<NemDeviceInfo> allList = new ArrayList<NemDeviceInfo>();
		List<NemDeviceInfo> list = JdbcDeviceUtil.getAllDevice();
		if(list != null && list.size() > 0){
			for(NemDeviceInfo ndi : list){
				if("1".equals(ndi.getDevStatus())){
					ndi.setDevStatus("0");
					ndi.setDisConn("0");
					String devOsType = ndi.getDevOsType();
					ndi.setDevOsType("SNMP_BASE");
					SnmpGetUtil.snmpGetSync(ndi, ndi, HandType.TEST);
					ndi.setDevOsType(devOsType);
					if("1".equals(ndi.getDisConn())){
						runList.add(ndi);
					}else{
						stopList.add(ndi);
					}
					ndi.setDevStatus("1");
					allList.add(ndi);
				}
			}
		}
		if("run".equalsIgnoreCase(flag)){
			return runList;
		}else if("stop".equalsIgnoreCase(flag)){
			return stopList;
		}else if("all".equalsIgnoreCase(flag)){
			return allList;
		}
		return list;
	}

	
	/**
	 * 未连接断开的设备添加warn日志
	 * @param list
	 */
	public static void saveDisConnWarnEvent(List<NemDeviceInfo> list){
		Connection conn = null;
		try {
			conn = DbUtil.getConn(DbUtil.DB_JTSEC_LOG);
			for(NemDeviceInfo ndi : list){
				//yyyy-MM-dd时间毫秒数
				long time = DateUtil.parseSimple(DateUtil.getNowTime("yyyy-MM-dd")).getTime();
				String sql = "SELECT * FROM " + WARN_EVENT_INFO + " WHERE dev_id='" + ndi.getDevIdentify() + "' AND happen_time >= " + time + " AND restore_time = 0";
				boolean flag = true;
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					if("1".equals(ndi.getDisConn())){	//如果设备状态为1，那么表示设备已经回复连接，更新异常表中的修复时间(restore_time)
						String id = rs.getString("id");
						sql = "UPDATE " + WARN_EVENT_INFO + " SET restore_time=" + System.currentTimeMillis() + " WHERE id='" + id + "';";
						conn.prepareStatement(sql).execute();
					}else{
						flag = false;
						break;
					}
				}
				//logger.info(ndi.getDevStatus());
				if(flag && "0".equals(ndi.getDisConn())){	//设备连接状态为0，且异常日志表没有记录，那么添加异常日志记录
					String addSql = "INSERT INTO " + WARN_EVENT_INFO + " VALUES('" + UUID.randomUUID().toString()+ "', '" + ndi.getDevIdentify() + "', '" + DateUtil.getNowTime() + "', '0002', '断开连接', " + System.currentTimeMillis() + ", '', 0, '')";
					conn.prepareStatement(addSql).execute();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DbUtil.closeConn(conn);
		}
	}
	
	public static void main(String[] args) {
		//		SnmpGetThread.startSnmpGet();

				List<NemDeviceInfo> list = SnmpGetThread.pollDevStatus("");
				if(list != null && list.size() > 0){
					for(NemDeviceInfo ndi : list){
						log.info(ndi.getDevIdentify());
					}
				}
				SnmpGetThread.saveDisConnWarnEvent(list);

//		SnmpGetThread.saveDisConnWarnEvent(SnmpGetThread.pollDevStatus(""));
	}
}
