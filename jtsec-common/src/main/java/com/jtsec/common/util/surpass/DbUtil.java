package com.jtsec.common.util.surpass;

import com.alibaba.druid.pool.DruidDataSource;
import com.jtsec.common.Constants.KeyCons;
import com.jtsec.common.util.enums.DBEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1、获得连接数据库的对象，单例模式
 * 2、通过JDBC调用存储过程
 *
 * @author surpassE
 */
@Slf4j
@Data
@Component
@Configuration
public class DbUtil {
	public static final String DB_JTSEC_COM = "jtsec_com";
	public static final String DB_JTSEC_LOG = "jtsec_log";
	public static final String DB_JTSEC_VIDEO_LOG = "jtsec_video_log";

	public static Map<String, DruidDataSource> dsMap = new HashMap<String, DruidDataSource> ();

	/**
	 * 获得数据库的Connection连接对象，连接数据库的对象是单例的模式
	 *
	 * @return
	 * @throws Exception
	 */
	public static Connection getConn (String... param) throws Exception {
		Connection conn = null;
		DruidDataSource ds = dsMap.get (DBEnum.url.getCode ());
		if (ds == null) {
			ds = (DruidDataSource) getDataSource (DBEnum.driver.getCode (),
					DBEnum.url.getCode (),
					DBEnum.username.getCode (),
					DBEnum.password.getCode ());

			dsMap.put (DBEnum.url.getCode (), ds);
		}
		try {
			conn = ds.getConnection ();
		} catch (SQLException e) {
			e.printStackTrace ();
		}

		return conn;
//		return  getDataSource(driver, url, username, password).getConnection();
	}


	//	public static Connection getConn() throws Exception{
	//		Connection conn = null;
	//		Class.forName(PropertiesUtil.getValue(KeyCons.DB_DRIVER));
	//		if(conn == null){
	//			conn = DriverManager.getConnection(PropertiesUtil.getValue(KeyCons.DB_URL), PropertiesUtil.getValue(KeyCons.DB_USERNAME), PropertiesUtil.getValue(KeyCons.DB_PASSWORD));
	//		}
	//		return conn;
	//	}

	/**
	 * 关闭数据库连接
	 *
	 * @param conn
	 */
	public static void closeConn (Connection conn) {
		if (conn != null) {
			try {
				conn.close ();
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}

	/**
	 * 关闭ResultSet结果集
	 *
	 * @param rs
	 */
	public static void closeRs (ResultSet rs) {
		if (rs != null) {
			try {
				rs.close ();
			} catch (SQLException e) {
				e.printStackTrace ();
			}
		}
	}

	/**
	 * 关闭Statement
	 *
	 * @param
	 */
	public static void closeSt (Statement st) {
		if (st != null) {
			try {
				st.close ();
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}

	/**
	 * 关闭PreparedStatement
	 *
	 * @param ps
	 */
	public static void closePs (PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close ();
			} catch (SQLException e) {
				e.printStackTrace ();
			}
		}
	}

	/*
	 * 关闭数据库连接资源
	 * 不需要关闭的资源的参数，值设置为null
	 * @param conn
	 */
	public static void closeConn (ResultSet rs, Statement st, Connection conn) {
		closeRs (rs);
		closeSt (st);
		closeConn (conn);
	}


	/**
	 * 事物回滚操作
	 *
	 * @param conn
	 */
	public static void rollbackConn (Connection conn) {
		if (conn != null) {
			try {
				conn.rollback ();
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}


//	/**
//	 * 创建数据库连接池
//	 * @return
//	 */
//	@Deprecated
//	protected static DataSource getDataSource(String driver, String url, String username, String password) {
//		if (dataSource == null ) {  
//			dataSource = new DruidDataSource();
//			dataSource.setMaxActive(100);
//			dataSource.setMinIdle(20);
//			dataSource.setInitialSize(10);
//			dataSource.setPoolPreparedStatements(false);
//			dataSource.setValidationQuery("SELECT 1");
//			dataSource.setTestOnBorrow(false);
//			dataSource.setTestOnReturn(false);
//			dataSource.setTestWhileIdle(true);
//			dataSource.setMinEvictableIdleTimeMillis(30);
//			dataSource.setMaxWaitThreadCount(1000);
//			dataSource.setTimeBetweenEvictionRunsMillis(120000);
//			dataSource.setDriverClassName(driver);
//			dataSource.setUrl(url);
//			dataSource.setUsername(username);
//			dataSource.setPassword(password);
//		}
//		return dataSource;  
//	}

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
		// 配置这个属性的意义在于，如果存在多个数据源，监控的时候，可以通过名字来区分开来。如果没有配置，将会生成一个名字，
		// 格式是：”DataSource-” + System.identityHashCode(this)
//		ds.setName(name);
		// 连接数据库的url，不同数据库不一样。例如：
		// mysql : jdbc:mysql://10.20.153.104:3306/druid2 
		// oracle : jdbc:oracle:thin:@10.20.149.85:1521:ocnauto
		ds.setUrl (url);
		// 连接数据库的用户名
		ds.setUsername (username);
		// 连接数据库的密码。如果你不希望密码直接写在配置文件中，可以使用ConfigFilter。详细看这里：
		// https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter		
		ds.setPassword (password);
		// 缺省值：根据url自动识别
		// 这一项可配可不配，如果不配置druid会根据url自动识别dbType，然后选择相应的driverClassName
		ds.setDriverClassName (driver);
		// 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
		ds.setInitialSize (5);
		// 最大连接池数量
		ds.setMaxActive (15);
		// 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，
		// 如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
//		ds.setMaxWait(maxWaitMillis);
		// 最小连接池数量
		ds.setMinIdle (3);
		// 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
//		ds.setMaxWait(maxWaitMillis);
		// 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。
		// 在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。
		ds.setPoolPreparedStatements (false);
		ds.setMaxWaitThreadCount (1000);
		// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
		ds.setTestOnBorrow (false);
		// 归还连接时执行validationQuery检测连接是否有效， 做了这个配置会降低性能
		ds.setTestOnReturn (false);
		// 建议配置为true，不影响性能，并且保证安全性。
		// 申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
		ds.setTestWhileIdle (true);
		ds.setValidationQuery ("SELECT 1");
		// 有两个含义： 1) Destroy线程会检测连接的间隔时间  2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
		ds.setTimeBetweenEvictionRunsMillis (90000); // 60秒（120000）
		// Destory线程中如果检测到当前连接的最后活跃时间和当前时间的差值大于minEvictableIdleTimeMillis，则关闭当前连接。
		ds.setMinEvictableIdleTimeMillis (25200000); // 25200000毫秒，7小时
		// 对于建立时间超过removeAbandonedTimeout的连接强制关闭
		ds.setRemoveAbandoned (true);
		// 指定连接建立多长时间就需要被强制关闭
		ds.setRemoveAbandonedTimeout (1800);  //1800秒，30分钟
		// 指定发生removeabandoned的时候，是否记录当前线程的堆栈信息到日志中
		ds.setLogAbandoned (true);
		return ds;
	}

	/**
	 * 执行单条sql语句
	 *
	 * @param conn
	 * @param sql
	 */
	public static void executeSingleSql (Connection conn, String sql) {
		Statement st = null;
		try {
			conn.setAutoCommit (false);
			st = conn.createStatement ();
			st.execute (sql);
			conn.commit ();
		} catch (Exception e) {
			rollbackConn (conn);
			e.printStackTrace ();
		} finally {
			closeSt (st);
		}
	}


	/**
	 * 批量执行sql语句
	 *
	 * @param conn
	 * @param sqlList 包括insert、update、delete、create等sql语句
	 */
	public static void executeBetchSql (Connection conn, List<String> sqlList) {
		if (sqlList != null && sqlList.size () > 0) {
			try {
				if (conn == null) {
					conn = getConn ();
				}
				conn.setAutoCommit (false);
				Statement stat = conn.createStatement ();
				for (String sql : sqlList) {
					stat.addBatch (sql);
				}
				stat.executeBatch ();
				conn.commit ();
			} catch (Exception e) {
				rollbackConn (conn);
				e.printStackTrace ();
			} finally {
				DbUtil.closeConn (conn);
			}
		}
	}


	/**
	 * 调用无返回值的存储过程,对参数的类型的判断，当前只有Integer和String ，如有其他类型自行扩充
	 *
	 * @param procName 存储过程的名称，例如{call procName(?,?,?)}或{call procName()}
	 * @param param    参数集合，
	 * @throws Exception
	 */
	public static void call (String procName, List<Object> param) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = getConn ();
			ps = conn.prepareCall (procName);
			if (param != null && param.size () > 0) {
				for (int i = 1; i < param.size () + 1; i++) {
					Object obj = param.get (i - 1);
					if (obj instanceof Integer) {
						ps.setInt (i, (Integer) obj);
					} else if (obj instanceof String) {
						ps.setString (i, (String) obj);
					}
					//扩充参数类型
				}
			}
			ps.execute ();
		} finally {
			if (ps != null) {
				ps.close ();
			}
			closeConn (conn);
		}
	}

	/**
	 * 调用无返回值的存储过程,对参数的类型的判断，当前只有Integer和String ，如有其他类型自行扩充
	 *
	 * @param procName 存储过程的名称，例如{call procName(?,?,?)}或{call procName()}
	 * @param param    参数集合，
	 * @throws Exception
	 */
	public static void callOver (String procName, Object... param) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = getConn ();
			ps = conn.prepareCall (procName);
			if (param != null && param.length > 0) {
				for (int i = 1; i < param.length + 1; i++) {
					Object obj = param[i - 1];
					if (obj instanceof Integer) {
						ps.setInt (i, (Integer) obj);
					} else if (obj instanceof String) {
						ps.setString (i, (String) obj);
					}
					//扩充参数类型
				}
			}
			ps.execute ();
		} finally {
			if (ps != null) {
				ps.close ();
			}
			closeConn (conn);
		}
	}


	/**
	 * 调用无返回值的存储过程,对参数的类型的判断，当前只有Integer和String ，如有其他类型自行扩充
	 *
	 * @param 数据源
	 * @param procName 存储过程的名称，例如{call procName(?,?,?)}或{call procName()}
	 * @param param    参数集合，
	 * @throws Exception
	 */
	public static void call (String dbConnKey, String procName, Object... param) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = getConn (dbConnKey);
			ps = conn.prepareCall (procName);
			if (param != null && param.length > 0) {
				for (int i = 1; i < param.length + 1; i++) {
					Object obj = param[i - 1];
					if (obj instanceof Integer) {
						ps.setInt (i, (Integer) obj);
					} else if (obj instanceof String) {
						ps.setString (i, (String) obj);
					}
					//扩充参数类型
				}
			}
			ps.execute ();
		} finally {
			if (ps != null) {
				ps.close ();
			}
			closeConn (conn);
		}
	}

	public static void main (String[] args) {
		try {
//			DbUtil.call("jtsec_com", "{call proc_log_backup(?,?)}","log_ftp_msg",1492118705985);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace ();
		}
	}

	/**
	 * 调用无返回值的存储过程,对参数的类型的判断，当前只有Integer和String ，如有其他类型自行扩充
	 *
	 * @param 数据源
	 * @param procName 存储过程的名称，例如{call procName(?,?,?)}或{call procName()}
	 * @param param    参数集合，
	 * @throws Exception
	 */
	public static void call2 (String dbConnKey, int cutOutCount, String procName, Object... param) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = getConn (dbConnKey);
			ps = conn.prepareCall (procName);
			if (param != null && param.length > 0) {
				ps.setInt (1, cutOutCount);
				for (int i = 2; i < param.length + 2; i++) {
					Object obj = param[i - 2];
					if (obj instanceof Integer) {
						ps.setInt (i, (Integer) obj);
					} else if (obj instanceof String) {
						ps.setString (i, (String) obj);
					}
					//扩充参数类型
				}
			}
			ps.execute ();
		} finally {
			if (ps != null) {
				ps.close ();
			}
			closeConn (conn);
		}
	}


	/**
	 * 将arr、params中的值绑定到sql的参数中 params优先级高于arr
	 *
	 * @param sql
	 * @param arr
	 * @param params
	 * @return
	 */
	public static String bindSqlParam (String sql, String[] arr, String... params) {
		StringBuffer sb = new StringBuffer (sql);
		if (params != null && params.length > 0) {
			for (String param : params) {
				sb.append ("'").append (param).append ("',");
			}
		}
		if (arr != null && arr.length > 0) {
			for (String param : arr) {
				sb.append ("'").append (param).append ("',");
			}
		}
		if (sb.toString ().endsWith (",")) {
			//			sb.substring(0, sb.toString().length() - 1);
			sb = sb.delete (sb.toString ().length () - 1, sb.toString ().length ());
		}
		sb.append (")");
		return sb.toString ();
	}

	/**
	 * 替换sql中columu? 和 number?的参数
	 * eg:update table set name = 0?, age = 1?, pwd = 2?
	 * eg:insert table(aa,bb,cc) value(aa?, bb?, 0?);
	 * 执行替换参数操作，将0?/1?/2?用arr和params中的参数进行替换，arr的参数优先级高于params
	 * 参数一定0?1?数序排列
	 *
	 * @param sql
	 * @param map
	 * @param params
	 * @return
	 */
	public static String setSqlParams (String sql, Map<String, String> map, String... params) {
		StringBuffer sb = new StringBuffer ();
		if (map != null && map.keySet ().size () > 0) {
			for (String key : map.keySet ()) {
				sql.replaceAll (key + "?", "'" + map.get (key) + "'");
			}
		}
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				sql.replaceAll (i + "?", "'" + params[i] + "'");
			}
		}
		return sb.toString ();
	}

	/**
	 * 替换sql中number?的参数
	 * eg:update table set name = 0?, age = 1?, pwd = 2?
	 *
	 * @param sql
	 * @param list
	 * @return
	 */
	public static String setSqlParams (String sql, List<String> list) {
		StringBuffer sb = new StringBuffer ();
		if (list != null && list.size () > 0) {
			for (int i = 0; i < list.size (); i++) {
				sql.replaceAll (i + "?", "'" + list.get (i) + "'");
			}
		}
		return sb.toString ();
	}

	/**
	 * 替换sql中number?的参数
	 * eg:update table set name = 0?, age = 1?, pwd = 2?
	 *
	 * @param sql
	 * @param arr
	 * @return
	 */
	public static String setSqlParams (String sql, String[] arr) {
		StringBuffer sb = new StringBuffer ();
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				sql.replaceAll (i + "?", "'" + arr[i] + "'");
			}
		}
		return sb.toString ();
	}


	//	public static String replaceSqlParams(String sql, )

	/**
	 * 判断指定tableName在指定的MySQL数据库中是否存在
	 *
	 * @param conn      数据库连接
	 * @param tableName 要判断是否存在的表名
	 * @param params    table_schema 参数值-数据库名称
	 * @return
	 */
	public static boolean mysqlTableIsExist (Connection conn, String tableName, String... params) {
		String dbName = "jtsec_com";
		if (params != null && params.length > 0) {
			dbName = params[0];
		}
		String sql = "SELECT COUNT(TABLE_NAME) AS tab_cnt FROM `INFORMATION_SCHEMA`.`TABLES` WHERE `INFORMATION_SCHEMA`.`TABLES`.TABLE_SCHEMA = '" + dbName + "' AND `TABLE_NAME` = '" + tableName + "'";
		long total = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement (sql);
			ResultSet rs = ps.executeQuery ();
			if (rs.next ()) {
				total = rs.getLong (1);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			closePs (ps);
		}
		if (total > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 查询MySQL数据库中包含指定数据库表名称的所有数据库表名
	 *
	 * @param 数据库的标识，对应配置文件中的key
	 * @param tableName          要查询的表名称的关键字
	 * @param params             是否限定查询那个数据库
	 * @return
	 * @throws Exception
	 */
	public static List<String> mysqlFindTableName (Connection conn, String tableName, String... params) {
		String param = "jtsec_com";
		if (params != null && params.length > 0 && NotNullUtil.stringNotNull (params[0])) {
			param = params[0];
		}

		List<String> list = new ArrayList<String> ();
		String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + param + "' AND TABLE_NAME LIKE '%%" + tableName + "%%'  ORDER BY TABLE_NAME DESC ";
		log.debug ("查找表名的查询语句：" + sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement (sql);
			rs = ps.executeQuery ();
			while (rs.next ()) {
				String tName = rs.getString (1);
				list.add (tName);
			}
		} catch (SQLException e) {
			e.printStackTrace ();
		} finally {
			closeRs (rs);
			closePs (ps);
		}
		return list;
	}

	/**
	 * 统计指定表中的数据信息
	 *
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static int getTotal (Connection conn, String sql) {
		int total = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement (sql);
			rs = ps.executeQuery ();
			while (rs.next ()) {
				total = rs.getInt (1);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			closeRs (rs);
			closePs (ps);
		}
		return total;
	}

	/**
	 * 针对统计安全文件的插入前触发器
	 *
	 * @param tableName
	 * @param triggerSuffix
	 */
	public static void createHistoryTrigger (String dbKey, String tableName, String triggerSuffix) {
		Connection conn = null;
		try {
			if (dbKey != null && dbKey.length () > 0) {
				conn = getConn (dbKey);
			} else {
				conn = getConn ();
			}
			String sql = "CREATE TRIGGER t_before_insert_" + triggerSuffix + "\n" +
					"BEFORE INSERT ON " + tableName + "\n" +
					"FOR EACH ROW" + "\n" +
					"BEGIN" + "\n" +
					"SET @tmp = new.message;" + "\n" +
					"SET @trans1 = (select INSTR(@tmp,'Received file:') from dual);" + "\n" +
					"SET @trans2 = (select INSTR(@tmp, 'Sent file') from dual);" + "\n" +
					"SET @trans3 = (select INSTR(@tmp,'successfully.') from dual);" + "\n" +
					"IF((@trans1  > 0 or @trans2 > 0) and @trans3 > 0)" + "\n" +
					"THEN" + "\n" +
					"INSERT INTO " + tableName + "_history(ID, ReceivedAt, FromHostIP, SyslogTag, Message) VALUES(new.id,new.ReceivedAt,new.FromHostIP,new.SyslogTag,new.Message);" + "\n" +
					"END IF;" + "\n" +
					"END;";
			PreparedStatement ps = conn.prepareStatement (sql);
			ps.execute ();
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			DbUtil.closeConn (conn);
		}
	}

	/**
	 * 统计指定表中的数据信息
	 *
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static int getTotalAndCloseConnn (Connection conn, String sql) {
		int total = 0;
		try {
			if (conn == null) {
				conn = getConn ();
			}
			PreparedStatement ps = conn.prepareStatement (sql);
			ResultSet rs = ps.executeQuery (sql);
			while (rs.next ()) {
				total = rs.getInt (1);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			closeConn (conn);
		}
		return total;
	}

	/**
	 * 判断指定tableName在指定的数据库中是否存在
	 *
	 * @param conn      数据库连接
	 * @param tableName 要判断是否存在的表名
	 * @param params    table_schema 参数值-数据库名称
	 * @return
	 */
	public static boolean tableIsExist (Connection conn, String tableName, String... params) {
		String dbName = "jtsec_com";
		if (params != null && params.length > 0) {
			dbName = params[0];
		}
		String sql = "SELECT COUNT(TABLE_NAME) AS tab_cnt FROM `INFORMATION_SCHEMA`.`TABLES` WHERE `INFORMATION_SCHEMA`.`TABLES`.TABLE_SCHEMA = '" + dbName + "' AND `TABLE_NAME` = '" + tableName + "'";
		long total = 0;
		try {
			PreparedStatement ps = conn.prepareStatement (sql);
			ResultSet rs = ps.executeQuery ();
			if (rs.next ()) {
				total = rs.getLong (1);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		} finally {
			closeConn (conn);
		}
		if (total > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 查询数据库中包含指定数据库表名称的所有数据库表名
	 *
	 * @param dbKey     数据库的标识，对应配置文件中的key
	 * @param tableName 要查询的表名称的关键字
	 * @param params    是否限定查询那个数据库
	 * @return
	 * @throws Exception
	 */
	public static List<String> findTableName (String dbKey, String tableName, String... params) throws Exception {
		String param = "jtsec_com";
		if (params != null && params.length > 0 && NotNullUtil.stringNotNull (params[0])) {
			param = params[0];
		}
		List<String> list = new ArrayList<String> ();
		String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='" + param + "' AND TABLE_NAME LIKE '%%" + tableName + "%%'  ORDER BY TABLE_NAME DESC ";
		log.debug ("查找表名的查询语句：" + sql);
		//获得对应数据源
		Connection conn = DbUtil.getConn (dbKey);
		try {
			PreparedStatement ps = conn.prepareStatement (sql);
			ResultSet rs = ps.executeQuery ();
			while (rs.next ()) {
				String tName = rs.getString (1);
				list.add (tName);
			}
			rs.close ();
		} finally {
			DbUtil.closeConn (conn);
		}
		return list;
	}

}
