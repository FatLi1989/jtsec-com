package com.jtsec.mc.dev.moitor.snmp.handler;

import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.mc.dev.moitor.snmp.AbstractDataHandler;
import com.jtsec.mc.dev.moitor.snmp.JdbcDeviceUtil;
import com.jtsec.mc.dev.moitor.snmp.util.OidUtil;
import com.surpass.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.snmp4j.smi.VariableBinding;

import java.util.List;

/**
 * 接收SNMP中的数据信息是设备信息
 * @author surpassE
 * @version 1.0.0
 * @since 2015-08-18
 *
 */
@Slf4j
public class DeviceDataHandler extends AbstractDataHandler {
	/**
	 * Logger for this class
	 */

	//数据到达临界值是否删除
	private static String chkLogSec = "0";
	private static int limitCount = 30;
	private static int delCount = 10;
	private static int alertCount = 25;
	private static final String HANDTYPE_DEVINFO = "devinfo";
	
	public DeviceDataHandler(){}

	public DeviceDataHandler(String ip, String port, String monitorTime){
		super.ip = ip;
		super.port = port;
		super.monitorTime = monitorTime;
	}

	public DeviceDataHandler(String ip, String port, String region, String monitorTime){
		super.ip = ip;
		super.port = port;
		super.region = region;
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
		if(HANDTYPE_DEVINFO.equals(dataType)){
			String tableName = OidUtil.getTableName(vbList.get(1).getVariable().toString());
			log.info(vbList.get(1).getVariable().toInt() + "\t tableName = " + tableName);
			String[] arr = new String[]{"", "", "", "", "", "", super.ip, super.port, ""};
			/**
			 * 设备数据类型** = 1.3.6.1.4.1.39056.1.1
			 * **.1:devId;**.2:devInfo;**.3:devVender;**.4:devSoftVersion;**.5:devHardVersion;**.6:devDomain
			 */
			for(int i = 2; i < vbList.size(); i++){
				VariableBinding vb = vbList.get(i);
				String oid = vb.getOid().toString();
				String variableMsg = super.getStringByEncode(vb.getVariable().toString(), null);
				int index = Integer.parseInt(oid.substring(oid.lastIndexOf(".") + 1)) - 1;
				arr[index] = variableMsg;
			}
			String devIdentify = JdbcDeviceUtil.findDeviceByIpRegion(super.ip, super.region);
			String sql = "";
			//判断有没有对应的表来接收数据信息
			if(tableName != null && tableName.length() > 0){
				String devDataTableName = JdbcDeviceUtil.findDevTableName(ip, region);
				if(devDataTableName == null || devDataTableName == ""){
					devDataTableName = arr[0] + "_" + System.currentTimeMillis();
				}
				arr[arr.length - 1] = devDataTableName;
				if(devIdentify == null){
					sql = "INSERT INTO " + tableName + "(dev_identify,memo,dev_vender,dev_version,dev_hard_version,dev_ip,port,dev_table_name) VALUES(0?,1?,2?,3?,4?,6?,7?,8?)";
					sql = DbUtil.setSqlParams(sql, arr);
				}else{
					sql = "UPDATE" + tableName + "SET memo=1?, dev_vender=2?, dev_version=3?, dev_hard_version=4?, dev_ip=6?,port=7?, dev_table_name=8?";
					sql = DbUtil.setSqlParams(sql, arr);
					sql += "WHERE dev_identify='" + devIdentify + "', and regionId = " + super.region;
				}
				try {
					//通过存储过程创建接收设备系统信息的表
					DbUtil.call(null, "create_two_way_dev_table_proc", devDataTableName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				sqlList.add(sql);
			}
		}else{
			this.callNextHandler(dataType, sqlList, vbList);
		}
	}

	@Override
	public void handleRequest(String dataType, List<String> sqlList, String data) {
		if("dev-conf".equals(dataType)){
			String[] arr = data.split(",");
			String equipmentId = arr[0];
			String logType = arr[4];
			if("888999".equals(logType)){
				chkLogSec = arr[1];
				limitCount = Integer.parseInt(arr[2]);
				delCount = Integer.parseInt(arr[3]);
				alertCount = Integer.parseInt(arr[4]);
			}if("7".equals(logType) || "8".equals(logType)){	//服务与规则信息(服务信息-7，规则信息-8)
				String value = CollectionUtil.arrToString(arr, 11);
				String sql = "INSERT INTO service_info(equipment_id, time, type, value, record_no) VALUES(";
				sql = DbUtil.bindSqlParam(sql, null, equipmentId, arr[1], logType, value, arr[11]);
				sqlList.add(sql);
			}else if("9".equals(logType)){
				String sql = "INSERT INTO config_info (equipment_id, time, in_ip, in_netmask, in_ip1, in_netmask1, in_gateway, in_dns, out_ip, out_netmask, out_ip1,out_netmask1, out_gateway,out_dns) VALUES(";
				sql = DbUtil.bindSqlParam(sql, CollectionUtil.subArray(arr, 11, 12), equipmentId, DateUtil.getNowTime());
				sqlList.add(sql);
			}else{
				String tableName = JdbcDeviceUtil.findDevTableName(super.ip, super.region).replace("-", "_");
				/**
				 * data数据信息格式
				 * 日志类型为5、6
				 * 设备名称,记录时间,地址,模块,日志类型,程序名,日志级别,信息类型，操作员,事件,消息,服务名, 服务描述, 源IP,源端口,目的IP,目的端口,连接数
				 * eg:
				 * y2k4-igap097,2015-05-26 16:59:14,192.168.8.80,tcp,6,tcp,6,0,tcp,,断开连接,tcp-8080,tcp-8080,192.168.8.158,54072,192.168.8.96,8080,0
				 * y2k4-igap097,2015-05-26 16:59:15,192.168.8.80,tcp,5,tcp,6,0,tcp,,开始连接,tcp-8080,tcp-8080,192.168.8.158,54075,192.168.8.96,8080,1 
				 */
//				if("5".equals(logType) || "6".equals(logType)){
//					this.chkLogSec(tableName + "_a", sqlList);
//					/*
//					String sql = "INSERT INTO " + tableName + "(equipment_id, time, log_ip, module, log_type, program," +
//							"log_level, warning_type, eve_actor, eve_object, eve_action, msg, inner_ip, outer_ip, log_device," +
//							"serv_name, serv_memo, src_ip, src_port, dst_ip, dst_port, conn_count) VALUES(";
//					*/
//					String sql = "INSERT INTO " + tableName + "_a" + "(equipment_id, time, log_ip, module, log_type, program, log_level, warning_type, eve_actor," +
//							" eve_object, eve_action, serv_name, serv_memo, src_ip, src_port, dst_ip, dst_port, conn_count) VALUES(";
//					sql = DbUtil.bindSqlParam(sql, arr);
//					sqlList.add(sql);
//				}
				this.chkLogSec(tableName , sqlList);
				String sql = "INSERT INTO " + tableName + "(msg, equipment_id, time, log_ip, module, log_type, program, log_level, warning_type, eve_actor," +
						" eve_object, eve_action) VALUES(";
				sql = DbUtil.bindSqlParam(sql, CollectionUtil.subArray(arr, 0, 11), CollectionUtil.arrToString(arr, 11, ""));
				sqlList.add(sql);
			}
		}else{
			super.callNextHandler(dataType, sqlList, data);
		}
	}
	
	/**
	 * 检查数据表的安全性，判断是否到达了告警值或是临界值
	 * @param tableName
	 * @param sqlList
	 */
	private void chkLogSec(String tableName, List<String> sqlList){
		if("1".equals(chkLogSec)){
			int total = DbUtil.getTotalAndCloseConnn(null, "SELECT COUNT(*) FROM " + tableName);
			String sql = "INSERT INTO log_warn_info(time, tableName, limit_count, del_count, alert_count, msg) VALUES(";
			if(total > limitCount){
				String delSql = "DELETE FROM " + tableName + " ORDER BY id DESC LIMIT " + delCount;
				sqlList.add(delSql);
				sql = DbUtil.bindSqlParam(sql, null, DateUtil.getNowTime(), tableName, limitCount+"", delCount+"", alertCount+"", "删除日志");
				sqlList.add(sql);
			}
			
			if(total > alertCount){
				sql = DbUtil.bindSqlParam(sql, null, DateUtil.getNowTime(), tableName, limitCount+"", delCount+"", alertCount+"", "日志已达到告警值!");
				sqlList.add(sql);
			}
		}
	}

	/**
	 * 创建设备日志a表
	 * 
	 * @param tableAName
	 * @return
	 */
	protected String getInitDevLogASql(String tableAName){
		StringBuffer sqlBuf = new StringBuffer();
		// 创建表结构
		sqlBuf.append("CREATE TABLE ");
		sqlBuf.append(tableAName);
		sqlBuf.append("(equipment_id varchar(64) NOT NULL default'',");
		sqlBuf.append("time datetime NOT NULL default '0000-00-0000:00:00',");
		sqlBuf.append("log_type tinyint(4) NOT NULL default '0',");
		sqlBuf.append("module varchar(16) NOT NULL default'',");
		sqlBuf.append("log_level tinyint(4) NOT NULL default '0',");
		sqlBuf.append("warning_type tinyint(4) NOT NULL default '0',");
		sqlBuf.append("eve_actor varchar(255) NOT NULL default'',");
		sqlBuf.append("eve_object varchar(255) NOT NULL default'',");
		sqlBuf.append("eve_action varchar(32) NOT NULL default'',");
		sqlBuf.append("msg varchar(255) NOT NULL default'',");
		sqlBuf.append("inner_ip varchar(128)default NULL,");
		sqlBuf.append("outer_ip varchar(128)default NULL,");
		sqlBuf.append("id bigint(38) NOT NULL auto_increment,");
		sqlBuf.append("log_device varchar(45)default NULL,");
		sqlBuf.append("ip varchar(45)default NULL,");
		sqlBuf.append("program varchar(45)default NULL,");
		sqlBuf.append("PRIMARY KEY USING BTREE(id),");
		sqlBuf.append("KEY Index_2 USING BTREE(time)");
		sqlBuf.append(") ENGINE = MyISAM DEFAULT CHARSET = utf8 ROW_FORMAT = DYNAMIC");
		return sqlBuf.toString();
	}

	/**
	 * 创建设备日志信息b表
	 * 
	 * @param tableBName
	 * @return
	 */
	protected String getInitDevLogBSql(String tableBName){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("CREATE TABLE ");
		sqlBuf.append(tableBName);
		sqlBuf.append("(equipment_id varchar(64) NOT NULL default '',");
		sqlBuf.append("time datetime NOT NULL default '0000-00-0000:00:00',");
		sqlBuf.append("log_ip varchar(32)default NULL,");
		sqlBuf.append("log_type tinyint(4) NOT NULL default '0',");
		sqlBuf.append("module varchar(16) NOT NULL default'',");
		sqlBuf.append("log_level tinyint(4) NOT NULL default '0',");
		sqlBuf.append("warning_type tinyint(4) NOT NULL default '0',");
		sqlBuf.append("eve_actor varchar(255) NOT NULL default'',");
		sqlBuf.append("eve_object varchar(255) NOT NULL default'',");
		sqlBuf.append("eve_action varchar(32) NOT NULL default'',");
		sqlBuf.append("msg varchar(255) NOT NULL default'',");
		sqlBuf.append("id bigint(38) NOT NULL auto_increment,");
		sqlBuf.append("log_device varchar(32)default NULL,");
		sqlBuf.append("service_name varchar(64)default NULL,");
		sqlBuf.append("src_ip varchar(32)default NULL,");
		sqlBuf.append("src_port int (10) unsigned NOT NULL default '0',");
		sqlBuf.append("dst_ip varchar(32)default NULL,");
		sqlBuf.append("dst_port int (10) unsigned NOT NULL default '0',");
		sqlBuf.append("conn_count int (10) unsigned default '0',");
		sqlBuf.append("service_descript varchar(45)default NULL,");
		sqlBuf.append("PRIMARY KEY(id),");
		sqlBuf.append("KEY Index_2 USING BTREE(equipment_id)");
		sqlBuf.append(") ENGINE = MyISAM DEFAULT CHARSET = UTF8 ROW_FORMAT = DYNAMIC");
		return sqlBuf.toString();
	}

	/**
	 * 创建设备日志清空事件
	 * 
	 * @param
	 * @return
	 */
	protected String getInitDevLogEventSql(String tableName){
		// 创建计划任务
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("DROP event event_" + tableName);
		sqlBuf.append("CREATE event event_");
		sqlBuf.append(tableName).append(" \n");
		sqlBuf.append("on schedule every 3 month \n");
		sqlBuf.append("starts CURRENT_TIMESTAMP + INTERVAL 5 second \n");
		sqlBuf.append("ENABLE \n" + "COMMENT '日志A表回滚计划任务' \n" + "DO \n");
		sqlBuf.append("delete from ");
		sqlBuf.append(tableName).append(";");					
		return sqlBuf.toString();
	}
	
	/**
	 * 创建系统日志信息存储表
	 * @param
	 * @return
	 */
	protected String getInitSystemLogSql(String tableName){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("CREATE TABLE ");
		sqlBuf.append(tableName);
		sqlBuf.append("(`id` bigint(38) NOT NULL AUTO_INCREMENT,");
		sqlBuf.append("`equipment_id` varchar(64) NOT NULL DEFAULT '' COMMENT '设备号，如Y2K4-igap097，AB\0s\0M A',");
		sqlBuf.append("`time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '时间，如2008-10-28 10:00:13，AB\0gap\0',");
		sqlBuf.append("`log_ip` varchar(45) DEFAULT NULL COMMENT 'IP，如192.168.8.23，AB\0\0\0',");
		sqlBuf.append("`module` varchar(16) NOT NULL DEFAULT '' COMMENT '模块，如auth_serv，AB\0ing\0',");
		sqlBuf.append("`log_type` varchar(4) NOT NULL DEFAULT '0' COMMENT '类型，如3，AB\0\0ent',");
		sqlBuf.append("`program` varchar(45) DEFAULT NULL COMMENT '程序名，如conf_serv:，A表\0\0Msg =',");
		sqlBuf.append("`log_level` varchar(4) NOT NULL DEFAULT '0' COMMENT '日志级别，如6  LOG_INFO  LOG_WARNING，AB\0se\0\n\0\0',");
		sqlBuf.append("`warning_type` varchar(4) NOT NULL DEFAULT '0' COMMENT '警告级别，如0，AB\0g_warn',");
		sqlBuf.append("`eve_actor` varchar(255) NOT NULL DEFAULT '' COMMENT '操作员，如super，AB\0\0rni\0',");
		sqlBuf.append("`eve_object` varchar(255) DEFAULT '' COMMENT '对象，AB',");
		sqlBuf.append("`eve_action` varchar(32) NOT NULL DEFAULT '' COMMENT '动作，如管理员登录，AB\0i\0rning\0\0',");
		sqlBuf.append("`msg` varchar(255) NOT NULL DEFAULT '' COMMENT '日志内容，如管理员super登录成功，AB\02k4_igap0\07\0\0',");
		sqlBuf.append("`inner_ip` varchar(128) DEFAULT NULL,");
		sqlBuf.append("`outer_ip` varchar(128) DEFAULT NULL,");
		sqlBuf.append("`log_device` varchar(45) DEFAULT NULL COMMENT '日志来源，如内端机或外端机，AB表\0e=''y2k4_igap\0\0',");
		sqlBuf.append("`serv_name` varchar(64) DEFAULT NULL COMMENT '服务名称，B表',");
		sqlBuf.append("`serv_memo` varchar(45) DEFAULT NULL COMMENT '服务描述，B表',");
		sqlBuf.append("`src_ip` varchar(32) DEFAULT NULL COMMENT '源IP，B\0\0',");
		sqlBuf.append("`src_port` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '源端口，B\0R\0\0',");
		sqlBuf.append("`dst_ip` varchar(32) DEFAULT NULL COMMENT '目的IP，B\0\0l',");
		sqlBuf.append("`dst_port` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '目的端口，B\0\0l\0e',");
		sqlBuf.append("`conn_count` int(10) unsigned DEFAULT '0' COMMENT '当前连接数，B',");
		sqlBuf.append("PRIMARY KEY (`id`) USING BTREE,");
		sqlBuf.append("KEY `Index_2` (`time`) USING BTREE");
		sqlBuf.append(") ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;");
		return sqlBuf.toString();
	}
	
	
	//=================== 伟思设备设备系统日志分析处理方法=============
//	if(data != null && data.length() > 0 && data.startsWith("021,")){
//		String[] arr = data.split(",");
//		int index = 0;
//		dataType = arr[index++];
//		String deviceId = arr[index++];
//		String servName = arr[index++];
//		String confInfo = arr[index++];
//		String tableName = "" + dataType;
//		String sql = "INSERT INTO " + tableName + "(device_id, serv_name, confi_info) VALUES(";
//		sql = DbUtil.bindSqlParam(sql, null, deviceId, servName, confInfo);
//		sqlList.add(sql);
//	}else{
//		/**
//		 * data数据信息格式
//		 * 设备名称,记录时间,操作员,日志类型,日志级别,事件,消息,源IP,源端口,目的IP,目的端口,服务标识(唯一，某些信息中没有次属性)
//		 */
//		String[] arr = data.split(",");
//		String deviceId = arr[0];
//		dataType = arr[3];
//		String tableName = deviceId + "" + dataType;
//		String column = "equipment_id, time, eve_actor,log_type,log_level,eve_action,msg,src_ip,src_port,dst_ip,dst_port";
//		if("b".equals("")){
//			column += ",serv_name";
//		}
//		String sql = "INSERT INTO " + tableName + "(" + column + ") VALUES(";
//		sql = DbUtil.bindSqlParam(sql, arr);
//		sqlList.add(sql);
//	}
}
