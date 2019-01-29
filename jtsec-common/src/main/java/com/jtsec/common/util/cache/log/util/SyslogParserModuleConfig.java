package com.jtsec.common.util.cache.log.util;

import lombok.extern.slf4j.Slf4j;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * 
 * SYSLOG报文解析处理模块配置文件读取类
 * 
 * @author lioncode
 *	
 */
@Slf4j
public class SyslogParserModuleConfig {
	

	private static ResourceBundle bundle = null;
	private static final String conf = "config/cache/config";
	
	static{
		if(bundle == null){
			bundle = ResourceBundle.getBundle(conf);
		}
	}

	/**
	 * 重新加载数据信息
	 */
	public static void reloadBundle(){
		bundle = ResourceBundle.getBundle(conf);
	}

	/**
	 * 通过key获得模块配置文件中的value值
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		String value = null;
		try{
			value = bundle.getString(key);			
		} catch (NullPointerException e){
			log.warn("Get property [" + key + "] value failed, key is null" );
		} catch (MissingResourceException e){
			log.warn("Get property [" + key + "] value failed, no object for the given key can be found");
		} catch (ClassCastException e){
			log.warn("Get property [" + key + "] value failed, the object found for the given key is not a string");
		}
		return value;
	}

}

