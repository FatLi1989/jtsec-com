package com.jtsec.mc.dev.moitor.syslog;

import com.jtsec.mc.dev.moitor.syslog.handler.Handler;
import com.jtsec.mc.dev.moitor.syslog.handler.HttpHandler;
import com.jtsec.mc.dev.moitor.syslog.handler.StandardHandler;

public class SyslogHandlerFactory {
	
	private static SyslogHandlerFactory syslogHandlerFactory = null;
	public SyslogHandlerFactory(){}
	
	public static synchronized SyslogHandlerFactory createSyslogHandlerFactory(){
		if(syslogHandlerFactory == null	){
			return new SyslogHandlerFactory();
		}
		return syslogHandlerFactory;
	}
	
	
	public AbstractSyslogHandler getSyslogHandler(String ip, String region){
		AbstractSyslogHandler syslogHandler = new Handler (ip, region);
		return syslogHandler;
	}
	
	public AbstractSyslogHandler getStandardSyslogHandler(String ip, String region){
		AbstractSyslogHandler syslogHandler = new StandardHandler (ip, region);
		syslogHandler.setNextHandler(new HttpHandler ());
		return syslogHandler;
	}
	
	/**
	 * 将创建的对象置空，尽快释放资源
	 */
	public static void releaseSources(AbstractSyslogHandler syslogHandler){
		if(syslogHandler != null){
			AbstractSyslogHandler ash = syslogHandler.getNextHandler();
			releaseSources(ash);
			syslogHandler = null;
		}
	}
}
