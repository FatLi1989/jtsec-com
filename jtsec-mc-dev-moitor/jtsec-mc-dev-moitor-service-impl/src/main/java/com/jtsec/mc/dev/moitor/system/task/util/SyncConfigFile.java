package com.jtsec.mc.dev.moitor.system.task.util;

import com.alibaba.fastjson.JSON;
import com.jtsec.common.util.surpass.FileUtil;
import com.jtsec.mc.dev.moitor.pojo.model.DBTableStruct;
import com.jtsec.mc.dev.moitor.pojo.model.DBTransportMessage;
import com.jtsec.mc.dev.moitor.pojo.model.SyncConfigDto;
import com.jtsec.mc.dev.moitor.pojo.model.TaskDatabaseSynchron;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SyncConfigFile {

	//private static final Logger logger = Logger.getLogger(ConfigFileUtil.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public static SyncConfigDto converEntity(DBTableStruct dataSynchronousPoji , TaskDatabaseSynchron taskDatabaseSynchron, String actionType){
		SyncConfigDto syncConfigFile = converConfigFile(dataSynchronousPoji, taskDatabaseSynchron, actionType);
		// 将cfg转成json字符串
		return syncConfigFile;
	}
	
	public static StringBuffer syncConfig(DBTransportMessage dBTransportMessage , TaskDatabaseSynchron  taskDatabaseSynchron){
		dBTransportMessage = converTranSport(dBTransportMessage, taskDatabaseSynchron);
		// 将cfg转成json字符串
		String jsonStr = JSON.toJSONString(dBTransportMessage);
		StringBuffer jsonBuffer = new StringBuffer(jsonStr);
		return jsonBuffer;
	}
	
	private static DBTransportMessage converTranSport(DBTransportMessage dBTransportMessage, TaskDatabaseSynchron taskDatabaseSynchron) {
		
		dBTransportMessage.setUrl(ternaryExpression(taskDatabaseSynchron.getSlaveIp()));
		dBTransportMessage.setPort(ternaryExpression(taskDatabaseSynchron.getSlavePort()));
		dBTransportMessage.setUserName(ternaryExpression(taskDatabaseSynchron.getSlaveUsername()));
		dBTransportMessage.setPassword(ternaryExpression(taskDatabaseSynchron.getSlavePassword()));
		dBTransportMessage.setDatabaseName(ternaryExpression(taskDatabaseSynchron.getHostDatabaseName()));
		dBTransportMessage.setDatabasetype((taskDatabaseSynchron.getSlaveDatabaseType()));
		return dBTransportMessage;
	}

	public static void sync2File(List<String> jsonStrList,  TaskDatabaseSynchron taskDatabaseSynchron){
		String path = SyncConfigFile.getSyncFilePath(taskDatabaseSynchron);
		FileUtil.sync2File(path, jsonStrList.toString(), FileUtil.SYNC_FILE_TMP_EXTENSION);
	}
	
	public static String getSyncFilePath(TaskDatabaseSynchron taskDatabaseSynchron){
		StringBuilder path = new StringBuilder();
		String pathHead = getSyncPath();
		String fileName = sdf.format(new Date()) + "_"+taskDatabaseSynchron.getHostDatabaseName()+".conf" + FileUtil.SYNC_FILE_TMP_EXTENSION;
		String separator = System.getProperty("file.separator");
		path.append(pathHead).append(pathHead.endsWith(File.separator) ? "" : separator);
		path.append(fileName);
		return path.toString();
	}
	
	
	public static String getSyncPath(){
		String osName = System.getProperty("os.name");	
		//String pathKey = "";  // 配置文件中的属性名
		String pathHead = "C:\\tmpdisk\\root\\jtsec_inner_trans\\send\\model\\system\\dbtablestructsync";
		if(osName.startsWith("Windows")){
			pathHead = "C:\\tmpdisk\\root\\jtsec_inner_trans\\send\\model\\system\\dbtablestructsync";
			pathHead = pathHead.replaceAll("/", "\\\\");
		}else{
			pathHead = pathHead.replaceAll("\\\\", "/");
		}
		return pathHead;
	}
	
	public static SyncConfigDto converConfigFile(DBTableStruct dataSynchronousPoji ,TaskDatabaseSynchron taskDatabaseSynchron ,String actionType) {
		
		SyncConfigDto syncConfigFile = new SyncConfigDto();
		syncConfigFile.setUrl(ternaryExpression(taskDatabaseSynchron.getSlaveIp()));
		syncConfigFile.setPort(ternaryExpression(taskDatabaseSynchron.getSlavePort()));
		syncConfigFile.setUserName(ternaryExpression(taskDatabaseSynchron.getSlaveUsername()));
		syncConfigFile.setPassword(ternaryExpression(taskDatabaseSynchron.getSlavePassword()));
		syncConfigFile.setAction(ternaryExpression(actionType));
		syncConfigFile.setDatabaseName(ternaryExpression(dataSynchronousPoji.getTableSchema()));
		syncConfigFile.setTableName(ternaryExpression(dataSynchronousPoji.getTableName()));
		syncConfigFile.setColumnName(ternaryExpression(dataSynchronousPoji.getColumnName()));
		syncConfigFile.setColumnType(ternaryExpression(dataSynchronousPoji.getColumnType()));
		syncConfigFile.setDataLength(ternaryExpression(dataSynchronousPoji.getDataLength()));
		syncConfigFile.setComment(ternaryExpression(dataSynchronousPoji.getColumnComment()));
		syncConfigFile.setConstraint(ternaryExpression(dataSynchronousPoji.getColumnKey()));
		
		return syncConfigFile;
	}

	/**
	 * 三目运算
	 * @param
	 * @return
	 */
	public static String ternaryExpression(String variable) {
		return (variable == null) ?"":variable;
	}
}
