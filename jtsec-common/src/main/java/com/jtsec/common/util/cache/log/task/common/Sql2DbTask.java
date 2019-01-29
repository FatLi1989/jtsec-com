package com.jtsec.common.util.cache.log.task.common;

import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.cache.log.model.CommonCache;
import com.jtsec.common.util.cache.log.util.SyslogParserModuleConfig;
import com.jtsec.common.util.cache.util.Blocker;
import com.jtsec.common.util.surpass.DbUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Sql2DbTask extends Thread {

	private static final Log logger = LogFactory.getLog (Sql2DbTask.class);

	private Blocker blocker = null;

	private boolean runflag = false;

	private long keepAliveFlag = 0;
	private long activeTime = System.currentTimeMillis ();

	private final static int sleepTime = Integer.parseInt (SyslogParserModuleConfig.getValue (CacheKeyCons.SQL_WRITE_SLEEP));
	private final static int wakeUpWeight = Integer.parseInt (SyslogParserModuleConfig.getValue (CacheKeyCons.SQL_WRITE_WEIGHT));

	public Sql2DbTask () {
	}


	public void startThread () {
		runflag = true;
	}

	public void stopThread () {
		runflag = false;
		this.blocker.prodAll ();
	}

	public boolean getRunflag () {
		return runflag;
	}

	public long getKeepAliveFlag () {
		return keepAliveFlag;
	}

	public long getActiveTime () {
		return activeTime;
	}

	public void init () {
		this.blocker = CommonCache
				.getInstance ()
				.getBlockerMap ()
				.get (CommonCache.SQL_QUEUE);

		if (this.blocker == null) {
			this.blocker = new Blocker ();
			CommonCache.getInstance ().getBlockerMap ().put (CommonCache.SQL_QUEUE, this.blocker);
		}
	}

	@Override
	public void run () {
		if (logger.isInfoEnabled ()) {
			logger.info ("Sql2DbTask [" + this.hashCode () + "] is Start.");
		}
		while (getRunflag ()) {
			this.task ();
		}
		if (logger.isInfoEnabled ()) {
			logger.info ("Sql2DbTask [" + this.hashCode () + "][" + CommonCache.getInstance ().getSqlQueue () + "] is Stoped!");
		}
	}

	private void syncSyslog (ConcurrentLinkedQueue<String> queue) {
		Statement st = null;
		Connection conn = null;
		try {
			conn = DbUtil.getConn ("jtsec_log");
			if (conn == null) {
				return;
			}
			st = conn.createStatement ();
			int count = 0;
			while (!queue.isEmpty ()) {
				String sql = queue.poll ();
				if (sql != null) {
					st.addBatch (sql);
					count++;
					//若队列中数据不停在增加，每次最多累计处理500条，批量提交
					if (count == 500) {
						break;
					}
				}
			}
			if (count > 0) {
				if (logger.isDebugEnabled ()) {
					logger.debug ("Sql2DbTask [" + this.hashCode () + "] Sync [" + count + "] record into DB begin");
				}
				st.executeBatch ();
				if (logger.isDebugEnabled ()) {
					logger.debug ("Sql2DbTask [" + this.hashCode () + "] Sync [" + count + "] record into DB end");
				}
			}
		} catch (SQLException e) {
			if (logger.isWarnEnabled ()) {
				logger.warn ("Sql2DbTask [" + this.hashCode () + "] size [" + queue.size () + "] IOException ErrorCode [" + e.getErrorCode () + "] LocalMsg[" + e.getLocalizedMessage () + "] Msg[" + e.getMessage () + "] Cause[" + e.getCause () + "]");
			}
			e.printStackTrace ();
			DbUtil.rollbackConn (conn);
		} catch (Exception e) {
			if (logger.isWarnEnabled ()) {
				logger.warn ("Sql2DbTask [" + this.hashCode () + "] size [" + queue.size () + "] Exception LocalMsg[" + e.getLocalizedMessage () + "] Msg[" + e.getMessage () + "] Cause[" + e.getCause () + "]");
			}
			e.printStackTrace ();
			DbUtil.rollbackConn (conn);
		} finally {
			DbUtil.closeSt (st);
			DbUtil.closeConn (conn);
		}
		return;
	}

	private void task () {
		this.keepAliveFlag++;
		this.activeTime = System.currentTimeMillis ();
		//休眠一定时间，自动唤醒，处理日志缓存集合中每中日志类型的队列
		this.blocker.waitingCall (sleepTime * 1000);
		//队列处理权值增减控制
		/*if(logger.isDebugEnabled()){
			logger.debug("Sql2DbTask [" + this.hashCode() + "][" + CommonCache.getInstance().getQueueSyslogSqlSize() + "] is Running ... ");
		}*/
		//如果日志队列中的数据条数大于指定条目，isWakeUp就会被置为true，这样可以控制每次队列处理主线程唤醒时，如果队列中数据未达到指定条数，
		//不会频繁处理该类日志的队列，但是如果长时间日志队列中的数据未达到指定条数，也应该及时处理掉队列中的数据，所以为每一类日志增加权值，
		//当每次该类日志没有达到指定条数未被同步，则权值增加1，当达到指定权值后，不论队列中的数据是否达到指定条数，都进行队列数据的处理操作。
		if (this.blocker.getIsWakeUp () || this.blocker.getWakeUpWeight () >= wakeUpWeight) {
			//处理队列中的数据
			ConcurrentLinkedQueue<String> queue = CommonCache.getInstance ().getSqlQueue ();
			if (!queue.isEmpty ()) {
				long st = System.currentTimeMillis ();
				this.syncSyslog (queue);
				long et = System.currentTimeMillis ();
				if (logger.isInfoEnabled ()) {
					logger.info ("Sql2DbTask [" + this.hashCode () + "][" + CommonCache.getInstance ().getQueueSyslogSqlSize () + "] syncSyslogSql use time " + (et - st));
				}
			}
			//执行一次队列处理之后，若队列中的数据不存在未处理的，则唤醒标识位置为false，权值置为零
			if (queue.isEmpty ()) {
				this.blocker.setIsWakeUp (false);
				this.blocker.setWakeUpWeight (0);
			}
		} else {
			//增大权值
			int count = this.blocker.getWakeUpWeight () + 1;
			this.blocker.setWakeUpWeight (count);
		}
	}

}
