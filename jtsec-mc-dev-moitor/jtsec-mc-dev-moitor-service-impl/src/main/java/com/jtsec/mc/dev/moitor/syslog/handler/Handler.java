package com.jtsec.mc.dev.moitor.syslog.handler;

import com.jtsec.common.Constants.LogCons;
import com.jtsec.common.Constants.SystemCons;
import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.FileContentUtil;
import com.jtsec.common.util.surpass.PropertiesUtil;
import com.jtsec.mc.dev.moitor.snmp.JdbcDeviceUtil;
import com.jtsec.mc.dev.moitor.syslog.AbstractSyslogHandler;
import com.jtsec.mc.dev.moitor.syslog.handler.util.HandlerUtil;
import com.jtsec.mc.dev.moitor.syslog.model.EventLogInfo;
import com.jtsec.mc.dev.moitor.syslog.parse.SyslogServerEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
public class Handler extends AbstractSyslogHandler {
	/**
	 * Logger for this class
	 */
	private static String fileName = "";
	private static final String SYSLOG_MSG_PATH = "syslog_msg_path";

	public Handler (String ip, String region){
		super.ip = ip;
		super.region = region;
	}

	@Override
	public void handleRequest(List<String> sqlList, SyslogServerEvent sse) {
		String tableName = JdbcDeviceUtil.findDevTableName(sse.getHost());
		if(tableName != null && tableName.length() >= 0){
			if("ftp".equalsIgnoreCase(sse.getIdent())){
				tableName += "_ftp_log";
			}else if("http".equalsIgnoreCase(sse.getIdent())){
				tableName += "_http_log";
			}else if("sfts".equalsIgnoreCase(sse.getIdent())){
				tableName += "_sfts_log";
			}else if("siphole_back".equalsIgnoreCase(sse.getIdent()) || "siphole_end".equalsIgnoreCase(sse.getIdent())){
				tableName += "_video_log";
			}
			if(log.isDebugEnabled()){
				try {
					log.debug("接受的日志信息:" + new String(sse.getRawBytes(), 0, sse.getRawLength(), "GBK"));
					log.debug("接受的日志信息:" + new String(sse.getRawBytes(), 0, sse.getRawLength(), "UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
			String fromHostIP = sse.getHost();
			String syslogTag = sse.getIdent();
			if(tableName.endsWith("_gap_log")){
//				sse.setRawLength(sse.getRawLength() - sse.getHostName().length());
				sse.setSublength(sse.getSubLength() - sse.getHostName().length());
			}
			
			String msg = HandlerUtil.decodeMsgBytes(sse);
			EventLogInfo eventLog = HandlerUtil.parserEventLog(sse, msg);
					
			ByteArrayInputStream bis = new ByteArrayInputStream(sse.getRawBytes(), sse.getSubLength(), sse.getRawLength() - sse.getSubLength());
			String sql = "INSERT INTO " + tableName + " (FromHostIP, ReceivedAt, SysLogTag, Message, EventLogType, EventUser, InfoUnitID) VALUES (?, ?, ?, ?, ?, ?, ?)";
			Connection conn = null;
			try {
				conn = DbUtil.getConn(DbUtil.DB_JTSEC_LOG);
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, fromHostIP);
				String nowTime = DateUtil.getNowTime();
				ps.setString(2, nowTime);
				ps.setString(3, syslogTag);
				ps.setBinaryStream(4, bis);
				ps.setString(5, eventLog.getEveType());
				ps.setString(6, eventLog.getEveActor());
				ps.setString(7, eventLog.getEveActorSid()+"");
				ps.execute();
				conn.commit();
				if(LogCons.DEV_TYPE_CODE_TZ.equals(SystemCons.dev_type)){
					HandlerUtil.putSql2LogCache(sse, sql, nowTime, msg, eventLog);
				}
			} catch (Exception e) {
				DbUtil.rollbackConn(conn);
				e.printStackTrace();
			}finally{
				DbUtil.closeConn(conn);
			}
		}else{
			String filePath = PropertiesUtil.getValue(SYSLOG_MSG_PATH);
			//			String filePath = "D:/jtsec_mc/syslog_msg/";
			//创建接收rsyslog的日志信息
			FileContentUtil.createPath(filePath);
			if("".equals(fileName) || fileName == null){
				fileName = "syslog_" + System.currentTimeMillis();
			}
			File file = new File(filePath + fileName);
			if(file.exists() && file.length() >= (1024*1024)){
				fileName = "syslog_" + System.currentTimeMillis();
			}
			try{
				FileOutputStream fos = new FileOutputStream((filePath + fileName),true);
				fos.write(sse.getRawBytes(), 0, sse.getRawLength());
				fos.write("\n".getBytes());
				fos.flush();
				fos.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
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
