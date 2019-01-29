package com.jtsec.mc.dev.moitor.syslog.handler.util;

import com.jtsec.common.util.cache.log.model.NormalLogCache;
import com.jtsec.mc.dev.moitor.syslog.model.EventLogInfo;
import com.jtsec.mc.dev.moitor.syslog.parse.SyslogServerEvent;
import com.jtsec.util.CharacterSetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerUtil {


	public static String decodeMsgBytes(SyslogServerEvent sse){
		byte[] msgBytes = sse.getRawBytes();
		int msgLen = sse.getRawLength();
		int subLen = sse.getSubLength();
		String msg = CharacterSetUtil.decodeMsgBytes(msgBytes, subLen, msgLen - subLen);
		if("".equals(msg)){
				log.warn("decode msgBytes failed!");
			return "";
		}
		//String msgFormat = "UNHEX(HEX(" + msg + "))";
		//select hex('INFO cli.c:1000 cli: 192.168.18.247: 37266 [test2] Sent file: \'167838199downc1240f060bef497d8a5d029387ee2583.json\' size: 432 successfully.') from dual
		//INSERT INTO `igap5000_112_1527215543468_sfts_log_1527323605` VALUES ('0', null, '2018-05-25 10:32:44', null, null, null, '192.168.18.112', UNHEX(hex('INFO cli.c:1000 cli: 192.168.18.247: 37266 [test2] Sent file: \'167838199downc1240f060bef497d8a5d029387ee2583.json\' size: 432 successfully.')), null, null, null, null, null, null, null, null, null, null, null, null, 'sfts', null, null, null, null);
		msg = msg.replaceAll("'", "\\\\'");
		return msg;
	}
	
	public static EventLogInfo parserEventLog(SyslogServerEvent sse, String msg){
		EventLogInfo log = new EventLogInfo();
		if("ftp".equalsIgnoreCase(sse.getIdent())){
			
		}else if("http".equalsIgnoreCase(sse.getIdent())){
			log = HandlerUtil.parserHttpEventLog(msg);
		}else if("sfts".equalsIgnoreCase(sse.getIdent())){
			log = HandlerUtil.parserSftsEventLog(msg);
		}else if("siphole_back".equalsIgnoreCase(sse.getIdent()) || "siphole_end".equalsIgnoreCase(sse.getIdent())){
			
		}
		return log;
	}
	
	public static EventLogInfo parserHttpEventLog(String msg){
		EventLogInfo log = new EventLogInfo();
		try{
	        if(msg != null && msg.length() > 0 ){
	            String[] arr = msg.split(",");
	            int i = 0;
	            // super[超级管理员]
	            String userAndRole = arr[i++];
	            String ip = arr[i++];
	            String action = arr[i++];
	            String result = arr[i++];
	            String user = "";
	            int roleSid = 99;
	            int sInd = userAndRole.indexOf("[");
	            int eInd = userAndRole.indexOf("]");
	            if(sInd > -1 && eInd > -1){
	            	user = userAndRole.substring(0, sInd);
	            	String role = userAndRole.substring(sInd+1, eInd);
					// user like '%安全保密%' OR user like '%系统管理%') OR result like '%用户名不存在%'
	            	// 99普通用户，2系统管理员，3安全保密员，4审计管理员
	            	if(role != null){
	            		if(role.contains("系统管理")){
	            			roleSid = 2;
	            		}else if(role.contains("安全保密")){
	            			roleSid = 3;
	            		}else if(role.contains("日志审计")){
	            			roleSid = 4;
	            		}
	            	}
	            }
	            log.setEveActor(user);
	            log.setEveActorSid(roleSid+"");
	            log.setEveActorIp(ip);
	            log.setEveType(action);
	            log.setEveResult(result);
	        }  		
		}catch(Exception e){
			e.printStackTrace();
		}
		return log;
	}
	
	public static EventLogInfo parserSftsEventLog(String msg){
		EventLogInfo log = new EventLogInfo();
		try{
			String eveType = HandlerUtil.parserSftsMsgEventType(msg);
			log.setEveType(eveType);			
		}catch(Exception e){
			e.printStackTrace();
		}
		return log;
	}
			
	
	public static String parserSftsMsgEventType(String msg){
		String type ="";
		if (msg.contains(" auth ") || msg.contains("auth")) {
			type = "认证";
		} else if (msg.contains("error: Password is wrong.")) {
			type = "失败：密码错误";
		} else if (msg.contains("error: User is locked.")) {
			type = "失败：用户被锁定";
		} else if (msg.contains("found filted key word")) {
			type = "发现过滤关键字";
		} else if (msg.contains("Filterd.")) {
			type = "病毒过滤";
		} else if (msg.contains("Filter file")) {
			type = "过滤文件";
		} else if (msg.contains("Filterd")) {
			type = "文件内容非法";	
		} else if (msg.contains(" FOUND.")) {
			type = "病毒文件已过滤";
		} else if (msg.contains(" is Viruses")) {
			type = "发现病毒";
		} else if (msg.contains("Not Security file")) {
			type = "不安全文件";
		} else if (msg.contains(" OK.")) {
			type = "非法关键字过滤";
		} else if (msg.contains("password ok")) {
			type = "密码正确";
		} else if (msg.contains("Received file:") && msg.contains("successfully")) {
			type = "接收文件成功";
		} else if (msg.contains("size is not valid")) {
			type = "文件大小非法";
		} else if (msg.contains("Sent file") && msg.contains("successfully")) {
			type = "发送文件成功";
		} else if (msg.contains("type:")) {
			type = "文件格式非法";
		} else if (msg.contains("type:")) {
			type = "文件格式非法";
		} else if (msg.contains("was deleted.")) {
			type = "断开连接";
		} else if (msg.contains("new with id:")) {
			type = "新会话连接";
		} else if (msg.contains("has no certifacate.")) {
			type = "未发现证书";
		} else if (msg.contains("username:")) {
			type = "活动用户";
		} 
		
		return type;		
	}
	
	/**
	 * 将SQL压入日志推送缓存队列
	 * @param sse
	 * @param sql
	 * @param nowTime
	 * @param msg
	 * @param
	 */
	public static void putSql2LogCache(SyslogServerEvent sse, String sql, String nowTime, String msg, EventLogInfo eventLog){
		String fromHostIP = sse.getHost();
		String syslogTag = sse.getIdent();
		StringBuilder values = new StringBuilder();
		values.append("('");
		values.append(fromHostIP).append("','").append(nowTime).append("','");
		values.append(syslogTag).append("','").append(msg).append("','");
		values.append(eventLog.getEveType()).append("','").append(eventLog.getEveActor()).append("','");
		values.append(eventLog.getEveActorSid());
		values.append("')");
		String cacheSql = sql.replace("(?, ?, ?, ?, ?, ?, ?)", values.toString());
		NormalLogCache.getInstance().putSql(cacheSql);
	}
	
	public static void main(String[] args){
		String str = "INFO ''cli.c:1000 cli: '192.168.18.247: 37266 [test2] Sent file: '167838199downc1240f060bef497d8a5d029387ee2583.json' size: 432 successfully.";
		str = str.replaceAll("'", "\\\\'");
		System.out.println(str);
	}
	
	
}
