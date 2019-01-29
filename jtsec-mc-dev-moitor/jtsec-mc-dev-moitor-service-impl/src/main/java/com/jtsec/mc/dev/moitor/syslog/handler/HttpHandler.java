package com.jtsec.mc.dev.moitor.syslog.handler;

import com.jtsec.common.util.surpass.DateUtil;
import com.jtsec.common.util.surpass.DbUtil;
import com.jtsec.common.util.surpass.FileContentUtil;
import com.jtsec.common.util.surpass.PropertiesUtil;
import com.jtsec.mc.dev.moitor.syslog.AbstractSyslogHandler;
import com.jtsec.mc.dev.moitor.syslog.parse.SyslogServerEvent;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
public class HttpHandler extends AbstractSyslogHandler{
	/**
	 * Logger for this class
	 */
	private static String fileName = "";
	private static final String SYSLOG_MSG_PATH = "syslog_msg_path";

	@Override
	public void handleRequest(List<String> sqlList, SyslogServerEvent sse) {
//		if("http".equalsIgnoreCase(sse.getIdent())){
		if("http".equalsIgnoreCase(sse.getIdent()) || (sse.getIdent() != null && sse.getIdent().indexOf("http") > -1)){
			String fromHostIP = sse.getHost();
			String syslogTag = sse.getIdent();
			String message = null;
			try {
				message = new String(sse.getRawBytes(), sse.getSubLength(), (sse.getRawLength() - sse.getSubLength()), "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			log.debug(fromHostIP);
			log.debug(syslogTag);
			log.debug(message);
			String sql = "INSERT INTO log_http (FromHostIP, ReceivedAt, SysLogTag, Message) VALUES (?, ? ,? ,?)";
			Connection conn = null;
			try {
				conn = DbUtil.getConn();
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, fromHostIP);
				ps.setString(2, DateUtil.getNowTime());
				ps.setString(3, syslogTag);
				ps.setString(4, message);
				ps.execute();
				conn.commit();
			} catch (Exception e) {
				DbUtil.rollbackConn(conn);
				e.printStackTrace();
			}finally{
				DbUtil.closeConn(conn);
			}
		}else{
//			callNextHandler(sqlList, sse);
			String filePath = PropertiesUtil.getValue(SYSLOG_MSG_PATH);
//			String filePath = "D:/jtsec_mc/syslog_msg/";
			//创建接收rsyslog的日志信息
			FileContentUtil.createPath(filePath);
			if("".equals(fileName) || fileName == null){
				fileName = "fw_" + System.currentTimeMillis();
			}
			File file = new File(filePath + fileName);
			if(file.exists() && file.length() >= (1024*1024)){
				fileName = "fw_" + System.currentTimeMillis();
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
