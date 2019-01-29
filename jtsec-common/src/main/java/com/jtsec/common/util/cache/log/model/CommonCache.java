package com.jtsec.common.util.cache.log.model;

import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.cache.log.util.SyslogParserModuleConfig;
import com.jtsec.common.util.cache.util.Blocker;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * SYSLOG日志数据缓存对象
 * 该对象用来对处理过的日志数据进行缓存，配合异步处理服务线程，
 * 在进行后续处理，提升日志处理上的性能和效率。
 * 
 * 该类为饿汉式单例实现
 * 
 * @author lioncode
 *
 */
public class CommonCache {
	
	private final static CommonCache instance = new CommonCache();
	
	private Map<String, Blocker> blockerMap = null;
	
	public final static String SQL_QUEUE =  "sqlQueue";
	
	// 存储SQL格式的日志缓存队列
	private ConcurrentLinkedQueue<String> sqlQueue;
	
	private CommonCache(){
		this.blockerMap = new HashMap<String, Blocker>();
		this.sqlQueue = new ConcurrentLinkedQueue<String>();
		this.blockerMap.put(SQL_QUEUE, new Blocker());
	}
	
	public static CommonCache getInstance(){
		return instance;
	}
	
	public Map<String, Blocker> getBlockerMap(){
		return this.blockerMap;
	}
	
	public ConcurrentLinkedQueue<String> getSqlQueue() {
		return sqlQueue;
	}
	
	public int putSql(String sql){
		this.sqlQueue.offer(sql);
		int size = sqlQueue.size();
		if( size >= Integer.valueOf(SyslogParserModuleConfig.getValue(CacheKeyCons.SQL_WRITE_NUMBER)) ){
			Blocker blocker = this.blockerMap.get(SQL_QUEUE);
			blocker.setIsWakeUp(true);
			blocker.prodAll();
		}
		return size;
	}
	
	public int getQueueSyslogSqlSize(){
		return this.sqlQueue.size();
	}	
}
