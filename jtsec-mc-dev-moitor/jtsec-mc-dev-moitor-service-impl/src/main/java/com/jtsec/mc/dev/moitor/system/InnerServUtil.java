package com.jtsec.mc.dev.moitor.system;

import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.cache.log.task.common.Sql2DbTask;
import com.jtsec.common.util.cache.log.task.normal.*;
import com.jtsec.common.util.cache.log.util.SyslogParserModuleConfig;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.common.util.surpass.PropertiesUtil;
import com.jtsec.mc.dev.moitor.snmp.DefaultCommandResponder;
import com.jtsec.mc.dev.moitor.snmp.SnmpGetThread;
import com.jtsec.mc.dev.moitor.snmp.util.SnmpUtil;
import com.jtsec.mc.dev.moitor.syslog.SyslogReceviceThread;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InnerServUtil {

	// 记录系统内部服务启动状态<服务名:线程对象/服务状态>
	public static Map<String, Object> innerServMap = new HashMap<String, Object> ();
	public static String SNMP_GET_SERV = "snmpGetServ";
	public static String SQL2DB_TASK = "sql2DbTask";
	public static String SQL_LOG_PUSH = "sqlLogPush";
	public static String JSON_LOG_PUSH = "jsonLogPush";
	public static String STRING_LOG_PUSH = "stringLogPush";
	public static String SQL_LOG_READ = "sqlLogRead";
	public static String JSON_LOG_READ = "jsonLogRead";
	public static String STRING_LOG_READ = "stringLogRead";
	public static String DB_STRUCT_SYNC_SERV = "dbStructSyncServ";

	/**
	 * 开启系统内部服务
	 */
	public static void startInnerServ () {
		InnerServUtil.startCacheSql2DbServ ();
		InnerServUtil.startSnmpGetServ ();
		InnerServUtil.startLogPushServ ();
		InnerServUtil.startLogReadServ ();
	}

	/**
	 * 开启SNMP轮询服务
	 */
	public static void startSnmpGetServ () {
		/** 新增snmp rsyslog服务监听 如果为集控设备，则开始监听*/

		SyslogReceviceThread.setRunFlag (true);
		//启动SNMP轮询线程
		SnmpGetThread.startGetThreadOpr ();

		try {
			SnmpUtil.startSnmp (PropertiesUtil.getValue ("snmp_param"), new DefaultCommandResponder ());
		} catch (Exception e) {
			e.printStackTrace ();
		}

		//创建接受SNMP的CPU、MEM数据的表
		//SnmpAnalyzeExecTask.createRecvCpuMemTable(DateUtil.format(System.currentTimeMillis()));
		InnerServUtil.innerServMap.put (InnerServUtil.SNMP_GET_SERV, "START");
	}

	/**
	 * 开启缓存SQL入库服务
	 */
	public static void startCacheSql2DbServ () {
		// 保存数据库的线程
		Sql2DbTask sql2DbTask = new Sql2DbTask ();
		sql2DbTask.init ();
		sql2DbTask.startThread ();
		sql2DbTask.start ();
		InnerServUtil.innerServMap.put (InnerServUtil.SQL2DB_TASK, sql2DbTask);
	}


	/**
	 * 开启日志推送服务
	 */
	public static void startLogPushServ () {
		// 开启日志推送服务
		String openSqlLogPush = SyslogParserModuleConfig.getValue (CacheKeyCons.OPEN_SQL_LOG_FILE_PUSH);
		String openJsonLogPush = SyslogParserModuleConfig.getValue (CacheKeyCons.OPEN_JSON_LOG_FILE_PUSH);
		String openStringLogPush = SyslogParserModuleConfig.getValue (CacheKeyCons.OPEN_STRING_LOG_FILE_PUSH);
		if (NotNullUtil.stringNotNull (openSqlLogPush) && openSqlLogPush.equals ("true")) {
			// 推送sql格式日志线程
			WriteSqlCache2FileTask writeSqlCache2FileTask = new WriteSqlCache2FileTask ();
			writeSqlCache2FileTask.init ();
			writeSqlCache2FileTask.startThread ();
			writeSqlCache2FileTask.start ();
			InnerServUtil.innerServMap.put (InnerServUtil.SQL_LOG_PUSH, writeSqlCache2FileTask);
		}
		if (NotNullUtil.stringNotNull (openJsonLogPush) && openJsonLogPush.equals ("true")) {
			// 推送json格式日志线程
			WriteJsonCache2FileTask writeJsonCache2FileTask = new WriteJsonCache2FileTask ();
			writeJsonCache2FileTask.init ();
			writeJsonCache2FileTask.startThread ();
			writeJsonCache2FileTask.start ();
			InnerServUtil.innerServMap.put (InnerServUtil.JSON_LOG_PUSH, writeJsonCache2FileTask);
		}
		if (NotNullUtil.stringNotNull (openStringLogPush) && openStringLogPush.equals ("true")) {
			// 推送string格式日志线程
			WriteStringCache2FileTask writeStringCache2FileTask = new WriteStringCache2FileTask ();
			writeStringCache2FileTask.init ();
			writeStringCache2FileTask.startThread ();
			writeStringCache2FileTask.start ();
			InnerServUtil.innerServMap.put (InnerServUtil.STRING_LOG_PUSH, writeStringCache2FileTask);
		}
	}

	/**
	 * 开启日志读取的服务
	 */
	public static void startLogReadServ () {
		// 开启日志读取的服务
		String openSqlLogRead = SyslogParserModuleConfig.getValue (CacheKeyCons.OPEN_SQL_LOG_FILE_READ);
		String openJsonLogRead = SyslogParserModuleConfig.getValue (CacheKeyCons.OPEN_JSON_LOG_FILE_READ);
		String openStringLogRead = SyslogParserModuleConfig.getValue (CacheKeyCons.OPEN_STRING_LOG_FILE_READ);
		if (NotNullUtil.stringNotNull (openSqlLogRead) && openSqlLogRead.equals ("true")) {
			// 扫描sql文件的线程
			ReadSqlCache4FileTask readSqlCache4FileTask = new ReadSqlCache4FileTask ();
			readSqlCache4FileTask.startTask ();
			readSqlCache4FileTask.start ();
			InnerServUtil.innerServMap.put (InnerServUtil.SQL_LOG_READ, readSqlCache4FileTask);
		}
		if (NotNullUtil.stringNotNull (openJsonLogRead) && openJsonLogRead.equals ("true")) {
			// 扫描json文件的线程
			ReadJsonCache4FileTask readJsonCache4FileTask = new ReadJsonCache4FileTask ();
			readJsonCache4FileTask.startTask ();
			readJsonCache4FileTask.start ();
			InnerServUtil.innerServMap.put (InnerServUtil.STRING_LOG_READ, readJsonCache4FileTask);
		}
		if (NotNullUtil.stringNotNull (openStringLogRead) && openStringLogRead.equals ("true")) {
			// 扫描string文件的线程
			ReadStringCache4FileTask readStringCache4FileTask = new ReadStringCache4FileTask ();
			readStringCache4FileTask.startTask ();
			readStringCache4FileTask.start ();
			InnerServUtil.innerServMap.put (InnerServUtil.STRING_LOG_READ, readStringCache4FileTask);
		}
	}

	public static void main (String[] args) {
		InnerServUtil.startInnerServ();

	}

}
