package com.jtsec.mc.dev.moitor.system.task.util;

import com.github.pagehelper.util.StringUtil;
import com.jtsec.mc.dev.moitor.pojo.model.DBTableStruct;
import com.jtsec.mc.dev.moitor.pojo.model.TaskDatabaseSynchron;

public class DateBaseCommonSql {

	public static StringBuffer generateAlertTableName(String tableName, TaskDatabaseSynchron taskDatabaseSynchron) {
		
		StringBuffer sql = new StringBuffer("");
		
		if (taskDatabaseSynchron.getSlaveDatabaseType() == 1) {
			sql.append(MysqlSystemSql.ALTERTABLENAME+tableName);
		}
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 2) {
			sql.append(OracleSystemSql.ALTERTABLENAME+tableName+" add( ");
		}
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 3) {
			sql.append(SqlServerSystemSql.ALTERTABLENAME+tableName+" add ");
		}
		return sql;
	}
	/**
	 * 生成添加字段sql
	 * @param dataSynchronousPoji
	 * @param taskDatabaseSynchron
	 * @param sql
	 * @return
	 */
	public static String generateAddSql(DBTableStruct dataSynchronousPoji, TaskDatabaseSynchron taskDatabaseSynchron, StringBuffer sql) {
		
		if (taskDatabaseSynchron.getSlaveDatabaseType() == 1) {
			sql.append(" add "+dataSynchronousPoji.getColumnName()+" "+dataSynchronousPoji.getColumnType());
		
			if(dataSynchronousPoji.getColumnType().toUpperCase().equals("DATE")||dataSynchronousPoji.getColumnType().toUpperCase().equals("TIME")) 
				sql.append(" COMMENT '"+dataSynchronousPoji.getColumnComment()+"'");
			else 
				sql.append("("+dataSynchronousPoji.getDataLength()+")"+" COMMENT '"+dataSynchronousPoji.getColumnComment()+"'");
			
			if (!StringUtil.isEmpty(dataSynchronousPoji.getColumnKey())) {
				if ("PRI".equals(dataSynchronousPoji.getColumnKey())) 
					sql.append(" PRIMARY KEY ");
			}
			sql.append(",");
		}
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 3 ||taskDatabaseSynchron.getSlaveDatabaseType() == 2 ) {
			sql.append(dataSynchronousPoji.getColumnName()+" "+dataSynchronousPoji.getColumnType());
			
			if(dataSynchronousPoji.getColumnType().toUpperCase().equals("DATE")||dataSynchronousPoji.getColumnType().toUpperCase().equals("INT")
			 ||dataSynchronousPoji.getColumnType().toUpperCase().equals("TIME")) 
				sql.append(",");
			else
				sql.append("("+dataSynchronousPoji.getDataLength()+"),");

		}
		return sql.toString();
	}
	/**
	 * 生成创建sql
	 * @param dataSynchronousPoji
	 * @param taskDatabaseSynchron
	 * @param sql
	 * @return
	 */
	public static StringBuffer generateCreateSql(DBTableStruct dataSynchronousPoji, TaskDatabaseSynchron taskDatabaseSynchron, StringBuffer sql) {

		if (taskDatabaseSynchron.getSlaveDatabaseType() == 1) {
			sql.append(""+dataSynchronousPoji.getColumnName()+" "+dataSynchronousPoji.getColumnType());
			
			if(dataSynchronousPoji.getColumnType().toUpperCase().equals("DATE")||dataSynchronousPoji.getColumnType().toUpperCase().equals("TIME")) 
				sql.append(" COMMENT '"+dataSynchronousPoji.getColumnComment()+"'");
			else
				sql.append("("+dataSynchronousPoji.getDataLength()+")"+" COMMENT '"+dataSynchronousPoji.getColumnComment()+"'");
			
			if (!StringUtil.isEmpty(dataSynchronousPoji.getColumnKey())) {
				if ("PRI".equals(dataSynchronousPoji.getColumnKey())) 
					sql.append(" PRIMARY KEY ");
			}
			sql.append(",");
		}
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 2 ||taskDatabaseSynchron.getSlaveDatabaseType() == 3) {
			sql.append(dataSynchronousPoji.getColumnName()+" "+dataSynchronousPoji.getColumnType());
			
			if(dataSynchronousPoji.getColumnType().toUpperCase().equals("DATE")||dataSynchronousPoji.getColumnType().toUpperCase().equals("INT")
			 ||dataSynchronousPoji.getColumnType().toUpperCase().equals("TIME")) 
				sql.append(",");
			else 
				sql.append("("+dataSynchronousPoji.getDataLength()+"),");

		}
		return sql;
		
	}
	/**
	 * 生成删除sql
	 * @param tableName
	 * @param taskDatabaseSynchron 
	 */
	public static StringBuffer drop(String tableName, TaskDatabaseSynchron taskDatabaseSynchron) {
	
		StringBuffer sql = new StringBuffer("");
		
		if (taskDatabaseSynchron.getSlaveDatabaseType() == 1 || taskDatabaseSynchron.getSlaveDatabaseType() == 3)
			sql.append( "drop table "+tableName);
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 2 )
			sql.append( "drop table  "+taskDatabaseSynchron.getHostUsername().toUpperCase()+"."+tableName+"");
		return sql;
	}
	/**
	 * 生成删除字段sql
	 * @param dataSynchronousPoji
	 * @param taskDatabaseSynchron
	 * @param sql
	 */
	public static StringBuffer delColumn(DBTableStruct dataSynchronousPoji, TaskDatabaseSynchron taskDatabaseSynchron, StringBuffer sql) {

		if (taskDatabaseSynchron.getSlaveDatabaseType() == 1) 
			sql.append(" drop column "+dataSynchronousPoji.getColumnName()+",");
		
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 2) 
			sql.append(dataSynchronousPoji.getColumnName()+",");
		
		if (taskDatabaseSynchron.getSlaveDatabaseType() == 3) 
			sql.append("column "+dataSynchronousPoji.getColumnName()+",");
		
		return sql;
	}
	/**
	 * 生成删除字段表sql
	 * @param tableName
	 * @param taskDatabaseSynchron
	 * @return
	 */
	public static StringBuffer generateDropColumnName(String tableName, TaskDatabaseSynchron taskDatabaseSynchron) {
		
		StringBuffer sql = new StringBuffer("");
		
		if (taskDatabaseSynchron.getSlaveDatabaseType() == 1) {
			sql.append("alter table "+tableName);
		}
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 2) {
			sql.append("alter table "+tableName +" drop (");
		}
		else if (taskDatabaseSynchron.getSlaveDatabaseType() == 3) {
			sql.append("alter table "+tableName +" drop ");
		}
		return sql;
	}
	
}
