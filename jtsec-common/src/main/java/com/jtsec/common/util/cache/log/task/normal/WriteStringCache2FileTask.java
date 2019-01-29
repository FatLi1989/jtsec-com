package com.jtsec.common.util.cache.log.task.normal;

import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.cache.log.model.NormalLogCache;
import com.jtsec.common.util.cache.log.util.CacheUtil;
import com.jtsec.common.util.cache.log.util.SyslogParserModuleConfig;
import com.jtsec.common.util.cache.util.Blocker;
import com.jtsec.common.util.surpass.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 将STRING格式日志缓存中的数据写入到文件
 *
 * @author 吴晗
 * 2018-3-30
 */
public class WriteStringCache2FileTask extends Thread{
 
	private static final Log logger = LogFactory.getLog(WriteStringCache2FileTask.class);
	
    private Blocker blocker = null;
	
	private boolean runflag = false; 
	
	private final static int sleepTime = Integer.valueOf(SyslogParserModuleConfig.getValue(CacheKeyCons.STRING_LOG_WRITE_SLEEP));
	
	private final static int wakeUpWeight = Integer.valueOf(SyslogParserModuleConfig.getValue(CacheKeyCons.STRING_LOG_WRITE_WEIGHT));
	
	public void startThread(){
		runflag = true;
	}
	
	public boolean getRunflag(){
  		return runflag; 
	}
	
	public void init(){
		this.blocker = NormalLogCache.getInstance().getBlockerMap().get(NormalLogCache.STRING_CACHE);
		if(this.blocker == null){
			this.blocker = new Blocker();
			NormalLogCache.getInstance().getBlockerMap().put(NormalLogCache.STRING_CACHE, this.blocker);
		}
	}
	
	@Override
	public void run() {
		if(logger.isInfoEnabled()){
			logger.info("WriteStringCache2FileTask [" + this.hashCode() + "] is Start.");
		}
		while (getRunflag()) {
		    this.task();
		}
	}
	
	/**
	 * 将sql队列中的数据写入到文件
	 * 
	 * @param queue
	 */
	private static void sync2File(ConcurrentLinkedQueue<String> queue){
		try {
			String tmpPath = CacheUtil.getSyncFilePath("string");
			File file = FileUtil.createFile(tmpPath);
			if(file == null){
				return;
			}
			boolean flag = FileUtil.write2File(file, queue, 500, true, false, "UTF-8");
			if(flag == false){
				logger.error("write string log queue to tmp file " + tmpPath + " failed!");
				return;
			}
			long fileSize = file.length();
			// 去掉临时扩展名
			String path = FileUtil.trimExtension(file, FileUtil.SYNC_FILE_TMP_EXTENSION);
			if("".equals(path)){
				logger.error("string log tmp file rename to fpath " + path + ", fsize " + fileSize + " failed!");
				return;
			}
			if(logger.isDebugEnabled()){
				logger.debug("string log tmp file rename to fpath " + path + ", fsize " + fileSize + " success.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	private void task(){
		// 休眠一定时间，自动唤醒，处理缓存集合中的string字符串，写入文件
		this.blocker.waitingCall(sleepTime*2000);
		// 队列处理权值增减控制
		if(logger.isDebugEnabled()){
			logger.debug("WriteStringCache2FileTask [" + this.hashCode() + "][" + NormalLogCache.getInstance().getStringCacheSize() + "] is Running ... ");
		}
		// 如果队列中的数据条数大于指定条目，isWakeUp就会被置为true，这样可以控制每次队列处理主线程唤醒时，如果队列中数据未达到指定条数，
		// 不会频繁处理写入的队列，但是如果长时间队列中的数据未达到指定条数，也应该及时处理掉队列中的数据，所以增加权值，
		// 当每次没有达到指定条数未被同步，则权值增加1，当达到指定权值后，不论队列中的数据是否达到指定条数，都进行队列数据的处理操作。
		if(this.blocker.getIsWakeUp() || this.blocker.getWakeUpWeight() >= wakeUpWeight){
			// 处理队列中的数据
			ConcurrentLinkedQueue<String> queue = NormalLogCache.getInstance().getStringCache();
			if(!queue.isEmpty()){
				long st = System.currentTimeMillis();
				// 写入操作
				sync2File(queue);
				long et = System.currentTimeMillis();
				if(logger.isInfoEnabled()){
					logger.info("WriteStringCache2FileTask [" + this.hashCode() + "][" + NormalLogCache.getInstance().getStringCacheSize() + "] StringCache use time " + (et-st));
				}
			}
			// 执行一次队列处理之后，若队列中的数据不存在未处理的，则唤醒标识位置为false，权值置为零
			if(queue.isEmpty()){
				this.blocker.setIsWakeUp(false);
				this.blocker.setWakeUpWeight(0);
			}
		}else{
			// 增大权值
			int count = this.blocker.getWakeUpWeight() + 1;
			this.blocker.setWakeUpWeight(count);
		}
	}
}
