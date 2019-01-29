package com.jtsec.mc.dev.moitor.syslog;

import com.jtsec.common.util.surpass.PropertiesUtil;
import com.jtsec.mc.dev.moitor.snmp.util.ServIpUtil;
import com.jtsec.mc.dev.moitor.syslog.parse.SyslogServerEvent;
import com.jtsec.util.DatagramSocketUtil;
import org.apache.log4j.Logger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 将报文信息解析为系统日志信息
 * 
 * @author surpassE
 * @verison 1.0.1
 * @since 2015-08-18
 *
 */
public class SyslogParseThread implements Runnable{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SyslogParseThread.class);

	InetAddress inetAddress = null;
	private String ip = "";
	private String region = "";
	private byte[] buf = null;
	private int length = -1;
	//对接受的rsyslog处理方式 0：直接解析 1：发送到远程服务器 2：解析并转发
	private String flag = "1";

	public SyslogParseThread(){}

	public SyslogParseThread(byte[] buf){
		this.buf = buf;
		this.flag = "1";
	}
	public SyslogParseThread(String ip, String region, byte[] buf, int length){
		this.ip = ip;
		this.region = region;
		this.buf = buf;
		this.length = length;
		this.flag = "0";
	}

	public SyslogParseThread(InetAddress inetAddress, String region, byte[] buf, int length){
		this.inetAddress = inetAddress;
		this.region = region;
		this.buf = buf;
		this.length = length;
		this.flag = "0";
	}
	
	public void run(){
		if("0".equals(flag)){
			this.analyzeSyslogLocal();
		}else if("1".equals(this.flag)){
			this.forwardSyslog();
		}else if("2".equals(this.flag)){
			this.analyzeSyslogLocal();
			this.forwardSyslog();
		}
	}
	
	/**
	 * 本地处理接收的rsyslog信息
	 */
	private void analyzeSyslogLocal(){
		List<String> sqlList = new ArrayList<String>();
		//通过职责链解析VariableBinding集合数据信息
//		AbstractDataHandler dataHandler = DataHandlerFactory.createDataHandlerFactory().getDataHandler(this.ip, "", this.region, DateUtil.getNowTime());
//		dataHandler.handleRequest("dev-conf", sqlList, data);
//		DataHandlerFactory.releaseSources(dataHandler);
		
		SyslogServerEvent sse = new SyslogServerEvent(buf, length, inetAddress);
		logger.debug(sse.getIdent());
//		logger.debug(sse.getHost());
//		logger.debug(sse.getHostName());
		//收集防火墙、ips日志-顺德公安使用
//		AbstractSyslogHandler syslogHandler = SyslogHandlerFactory.createSyslogHandlerFactory().getStandardSyslogHandler(this.ip, this.region);
		AbstractSyslogHandler syslogHandler = SyslogHandlerFactory.createSyslogHandlerFactory().getSyslogHandler(this.ip, this.region);
		syslogHandler.handleRequest(sqlList, sse);
		SyslogHandlerFactory.releaseSources(syslogHandler);
		//执行数据存储操作
//		DbUtil.executeBetchSql(null, sqlList);
	}
	
	/**
	 * 转发syslog数据到远程服务器
	 */
	private void forwardSyslog(){
		try {
			String servIp = ServIpUtil.loadValue("serv_ip");
			String port = PropertiesUtil.getValue("datagram_server_port");
			//转发数据包
			DatagramSocketUtil.sendBytes(servIp, Integer.parseInt(port), buf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
