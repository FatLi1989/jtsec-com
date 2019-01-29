package com.jtsec.mc.dev.moitor.system.task.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.jtsec.common.Constants.KeyCons;
import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.cache.log.util.SyslogParserModuleConfig;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.NotNullUtil;
import com.jtsec.common.util.surpass.PropertiesUtil;
import com.jtsec.mc.dev.moitor.pojo.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class DatabaseSynchronUtil {


	public static Map<String, DruidDataSource> dsMap = new HashMap<String, DruidDataSource> ();

	public static Long FileCREATETIME = null;

	public static Connection getConnection (String... params) {
		Connection conn = null;

		String driver = PropertiesUtil.getDbValue (KeyCons.DB_DRIVER);
		String url = PropertiesUtil.getDbValue (KeyCons.DB_URL);
		String username = PropertiesUtil.getDbValue (KeyCons.DB_USERNAME);
		String password = PropertiesUtil.getDbValue (KeyCons.DB_PASSWORD);
		if (NotNullUtil.objectArrayNotNull (params)) {
			if (params.length == 6) {
				if (params[5].equals ("1")) {
					driver = "com.mysql.jdbc.Driver";
					url = "jdbc:mysql://" + params[0] + ":" + params[1] + "/" + params[4] + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
					username = params[2];
					password = params[3];
				} else if (params[5].equals ("2")) {
					driver = "oracle.jdbc.driver.OracleDriver";
					url = "jdbc:oracle:thin:@" + params[0] + ":" + params[1] + ":" + params[4] + "";
					username = params[2];
					password = params[3];
				} else if (params[5].equals ("3")) {
					driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
					url = "jdbc:sqlserver://" + params[0] + ":" + params[1] + ";DatabaseName=" + params[4] + "";
					username = params[2];
					password = params[3];
				}
			}
		}
		DruidDataSource ds = dsMap.get (url);
		if (ds == null) {
			ds = (DruidDataSource) getDataSource (driver, url, username, password);
			dsMap.put (url, ds);
		}
		try {
			conn = ds.getConnection ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}
		return conn;
	}

	/**
	 * 创建数据库连接池
	 *
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static DataSource getDataSource (String driver, String url, String username, String password) {
		DruidDataSource ds = new DruidDataSource ();
		ds.setMaxActive (5);
		ds.setMinIdle (1);
		ds.setInitialSize (3);
		ds.setPoolPreparedStatements (false);
		ds.setValidationQuery ("SELECT 1");
		ds.setTestOnBorrow (false);
		ds.setTestOnReturn (false);
		ds.setTestWhileIdle (true);
		ds.setMinEvictableIdleTimeMillis (30);
		ds.setMaxWaitThreadCount (1000);
		ds.setTimeBetweenEvictionRunsMillis (120000);
		ds.setDriverClassName (driver);
		ds.setUrl (url);
		ds.setUsername (username);
		ds.setPassword (password);
		return ds;

	}

	/**
	 * 获取对应数据库sql
	 *
	 * @param hostDatabaseType
	 * @param hostDatabaseName
	 * @return
	 */
	public static String getSqlByDatabaseType (Integer hostDatabaseType, String hostDatabaseName) {
		String sql = null;

		if (hostDatabaseType.equals (1)) {
			sql = "SELECT " + MysqlSystemSql.SELECT + " FROM " + MysqlSystemSql.FROM + " WHERE `tb`.`TABLE_SCHEMA` IN ('" + hostDatabaseName + "')";
		} else if (hostDatabaseType.equals (2)) {
			sql = "SELECT " + OracleSystemSql.SELECT + " FROM " + OracleSystemSql.FROM;
		} else if (hostDatabaseType.equals (3)) {
			sql = "SELECT " + SqlServerSystemSql.SELECT + " FROM " + SqlServerSystemSql.FROM;
		}
		return sql;
	}

	/**
	 * 拿到连接
	 *
	 * @param
	 * @param sql
	 * @return
	 */
	public static List<DBTableStruct> getTableColumn (TaskDatabaseSynchron taskDatabaseSynchron, String sql) {

		List<DBTableStruct> tableColumnList = new ArrayList<DBTableStruct> ();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//创建连接
			conn = DatabaseSynchronUtil.getConnection (taskDatabaseSynchron.getHostIp (),
					taskDatabaseSynchron.getHostPort (),
					taskDatabaseSynchron.getHostUsername (),
					taskDatabaseSynchron.getHostPassword (),
					taskDatabaseSynchron.getHostDatabaseName (),
					String.valueOf (taskDatabaseSynchron.getHostDatabaseType ()));
			ps = conn.prepareStatement (sql);
			log.debug (sql);
			rs = ps.executeQuery ();
			while (rs.next ()) {
				DBTableStruct dataSynchronousPoji = new DBTableStruct ();
				dataSynchronousPoji.setTableSchema (SyncConfigFile.ternaryExpression (rs.getString ("database_name")));
				dataSynchronousPoji.setTableName (SyncConfigFile.ternaryExpression (rs.getString ("table_name")));
				dataSynchronousPoji.setColumnComment (SyncConfigFile.ternaryExpression (rs.getString ("column_comment")));
				dataSynchronousPoji.setColumnKey (SyncConfigFile.ternaryExpression (rs.getString ("column_key")));
				dataSynchronousPoji.setColumnName (SyncConfigFile.ternaryExpression (rs.getString ("column_name")));
				dataSynchronousPoji.setDataLength (SyncConfigFile.ternaryExpression (rs.getString ("data_length")));
				dataSynchronousPoji.setColumnType (SyncConfigFile.ternaryExpression (rs.getString ("column_type")));
				tableColumnList.add (dataSynchronousPoji);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DbUtil.closeRs (rs);
			DbUtil.closePs (ps);
			DbUtil.closeConn (conn);
		}
		return tableColumnList;
	}

	/**
	 * 获取TaskTableColumn条数
	 *
	 * @return
	 */
	public static int getBakTableColumnCount (String dataBaseName, Map<String, Object> map) {
		Connection conn;
		int total = 0;
		try {
			conn = DbUtil.getConn ("jtsec_com");
			String sqlCount = "select count(*) from task_table_column where 1=1 ";
			if (!StringUtil.isEmpty (dataBaseName))
				sqlCount += " and db_name = '" + dataBaseName + "' ";
			if (map != null) {
				if (NotNullUtil.stringNotNull (String.valueOf (map.get ("tableName"))))
					sqlCount += " and table_name ='" + map.get ("tableName") + "';";
			}
			total = DbUtil.getTotal (conn, sqlCount);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return total;
	}

	/**
	 * 保存或者修改task_table_column表
	 *
	 * @param tableColumnList
	 */
	public static void saveTaskTableColumn (List<DBTableStruct> tableColumnList,
											TaskDatabaseSynchron taskDatabaseSynchron) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtil.getConn ("jtsec_com");
			conn.setAutoCommit (false);
			String sql = "INSERT into task_table_column (table_name,column_name,column_type,column_key,column_comment,"
					+ "db_name,db_ip,db_port,db_usr,db_pwd,data_length)"
					+ "values (?,?,?,?,?,?,?,?,?,?,?)";
			log.debug ("保存备份表sql" + sql);
			ps = conn.prepareStatement (sql);
			for (DBTableStruct dataSynchronousPoji : tableColumnList) {
				ps.setString (1, dataSynchronousPoji.getTableName ());
				ps.setString (2, dataSynchronousPoji.getColumnName ());
				ps.setString (3, dataSynchronousPoji.getColumnType ());
				ps.setString (4, dataSynchronousPoji.getColumnKey ());
				ps.setString (5, dataSynchronousPoji.getColumnComment ());
				ps.setString (6, dataSynchronousPoji.getTableSchema ());
				ps.setString (7, taskDatabaseSynchron.getHostIp ());
				ps.setString (8, taskDatabaseSynchron.getHostPort ());
				ps.setString (9, taskDatabaseSynchron.getHostUsername ());
				ps.setString (10, taskDatabaseSynchron.getHostPassword ());
				ps.setString (11, dataSynchronousPoji.getDataLength ());
				ps.addBatch ();
			}
			ps.executeBatch ();
			conn.commit ();
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DbUtil.closePs (ps);
			DbUtil.closeConn (conn);
		}
	}

	/**
	 * 查询数据库名称及字段名
	 *
	 * @param
	 * @param
	 * @return
	 */
	public static List<DBTableStruct> queryTaskTableColumn (String dataBaseName, Map<String, Object> map) {
		List<DBTableStruct> tableColumnList = new ArrayList<DBTableStruct> ();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String querySql = "SELECT\r\n" +
				"	db_name,\r\n" +
				"	table_name,\r\n" +
				"	column_comment,\r\n" +
				"	column_key,\r\n" +
				"	column_name,\r\n" +
				"	column_type,\r\n" +
				"	create_time\r\n" +
				"FROM\r\n" +
				"	task_table_column where 1=1 ";
		if (!StringUtil.isEmpty (dataBaseName))
			querySql += " and db_name = '" + dataBaseName + "' ";

		try {
			//创建连接
			conn = DbUtil.getConn ("jtsec_com");
			ps = conn.prepareStatement (querySql);
			rs = ps.executeQuery ();
			while (rs.next ()) {
				DBTableStruct dataSynchronousPoji = new DBTableStruct ();
				dataSynchronousPoji.setTableSchema (rs.getString ("db_name"));
				dataSynchronousPoji.setTableName (rs.getString ("table_name"));
				dataSynchronousPoji.setColumnComment (rs.getString ("column_comment"));
				dataSynchronousPoji.setColumnKey (rs.getString ("column_key"));
				dataSynchronousPoji.setColumnName (rs.getString ("column_name"));
				dataSynchronousPoji.setColumnType (rs.getString ("column_type"));
				tableColumnList.add (dataSynchronousPoji);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DbUtil.closeRs (rs);
			DbUtil.closePs (ps);
			DbUtil.closeConn (conn);
		}
		return tableColumnList;
	}

	/**
	 * 对比集合内容并保存到task_table_column表 返回差别内容
	 *
	 * @param tableColumnListBak
	 * @param tableColumnList
	 * @return
	 */
	public static List<Set<DBTableStruct>> compareList (List<DBTableStruct> tableColumnListBak,
														List<DBTableStruct> tableColumnList) {
		List<Set<DBTableStruct>> compareList = new ArrayList<Set<DBTableStruct>> ();
		//比较下集合中的内容
		if (!tableColumnListBak.isEmpty () && !tableColumnList.isEmpty ()) {
			Set<DBTableStruct> realCompareBakSet = new HashSet<DBTableStruct> ();
			Set<DBTableStruct> bakCompareRealSet = new HashSet<DBTableStruct> ();

			for (DBTableStruct dataSynchronousPojiBak : tableColumnListBak) {
				for (DBTableStruct dataSynchronousPoji : tableColumnList) {
					if (!dataSynchronousPojiBak.equals (dataSynchronousPoji)) {
						bakCompareRealSet.add (dataSynchronousPojiBak);
					} else {
						if (bakCompareRealSet.contains (dataSynchronousPojiBak)) {

							bakCompareRealSet.remove (dataSynchronousPojiBak);
							break;
						} else break;
					}
				}
			}
			for (DBTableStruct dataSynchronousPoji : tableColumnList) {
				for (DBTableStruct dataSynchronousPojiBak : tableColumnListBak) {
					if (!dataSynchronousPoji.equals (dataSynchronousPojiBak))

						realCompareBakSet.add (dataSynchronousPoji);
					else {
						if (realCompareBakSet.contains (dataSynchronousPoji)) {
							realCompareBakSet.remove (dataSynchronousPoji);
							break;
						} else break;
					}
				}
			}
			//同步数据
			compareList.add (realCompareBakSet);
			compareList.add (bakCompareRealSet);
		}
		return compareList;
	}

	/**
	 * 生成sql
	 *
	 * @param compareList
	 * @思路 我拿到两张表的对比之后 分别查询出不同元素放入到两个集合中 遍历集合查询对象对应在表中的数据是否相同 一共三个集合 存放添加数据 修改数据 删除的数据
	 * 开启任一模式之后 进行单个集合操作 生成配置文件以及日志记录
	 */
	public static void synchroOperate (List<Set<DBTableStruct>> compareList, TaskDatabaseSynchron taskDatabaseSynchron) {

		Set<DBTableStruct> realCompareBakSet = compareList.get (0);
		Set<DBTableStruct> bakCompareRealSet = compareList.get (1);

		Set<DBTableStruct> addSet = new HashSet<DBTableStruct> ();
		Set<DBTableStruct> delSet = new HashSet<DBTableStruct> ();
		Set<DBTableStruct> beforeEditSet = new HashSet<DBTableStruct> ();
		Set<DBTableStruct> afterEditSet = new HashSet<DBTableStruct> ();

		//添加字段 判断添加整张表 还是字段
		if (!realCompareBakSet.isEmpty () && bakCompareRealSet.isEmpty ()) {
			addSet.addAll (realCompareBakSet);
			generateLog (realCompareBakSet, null, taskDatabaseSynchron);
		} else if (realCompareBakSet.isEmpty () && !bakCompareRealSet.isEmpty ()) {
			delSet.addAll (bakCompareRealSet);
			generateLog (null, bakCompareRealSet, taskDatabaseSynchron);
		} else if (!realCompareBakSet.isEmpty () && !bakCompareRealSet.isEmpty ()) {
			addSet.addAll (realCompareBakSet);
			delSet.addAll (bakCompareRealSet);
			generateLog (realCompareBakSet, bakCompareRealSet, taskDatabaseSynchron);
		/*	contrastAndValuation(realCompareBakSet, addSet, afterEditSet,  taskDatabaseSynchron);
			contrastAndValuation(bakCompareRealSet, delSet, beforeEditSet, taskDatabaseSynchron);*/
		}
		//生成配置文件和日志
		generateSqlFile (addSet, afterEditSet, beforeEditSet, delSet, realCompareBakSet, bakCompareRealSet, taskDatabaseSynchron);
	}
	/**
	 * 集合对比和比较
	 * @param compareSet
	 * @param beCompareSet
	 * @param delSet
	 * @param editSet
	 * @param addSet
	 * @param taskDatabaseSynchron
	 */
/*	private static void contrastAndValuation(Set<DBTableStruct>   compareSet,
											 Set<DBTableStruct>   addOrDelSet,
											 Set<DBTableStruct>   editSet,
											 TaskDatabaseSynchron taskDatabaseSynchron) {

		Iterator<DBTableStruct> it = compareSet.iterator();

		while (it.hasNext()) {
			DBTableStruct dBTableStruct = it.next();
			try {
				 getForTheColumn(taskDatabaseSynchron, dBTableStruct);
			}
			catch(Exception ex) {
				it.remove();
		    	editSet.add(dBTableStruct);
			}
		}
		addOrDelSet.addAll(compareSet);
	}*/

	/**
	 * 获取整列的值
	 * @param taskDatabaseSynchron
	 * @param realDBTableStruct
	 * @throws SQLException
	 */
/*	private static  List<String> getForTheColumn(TaskDatabaseSynchron taskDatabaseSynchron, DBTableStruct realDBTableStruct) throws SQLException {


		List<String> columnValueList = new ArrayList<String>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//创建连接
			conn = DatabaseSynchronUtil.getConnection(taskDatabaseSynchron.getHostIp(),
													  taskDatabaseSynchron.getHostPort(),
													  taskDatabaseSynchron.getHostUsername(),
												      taskDatabaseSynchron.getHostPassword(),
													  taskDatabaseSynchron.getHostDatabaseName(),
													  String.valueOf(taskDatabaseSynchron.getHostDatabaseType()));

			String sql = "select "+realDBTableStruct.getColumnName()+" from "+realDBTableStruct.getTableName()+" ";

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				int index = 1;
				columnValueList.add(rs.getString(index++));
			}
		}
		finally{
			DbUtil.closeRs(rs);
			DbUtil.closePs(ps);
			DbUtil.closeConn(conn);
		}
		return columnValueList;
	}*/

	/**
	 * 生成日志
	 *
	 * @param realCompareBakSet
	 * @param bakCompareRealSet
	 * @param taskDatabaseSynchron
	 */
	private static void generateLog (Set<DBTableStruct> realCompareBakSet, Set<DBTableStruct> bakCompareRealSet,
									 TaskDatabaseSynchron taskDatabaseSynchron) {
		//因为没有办法判断修改的情况所以  目前只能记录两个表的对比变化 等以后再进行优化
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd hh:ss:mm");

		String createTime = format.format (System.currentTimeMillis ());


		if (realCompareBakSet != null) {
			for (DBTableStruct dBTableStruct : realCompareBakSet) {
				setLogProperties (taskDatabaseSynchron, createTime, dBTableStruct, "1");
			}
		}
		if (bakCompareRealSet != null) {
			for (DBTableStruct dBTableStruct : bakCompareRealSet) {
				setLogProperties (taskDatabaseSynchron, createTime, dBTableStruct, "2");
			}
		}
	}

	/**
	 * 保存日志
	 *
	 * @param taskDatabaseSynchron
	 * @param createTime
	 * @param dBTableStruct
	 * @param operate
	 */
	private static void setLogProperties (TaskDatabaseSynchron taskDatabaseSynchron, String createTime,
										  DBTableStruct dBTableStruct, String operate) {
		TaskTableColumnLog task = new TaskTableColumnLog ();
		task.setDyncTime (createTime);
		task.setHostIp (taskDatabaseSynchron.getHostIp ());
		task.setHostPort (taskDatabaseSynchron.getHostPort ());
		task.setSlaveIp (taskDatabaseSynchron.getSlaveIp ());
		task.setSlavePort (taskDatabaseSynchron.getSlavePort ());
		task.setDyncDatabaseName (taskDatabaseSynchron.getHostDatabaseName ());
		task.setDyncTableName (dBTableStruct.getTableName ());
		task.setDyncColumnName (dBTableStruct.getColumnName ());
		task.setDyncColumnType (dBTableStruct.getColumnType ());
		task.setDyncColumnComment (dBTableStruct.getColumnComment ());
		task.setDyncConstraint (dBTableStruct.getColumnKey ());
		task.setDyncDataLength (dBTableStruct.getDataLength ());

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DbUtil.getConn ("jtsec_com");
			conn.setAutoCommit (false);
			String sql = "INSERT INTO task_table_column_log (" + task.getColumn () + ")VALUES(now(),?,?,?,?,?,?,?,?,?,?,?,?)";
			log.debug ("保存日志执行的sql" + sql);
			ps = conn.prepareStatement (sql);

			ps.setString (1, task.getDyncDatabaseName ());
			ps.setString (2, task.getDyncTableName ());
			ps.setString (3, task.getDyncColumnName ());
			ps.setString (4, task.getHostIp ());
			ps.setString (5, task.getHostPort ());
			ps.setString (6, task.getSlaveIp ());
			ps.setString (7, task.getSlavePort ());
			ps.setString (8, operate);
			ps.setString (9, task.getDyncColumnComment ());
			ps.setString (10, task.getDyncColumnType ());
			ps.setString (11, task.getDyncConstraint ());
			ps.setString (12, task.getDyncDataLength ());

			ps.addBatch ();
			ps.executeBatch ();
			conn.commit ();
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DbUtil.closePs (ps);
			DbUtil.closeConn (conn);
		}
	}

	/**
	 * //生成配置文件和日志
	 *
	 * @param addSet
	 * @param delSet
	 * @param taskDatabaseSynchron
	 * @param bakCompareRealSet
	 * @param realCompareBakSet
	 */
	private static void generateSqlFile (Set<DBTableStruct> addSet, Set<DBTableStruct> afterEditSet,
										 Set<DBTableStruct> beforeEditSet, Set<DBTableStruct> delSet,
										 Set<DBTableStruct> realCompareBakSet, Set<DBTableStruct> bakCompareRealSet,
										 TaskDatabaseSynchron taskDatabaseSynchron) {
		//查看数据生成策略
		String strategy = taskDatabaseSynchron.getSynchronisationStrategy ();
		//生成策略为添加和删除
		if (strategy.length () > 1) {
			List<String> jsonStrList = generateJson (addSet, delSet, realCompareBakSet, bakCompareRealSet, taskDatabaseSynchron);

			if (!jsonStrList.isEmpty ())
				SyncConfigFile.sync2File (jsonStrList, taskDatabaseSynchron);
		} else if (strategy.length () == 1) {
			//生成策略为添加
			if ("1".equals (strategy)) {
				List<String> jsonStrList = generateJson (addSet, null, realCompareBakSet, bakCompareRealSet, taskDatabaseSynchron);

				if (!jsonStrList.isEmpty ())
					SyncConfigFile.sync2File (jsonStrList, taskDatabaseSynchron);
			}
			//生成策略为删除
			else if ("2".equals (strategy)) {
				List<String> jsonStrList = generateJson (null, delSet, realCompareBakSet, bakCompareRealSet, taskDatabaseSynchron);

				if (!jsonStrList.isEmpty ())
					SyncConfigFile.sync2File (jsonStrList, taskDatabaseSynchron);
			}
		}
		//修改集合
	}

	/**
	 * @param addSet
	 * @param delSet
	 * @param realCompareBakSet
	 * @param bakCompareRealSet
	 * @param taskDatabaseSynchron
	 * @return
	 */
	private static List<String> generateJson (Set<DBTableStruct> addSet, Set<DBTableStruct> delSet,
											  Set<DBTableStruct> realCompareBakSet, Set<DBTableStruct> bakCompareRealSet,
											  TaskDatabaseSynchron taskDatabaseSynchron) {
		List<String> JsonStrList = new ArrayList<String> ();
		//添加集合
		if (addSet != null && !addSet.isEmpty ()) {

			Set<String> editTableSet = new HashSet<String> ();  //
			Set<String> creatOrDelTableSet = new HashSet<String> (); //需要创建的表名集合

			voluationSet (editTableSet, creatOrDelTableSet, addSet, "add", null);

			JsonStrList = generateCreateSql (editTableSet, creatOrDelTableSet, realCompareBakSet, taskDatabaseSynchron, JsonStrList);
		}
		//删除集合
		if (delSet != null && !delSet.isEmpty ()) {
			Set<String> editTableSet = new HashSet<String> ();  //
			Set<String> creatOrDelTableSet = new HashSet<String> (); //需要创建的表名集合

			voluationSet (editTableSet, creatOrDelTableSet, delSet, "del", taskDatabaseSynchron);

			JsonStrList = generateDelSql (editTableSet, creatOrDelTableSet, bakCompareRealSet, taskDatabaseSynchron, JsonStrList);
		}
		return JsonStrList;
	}

	/**
	 * 给两个集合赋值
	 *
	 * @param editTableNameSet
	 * @param creatOrDelTableNameSet
	 * @param taskDatabaseSynchron
	 * @param operate
	 * @param operateSet
	 */
	private static void voluationSet (Set<String> editTableNameSet, Set<String> creatOrDelTableNameSet,
									  Set<DBTableStruct> operateSet, String operate,
									  TaskDatabaseSynchron taskDatabaseSynchron) {

		for (DBTableStruct dataSynchronousPoji : operateSet) {
			editTableNameSet.add (dataSynchronousPoji.getTableName ());
		}
		//过滤掉表 集合中只剩下需要创建的表
		Iterator<String> it = editTableNameSet.iterator ();
		while (it.hasNext ()) {
			Map<String, Object> map = new HashMap<String, Object> ();
			String element = it.next ();
			map.put ("tableName", element);
			if ("add".equals (operate)) {
				if (getBakTableColumnCount (null, map) == 0) {
					creatOrDelTableNameSet.add (element);
					it.remove ();
				}
			} else if ("del".equals (operate)) {
				if (getRealTimeTableColumnCount (map, taskDatabaseSynchron) == 0) {
					creatOrDelTableNameSet.add (element);
					it.remove ();
				}
			}
		}
	}

	private static int getRealTimeTableColumnCount (Map<String, Object> map, TaskDatabaseSynchron taskDatabaseSynchron) {
		Connection conn;
		int total = 0;
		try {
			conn = DatabaseSynchronUtil.getConnection (taskDatabaseSynchron.getHostIp (),
					taskDatabaseSynchron.getHostPort (),
					taskDatabaseSynchron.getHostUsername (),
					taskDatabaseSynchron.getHostPassword (),
					taskDatabaseSynchron.getHostDatabaseName (),
					String.valueOf (taskDatabaseSynchron.getHostDatabaseType ()));
			String sqlCount = null;
			if (taskDatabaseSynchron.getHostDatabaseType () == 1)
				sqlCount = "select " + MysqlSystemSql.COUNT + " from " + MysqlSystemSql.FROM + " where 1=1";
			else if (taskDatabaseSynchron.getHostDatabaseType () == 2)
				sqlCount = "select " + OracleSystemSql.COUNT + " from " + OracleSystemSql.FROM + " where 1=1";
			else if (taskDatabaseSynchron.getHostDatabaseType () == 3)
				sqlCount = "select " + SqlServerSystemSql.COUNT + " from " + SqlServerSystemSql.FROM + " where 1=1";

			if (map != null) {
				if (NotNullUtil.stringNotNull (String.valueOf (map.get ("tableName"))))
					if (taskDatabaseSynchron.getHostDatabaseType () == 1)
						sqlCount += " and `tb`.`TABLE_NAME` ='" + map.get ("tableName") + "';";
					else if (taskDatabaseSynchron.getHostDatabaseType () == 2)
						sqlCount += " and  T.table_name ='" + map.get ("tableName") + "';";
					else if (taskDatabaseSynchron.getHostDatabaseType () == 3)
						sqlCount += " and  c.table_name  ='" + map.get ("tableName") + "';";
			}
			total = DbUtil.getTotal (conn, sqlCount);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return total;
	}

	/**
	 * 生成删除表和删除字段的sql
	 *
	 * @param bakCompareRealSet
	 * @return
	 */
	private static List<String> generateDelSql (Set<String> delNameSet, Set<String> dropTableSet,
												Set<DBTableStruct> bakCompareRealSet, TaskDatabaseSynchron taskDatabaseSynchron,
												List<String> JsonStrList) {

		if (!delNameSet.isEmpty () && dropTableSet.isEmpty ())
			JsonStrList = generateDel (delNameSet, bakCompareRealSet, taskDatabaseSynchron, JsonStrList);

		else if (delNameSet.isEmpty () && !dropTableSet.isEmpty ())
			JsonStrList = generateDrop (dropTableSet, bakCompareRealSet, taskDatabaseSynchron, JsonStrList);

		else if (!delNameSet.isEmpty () && !dropTableSet.isEmpty ()) {
			JsonStrList = generateDel (delNameSet, bakCompareRealSet, taskDatabaseSynchron, JsonStrList);
			JsonStrList = generateDrop (dropTableSet, bakCompareRealSet, taskDatabaseSynchron, JsonStrList);
		}
		return JsonStrList;

	}

	/**
	 * 生成删除字段sql
	 *
	 * @param alertTableSet
	 * @param bakCompareRealSet
	 * @return
	 */
	private static List<String> generateDel (Set<String> alertTableSet, Set<DBTableStruct> bakCompareRealSet,
											 TaskDatabaseSynchron taskDatabaseSynchron, List<String> JsonStrList) {
		for (String tableName : alertTableSet) {

			StringBuffer sql = DateBaseCommonSql.generateDropColumnName (tableName, taskDatabaseSynchron);

			DBTransportMessage dBTransportMessage = new DBTransportMessage ();
			List<SyncConfigDto> syncConfigDtoList = new ArrayList<SyncConfigDto> ();

			for (DBTableStruct dataSynchronousPoji : bakCompareRealSet) {
				if (dataSynchronousPoji.getTableName ().equals (tableName)) {

					SyncConfigDto syncConfigDto = SyncConfigFile.converEntity (dataSynchronousPoji, taskDatabaseSynchron, "drop column");

					DateBaseCommonSql.delColumn (dataSynchronousPoji, taskDatabaseSynchron, sql);

					syncConfigDtoList.add (syncConfigDto);
				}
			}
			sql.deleteCharAt (sql.length () - 1);
			if (taskDatabaseSynchron.getSlaveDatabaseType () == 2) sql.append (")");

			dBTransportMessage.setSql (sql.toString ());
			dBTransportMessage.setList (syncConfigDtoList);
			StringBuffer str = SyncConfigFile.syncConfig (dBTransportMessage, taskDatabaseSynchron);
			JsonStrList.add (str.toString ());
		}
		return JsonStrList;
	}

	/**
	 * 生成删除表sql
	 *
	 * @param tableNameSet
	 * @return
	 */
	private static List<String> generateDrop (Set<String> tableNameSet, Set<DBTableStruct> bakCompareRealSet,
											  TaskDatabaseSynchron taskDatabaseSynchron, List<String> jsonStrList) {
		for (String tableName : tableNameSet) {

			DBTransportMessage dBTransportMessage = new DBTransportMessage ();

			StringBuffer sql = null;

			for (DBTableStruct dataSynchronousPoji : bakCompareRealSet) {
				if (dataSynchronousPoji.getTableName ().equals (tableName))

					sql = DateBaseCommonSql.drop (tableName, taskDatabaseSynchron);
			}
			dBTransportMessage.setSql (sql.toString ());
			StringBuffer str = SyncConfigFile.syncConfig (dBTransportMessage, taskDatabaseSynchron);
			jsonStrList.add (str.toString ());
		}
		return jsonStrList;
	}

	/**
	 * 生成创建表和添加字段的sql
	 *
	 * @param realCompareBakSet
	 * @return
	 */
	private static List<String> generateCreateSql (Set<String> addNameSet, Set<String> createTableSet,
												   Set<DBTableStruct> realCompareBakSet, TaskDatabaseSynchron taskDatabaseSynchron,
												   List<String> JsonStrList) {

		if (!addNameSet.isEmpty () && createTableSet.isEmpty ())
			JsonStrList = generateAdd (addNameSet, realCompareBakSet, taskDatabaseSynchron, JsonStrList);

		else if (addNameSet.isEmpty () && !createTableSet.isEmpty ())
			JsonStrList = generateCreate (createTableSet, realCompareBakSet, taskDatabaseSynchron, JsonStrList);

		else if (!addNameSet.isEmpty () && !createTableSet.isEmpty ()) {
			JsonStrList = generateAdd (addNameSet, realCompareBakSet, taskDatabaseSynchron, JsonStrList);
			JsonStrList = generateCreate (createTableSet, realCompareBakSet, taskDatabaseSynchron, JsonStrList);
		}
		return JsonStrList;
	}

	/**
	 * 生成添加字段sql
	 *
	 * @param alertTableSet
	 * @param realCompareBakSet
	 */
	private static List<String> generateAdd (Set<String> alertTableSet, Set<DBTableStruct> realCompareBakSet,
											 TaskDatabaseSynchron taskDatabaseSynchron, List<String> JsonStrList) {
		for (String tableName : alertTableSet) {

			StringBuffer sql = DateBaseCommonSql.generateAlertTableName (tableName, taskDatabaseSynchron);
			DBTransportMessage dBTransportMessage = new DBTransportMessage ();
			List<SyncConfigDto> syncConfigDtoList = new ArrayList<SyncConfigDto> ();

			for (DBTableStruct dataSynchronousPoji : realCompareBakSet) {
				if (dataSynchronousPoji.getTableName ().equals (tableName)) {

					SyncConfigDto syncConfigDto = SyncConfigFile.converEntity (dataSynchronousPoji, taskDatabaseSynchron, "alter table");

					DateBaseCommonSql.generateAddSql (dataSynchronousPoji, taskDatabaseSynchron, sql);

					syncConfigDtoList.add (syncConfigDto);
				}
			}
			sql.deleteCharAt (sql.length () - 1);
			if (taskDatabaseSynchron.getSlaveDatabaseType () == 2)
				sql.append (")");

			dBTransportMessage.setSql (sql.toString ());
			dBTransportMessage.setList (syncConfigDtoList);
			StringBuffer str = SyncConfigFile.syncConfig (dBTransportMessage, taskDatabaseSynchron);
			JsonStrList.add (str.toString ());
		}
		return JsonStrList;
	}

	/**
	 * 生成创建表sql
	 *
	 * @param tableNameSet
	 * @param realCompareBakSet
	 * @return
	 */
	private static List<String> generateCreate (Set<String> tableNameSet, Set<DBTableStruct> realCompareBakSet,
												TaskDatabaseSynchron taskDatabaseSynchron, List<String> JsonStrList) {
		for (String tableName : tableNameSet) {

			DBTransportMessage dBTransportMessage = new DBTransportMessage ();
			ArrayList<SyncConfigDto> syncConfigDtoList = new ArrayList<SyncConfigDto> ();

			StringBuffer sql = new StringBuffer ("create table ");

			if (taskDatabaseSynchron.getSlaveDatabaseType () == 1 || taskDatabaseSynchron.getSlaveDatabaseType () == 3)
				sql.append (tableName + "(");
			else if (taskDatabaseSynchron.getSlaveDatabaseType () == 2)
				sql.append (taskDatabaseSynchron.getHostUsername ().toUpperCase () + "." + tableName + "(");

			for (DBTableStruct dataSynchronousPoji : realCompareBakSet) {
				if (dataSynchronousPoji.getTableName ().equals (tableName)) {

					SyncConfigDto syncConfigDto = SyncConfigFile.converEntity (dataSynchronousPoji, taskDatabaseSynchron, "create table");

					DateBaseCommonSql.generateCreateSql (dataSynchronousPoji, taskDatabaseSynchron, sql);

					syncConfigDtoList.add (syncConfigDto);
				}
			}
			sql.deleteCharAt (sql.length () - 1);
			if (taskDatabaseSynchron.getSlaveDatabaseType () == 1)
				sql.append (") ENGINE=InnoDB DEFAULT CHARSET=utf8");
			else if (taskDatabaseSynchron.getSlaveDatabaseType () == 2 || taskDatabaseSynchron.getSlaveDatabaseType () == 3)
				sql.append (")");
			dBTransportMessage.setSql (sql.toString ());
			dBTransportMessage.setList (syncConfigDtoList);
			StringBuffer str = SyncConfigFile.syncConfig (dBTransportMessage, taskDatabaseSynchron);
			JsonStrList.add (str.toString ());
		}
		return JsonStrList;
	}

	/**
	 * 获取数据库对应表中数据调数
	 *
	 * @param map
	 * @return
	 */
	public static int getTableColumnCount (Map<String, Object> map) {
		Connection conn;
		int total = 0;
		try {
			conn = DbUtil.getConn ("jtsec_com");
			String sqlCount = "SELECT\r\n" +
					"	count(*)\r\n" +
					"FROM\r\n" +
					"	`information_schema`.`tables` `tb`\r\n" +
					"LEFT JOIN `information_schema`.`columns` `col` ON `tb`.`TABLE_NAME` = `col`.`TABLE_NAME`\r\n" +
					"AND `tb`.`TABLE_SCHEMA` = `col`.`TABLE_SCHEMA`";
			if (map != null)
				sqlCount += " where 1=1";
			if (NotNullUtil.stringNotNull (String.valueOf (map.get ("databaseName"))))
				sqlCount += " AND `tb`.`TABLE_SCHEMA` IN ('" + map.get ("databaseName") + "')";
			if (NotNullUtil.stringNotNull (String.valueOf (map.get ("tableName"))))
				sqlCount += " AND tb.TABLE_NAME ='" + map.get ("tableName") + "'";
			total = DbUtil.getTotal (conn, sqlCount);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return total;
	}

	/**
	 * 清空表内容
	 */
	public static void delTaskTableColumn (Set<String> dataBaseNameSet) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			for (String dataBaseName : dataBaseNameSet) {
				conn = DbUtil.getConn ("jtsec_com");

				String sql = "delete from task_table_column where 1=1 ";
				if (!StringUtil.isEmpty (dataBaseName))
					sql += " and db_name  = '" + dataBaseName + "'";
				ps = conn.prepareStatement (sql);
				ps.execute ();
			}

		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DbUtil.closePs (ps);
			DbUtil.closeConn (conn);
		}
	}

	public static File loadDir () {

		String pathKey = CacheKeyCons.DB_STRUCT_SYNC_READ_PATH;
		String path = SyslogParserModuleConfig.getValue (pathKey);
		File file = new File (path);

		return file;
	}

	/**
	 * 解析执行文件
	 *
	 * @param file
	 */
	public static void ParseAndExecute (File file) {

		if (file != null) {
			String jsonSql = parseFile (file);
			log.debug ("解析生成的json：" + jsonSql);
			List<DBTransportMessage> dBTransportMessageList = JSON.parseArray (jsonSql, DBTransportMessage.class);

			Boolean result = executeFile (dBTransportMessageList);

			if (result) {
				log.debug ("解析并执行文件：" + file.getName () + "成功");
				file.delete ();
			} else
				log.debug ("解析并执行文件：" + file.getName () + "失败");
		}
	}

	/**
	 * 执行文件
	 *
	 * @param dBTransportMessageList
	 * @return
	 */
	private static Boolean executeFile (List<DBTransportMessage> dBTransportMessageList) {
		Boolean result = true;
		if (!dBTransportMessageList.isEmpty ()) {
			for (DBTransportMessage dBTransportMessage : dBTransportMessageList) {

				Connection conn = null;
				PreparedStatement ps = null;
				try {
					conn = DatabaseSynchronUtil.getConnection (dBTransportMessage.getUrl (), dBTransportMessage.getPort (),
							dBTransportMessage.getUserName (), dBTransportMessage.getPassword (),
							dBTransportMessage.getDatabaseName (), String.valueOf (dBTransportMessage.getDatabasetype ()));

					log.debug (dBTransportMessage.getSql ());
					ps = conn.prepareStatement (dBTransportMessage.getSql ());
					ps.executeUpdate ();
					result = true;
				} catch (Exception e) {
					result = false;
					e.printStackTrace ();
				} finally {
					DbUtil.closeConn (conn);
					DbUtil.closePs (ps);
				}
			}
		}
		return result;
	}

	/**
	 * 解析文件
	 *
	 * @param file
	 * @return
	 */
	private static String parseFile (File file) {
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		String jsonSql = "";
		try {
			fileInputStream = new FileInputStream (file);
			inputStreamReader = new InputStreamReader (fileInputStream, "UTF-8");
			reader = new BufferedReader (inputStreamReader);
			String str = null;
			while ((str = reader.readLine ()) != null) {
				jsonSql += str;
			}
		} catch (IOException e) {
			e.printStackTrace ();
		} finally {
			if (reader != null) {
				try {
					reader.close ();
				} catch (IOException e) {
					e.printStackTrace ();
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close ();
				} catch (IOException e) {
					e.printStackTrace ();
				}
			}
			if (fileInputStream != null) {
				try {
					fileInputStream.close ();
				} catch (IOException e) {
					e.printStackTrace ();
				}
			}
		}
		return jsonSql;
	}

	public static Set<String> getDelDataBaseName (List<DBTableStruct> dataSynchronousPojiList) {
		Set<String> delDataBaseNameSet = new HashSet<String> ();

		for (DBTableStruct dBTableStruct : dataSynchronousPojiList) {
			delDataBaseNameSet.add (dBTableStruct.getTableSchema ());
		}

		return delDataBaseNameSet;
	}
}