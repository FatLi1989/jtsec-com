package com.jtsec.common.util.cache.log.task.normal;

import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.cache.log.util.CacheUtil;
import com.jtsec.common.util.cache.log.util.SyslogParserModuleConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;

/**
 * 
 * 从文件中读取数据，存入到STRING格式的日志缓存中
 *
 * @author 吴晗
 * 2018-3-30
 */
public class ReadStringCache4FileTask extends Thread{

	private static final Log logger = LogFactory.getLog(ReadStringCache4FileTask.class);
	
    private boolean isRun = false;
	
	private final static int sleepTime = Integer.valueOf(SyslogParserModuleConfig.getValue(CacheKeyCons.STRING_LOG_READ_SLEEP));
	
	public void startTask(){
		isRun = true;
	}
	
	public void stopTask(){
  		isRun = false; 
	}
	
	public boolean getTaskStatus(){
  		return isRun; 
	}
	
	
	@Override
	public void run() {
		if(logger.isInfoEnabled()){
			logger.info("ReadStringCache4FileTask [" + this.hashCode() + "] is Start.");
		}
		while (getTaskStatus()) {
			try {
				ReadSqlCache4FileTask.sleep(sleepTime*2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(logger.isDebugEnabled()){
				logger.debug("ReadStringCache4FileTask [" + this.hashCode() + "] is Running ... ");
			}
			// 将文件中的数据写入到队列
			file2Queue();
		}
	}
	
	
	/**
	 *  读取文件中的数据放到队列当中
	 */
	private static void file2Queue(){
		File dir = CacheUtil.getReadFilePath("string");
		File[] list = dir.listFiles();
		if (list != null) {
			for (File file : list) {
				String fileName = file.getName();
				if (fileName.endsWith("string.log")) {
					// 操作
					
				}
			}
		}
	}
}
