package com.jtsec.mc.dev.moitor.syslog;

import com.jtsec.mc.dev.moitor.syslog.parse.SyslogServerEvent;
import java.util.List;


public abstract class AbstractSyslogHandler {
	
	public AbstractSyslogHandler abstractSyslogHandler;
	protected String ip;
	protected String region;
	
	
	/**
	 * 处理SNMP数据信息
	 * @param
	 * @param sqlList
	 */
	public abstract void handleRequest(List<String> sqlList, SyslogServerEvent sse);
	
	/**
	 * 如果此处不能解析数据，那么调用下一个abstractDataHandler进行数据处理
	 * @param
	 */
	public abstract void setNextHandler(AbstractSyslogHandler abstractSyslogHandler);
	
	/**
	 * 获得下一个事件的处理的对象
	 * @param
	 */
	public abstract AbstractSyslogHandler getNextHandler();
	
	/**
	 * 将data传递到下一个handler进行解析
	 * 
	 * @param
	 * @param
	 * @param
	 */
	protected void callNextHandler(List<String> list, SyslogServerEvent sse){
		if(this.abstractSyslogHandler != null){
			this.abstractSyslogHandler.ip = this.ip;
			this.abstractSyslogHandler.region = this.region;
		}
		this.abstractSyslogHandler.handleRequest(list, sse);
	}
	
}
