package com.jtsec.mc.dev.moitor.syslog.handler;

import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.mc.dev.moitor.snmp.JdbcDeviceUtil;
import com.jtsec.mc.dev.moitor.syslog.AbstractSyslogHandler;
import com.jtsec.mc.dev.moitor.syslog.parse.SyslogServerEvent;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
public class StandardHandler extends AbstractSyslogHandler{
	/**
	 * Logger for this class
	 */

	
	public StandardHandler(){
		
	}
	
	public StandardHandler(String ip, String region){
		super.ip = ip;
		super.region = region;
	}

	@Override
	public void handleRequest(List<String> sqlList, SyslogServerEvent sse) {
		String tableName = JdbcDeviceUtil.findDevTableName(sse.getHost());
		if("sfts".equalsIgnoreCase(sse.getIdent()) || "ftp".equalsIgnoreCase(sse.getIdent()) || tableName.indexOf("_fw") > -1 || tableName.indexOf("_ips") > -1){
			tableName = "log_sfts";
			if("ftp".equalsIgnoreCase(sse.getIdent())){
				tableName = "log_ftp";
			}
			if(tableName.indexOf("_fw") > -1){
				tableName = "log_fw";
			}else if(tableName.indexOf("_ips") > -1){
				tableName = "log_ips";
			}
			log.debug("接受的日志信息:" + new String(sse.getRawBytes(), 0, sse.getRawLength()));
			String fromHostIP = sse.getHost();
			String syslogTag = sse.getIdent();
			ByteArrayInputStream bis = new ByteArrayInputStream(sse.getRawBytes(), sse.getSubLength(), sse.getRawLength() - sse.getSubLength());
			String sql = "INSERT INTO " + tableName + " (FromHostIP, ReceivedAt, SysLogTag, Message) VALUES (?, ? ,? ,?)";
			Connection conn = null;
			try {
				conn = DbUtil.getConn();
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, fromHostIP);
				ps.setString(2, DateUtil.getNowTime());
				ps.setString(3, syslogTag);
				ps.setBinaryStream(4, bis);
				ps.execute();
				conn.commit();
			} catch (Exception e) {
				DbUtil.rollbackConn(conn);
				e.printStackTrace();
			}finally{
				DbUtil.closeConn(conn);
			}
		}else{
			callNextHandler(sqlList, sse);
		}
	}

	@Override
	public void setNextHandler(AbstractSyslogHandler abstractSyslogHandler) {
		super.abstractSyslogHandler = abstractSyslogHandler;
	}

	@Override
	public AbstractSyslogHandler getNextHandler() {
		return super.abstractSyslogHandler;
	}
}
