package com.jtsec.common.util.cache.log.util;

import com.jtsec.common.util.cache.log.common.CacheKeyCons;
import com.jtsec.common.util.surpass.FileUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CacheUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static String separator = System.getProperty("file.separator");
	
	public static String getSyncFilePath(String logType){
		StringBuilder path = new StringBuilder();
		String pathKey = "";
		String name = "";
		if(logType.equals("sql")){
			pathKey = CacheKeyCons.SQL_LOG_FILE_WRITE_PATH;
			name = 	"sql.log";
		}else if(logType.equals("json")){
			pathKey = "";
			name = "json.log";
		}else if(logType.equals("string")){
			pathKey = "";
			name = "string.log";
		}
		
		String prefix = SyslogParserModuleConfig.getValue(CacheKeyCons.WINDOWS_TEST_PATH_PREFIX);
		//String pathKey = logType.equals("sfts") ? SftLogCons.SFTS_LOG_FILE_SEND_PATH : SftLogCons.SFTC_LOG_FILE_SEND_PATH;
		String pathHead = SyslogParserModuleConfig.getValue(pathKey);
		pathHead = FileUtil.appendPath(prefix, pathHead);
		pathHead = FileUtil.formatStandardPath(pathHead);
		String fileName = sdf.format(new Date()) + "_" + name + FileUtil.SYNC_FILE_TMP_EXTENSION;
		path.append(pathHead).append(pathHead.endsWith(File.separator) ? "" : separator);
		path.append(fileName);
		return path.toString();
	}
	
	public static File getReadFilePath(String logType){
		StringBuilder path = new StringBuilder();
		String pathKey = "";
		if(logType.equals("sql")){
			pathKey = CacheKeyCons.SQL_LOG_FILE_READ_PATH;
		}else if(logType.equals("json")){
			pathKey = "";
		}else if(logType.equals("string")){
			pathKey = "";
		}
		String prefix = SyslogParserModuleConfig.getValue(CacheKeyCons.WINDOWS_TEST_PATH_PREFIX);
		String pathHead = SyslogParserModuleConfig.getValue(pathKey);
		pathHead = FileUtil.appendPath(prefix, pathHead);
		pathHead = FileUtil.formatStandardPath(pathHead);
		path.append(pathHead).append(pathHead.endsWith(File.separator) ? "" : separator);
		File file = new File(path.toString());
		return file;
	}

	

	
	public static void main(String[] args){
		test2();
	}
	
	public static void test(){
		String str = SyslogParserModuleConfig.getValue(CacheKeyCons.SQL_LOG_FILE_WRITE_PATH);
		String str2 = "C:/tmpdisk/root/jtsec_inner_trans/log/sql/";
		String str3 = "C\\:/tmpdisk/root/jtsec_inner_trans/log/sql/";
		String s = "---------------------------------------------";
		System.out.println("str-->" + str);
		System.out.println("str-->" + str.indexOf(":", 0));
		System.out.println("str-->" + str.indexOf(":", 1));
		System.out.println("str-->" + str.substring(2, str.length()));
		System.out.println(s);
		System.out.println("str2-->" + str);
		System.out.println("str2-->" + str2.indexOf(":", 1));
		System.out.println(s);
		System.out.println("str3-->" + str);
		System.out.println("str3-->" + str3.indexOf(":", 1));
		
	}
	
	public static void test2(){
		StringBuilder path = new StringBuilder();
		String prefix = SyslogParserModuleConfig.getValue(CacheKeyCons.WINDOWS_TEST_PATH_PREFIX);
		//String pathKey = logType.equals("sfts") ? SftLogCons.SFTS_LOG_FILE_SEND_PATH : SftLogCons.SFTC_LOG_FILE_SEND_PATH;
		String pathHead = SyslogParserModuleConfig.getValue(CacheKeyCons.SQL_LOG_FILE_WRITE_PATH);
		pathHead = FileUtil.appendPath(prefix, pathHead);
		pathHead = FileUtil.formatStandardPath(pathHead);
		String fileName = sdf.format(new Date()) + "_" + "sql.log" + FileUtil.SYNC_FILE_TMP_EXTENSION;
		path.append(pathHead).append(pathHead.endsWith(File.separator) ? "" : separator);
		path.append(fileName);
		System.out.println(path);
	}
	
}
