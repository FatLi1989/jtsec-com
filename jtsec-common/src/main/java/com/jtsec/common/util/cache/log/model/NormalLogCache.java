package com.jtsec.common.util.cache.log.model;

import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.cache.log.util.SyslogParserModuleConfig;
import com.jtsec.common.util.cache.util.Blocker;
import java.util.HashMap;
import java.util.List;
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
public class NormalLogCache {
	
	public final static String SQL_CACHE =  "sqlCache";
	public final static String JSON_CACHE =  "jsonCache";
	public final static String STRING_CACHE =  "stringCache";
	
	private static NormalLogCache instance = new NormalLogCache();

	private Map<String, Blocker> blockerMap = null;
	
	// 存储SQL格式的日志缓存队列
	private ConcurrentLinkedQueue<String> sqlCache;
	// 存储JSON格式的日志缓存队列
	private ConcurrentLinkedQueue<String> jsonCache;
	// 存储字符串格式的日志缓存队列
	private ConcurrentLinkedQueue<String> stringCache;
	
	private NormalLogCache(){
		this.blockerMap = new HashMap<String, Blocker>();
		this.sqlCache = new ConcurrentLinkedQueue<String>();
		this.jsonCache = new ConcurrentLinkedQueue<String>();
		this.stringCache = new ConcurrentLinkedQueue<String>();
		this.blockerMap.put(SQL_CACHE, new Blocker());
		this.blockerMap.put(JSON_CACHE, new Blocker());
		this.blockerMap.put(STRING_CACHE, new Blocker());
	}
	
	public static NormalLogCache getInstance(){
		return instance;
	}
	
	public Map<String, Blocker> getBlockerMap(){
		return this.blockerMap;
	}
	
	public ConcurrentLinkedQueue<String> getSqlCache() {
		return sqlCache;
	}	

	public ConcurrentLinkedQueue<String> getJsonCache() {
		return jsonCache;
	}	
	
	public ConcurrentLinkedQueue<String> getStringCache() {
		return stringCache;
	}	
	
	public int putSql(String sql){
		this.sqlCache.offer(sql);
		int size = sqlCache.size();
		if( size >= Integer.valueOf(SyslogParserModuleConfig.getValue(CacheKeyCons.SQL_LOG_WRITE_NUMBER)) ){
			Blocker blocker = this.blockerMap.get(SQL_CACHE);
			blocker.setIsWakeUp(true);
			blocker.prodAll();
		}
		return size;
	}
	
	public int putSql(List<String> sqlList){
		for(String sql : sqlList){
			this.putSql(sql);
		}
		return sqlCache.size();
	}
	
	public int getSqlCacheSize(){
		return this.sqlCache.size();
	}	
	
	public int putJson(String json){
		this.jsonCache.offer(json);
		int size = jsonCache.size();
		if( size >= Integer.valueOf(5) ){
			Blocker blocker = this.blockerMap.get(JSON_CACHE);
			blocker.setIsWakeUp(true);
			blocker.prodAll();
		}
		return size;
	}
	
	public int putJson(List<String> jsonList){
		for(String json : jsonList){
			this.putSql(json);
		}
		return jsonCache.size();
	}
	
	public int getJsonCacheSize(){
		return this.jsonCache.size();
	}
	
	public int putString(String str){
		this.stringCache.offer(str);
		int size = stringCache.size();
		if( size >= Integer.valueOf(5) ){
			Blocker blocker = this.blockerMap.get(STRING_CACHE);
			blocker.setIsWakeUp(true);
			blocker.prodAll();
		}
		return size;
	}
	
	public int putString(List<String> strList){
		for(String str : strList){
			this.putSql(str);
		}
		return stringCache.size();
	}
	
	public int getStringCacheSize(){
		return this.stringCache.size();
	}
	
	
}
