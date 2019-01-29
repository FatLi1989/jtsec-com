package com.jtsec.mc.dev.moitor.snmp.util;

import org.apache.commons.configuration.PropertiesConfiguration;

public class ServIpUtil {

	/**
	 * 加载集控服务器地址
	 * @return
	 * @throws Exception
	 */
	public static String loadValue(String key) throws Exception{
		String mcServFilePath = ServIpUtil.class.getResource("/") + "mc_serv.properties";
		PropertiesConfiguration mcSer = new PropertiesConfiguration (mcServFilePath);
		String ip = mcSer.getString(key);
		mcSer.save(mcServFilePath);
		return ip;
	}
	
	/**
	 * 更新集控服务器地址
	 * @param servIp
	 * @throws Exception
	 */
	public static void editServIp(String servIp) throws Exception{
		String mcServFilePath = ServIpUtil.class.getResource("/") + "mc_serv.properties";
		PropertiesConfiguration mcSer = new PropertiesConfiguration (mcServFilePath);
		mcSer.setProperty("serv_ip", servIp);
		mcSer.save(mcServFilePath);
	}
	
}
