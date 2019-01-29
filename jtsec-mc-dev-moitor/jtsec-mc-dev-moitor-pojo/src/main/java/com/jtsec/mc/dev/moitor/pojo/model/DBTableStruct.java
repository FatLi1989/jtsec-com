package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;
import java.io.Serializable;

/**对应tabble
 * task_table_column
 */
@Data
public class DBTableStruct implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public  static final String TASK_TABLE_COLUMN = "task_table_column";
	
	private      Integer        id;
	private      String			tableSchema;
	private		 String 		tableName;
	private 	 String 		columnName;
	private		 String 		columnType;
	private 	 String 		columnKey;
	private 	 String 		columnComment;
	private      String         dataLength;
	//对应task_database_synchron_id
	private      int            tdsId;  
	//---------------------------------------数据库参数-----------------------------------
	private      String         userName;
	private      String         password;
	private      String         ip;
	private      String         port;
	private      String         databaseType;
	//---------------------------------------返回的json-----------------------------------
	private      String         sql;

	@Override
	public String toString() {
		return "DBTableStruct [id=" + id + ", tableSchema=" + tableSchema + ", tableName=" + tableName + ", columnName="
				+ columnName + ", columnType=" + columnType + ", columnKey=" + columnKey + ", columnComment="
				+ columnComment + ", dataLength=" + dataLength + ", tdsId=" + tdsId + ", userName=" + userName
				+ ", password=" + password + ", ip=" + ip + ", port=" + port + ", databaseType=" + databaseType
				+ ", sql=" + sql + "]";
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDataLength() {
		return dataLength;
	}
	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}
	public boolean equals(Object obj) {
		if (obj instanceof DBTableStruct) {
			if (!this.tableSchema.equals(((DBTableStruct) obj).getTableSchema())) return false;
			if (!this.columnName.equals(((DBTableStruct)  obj).getColumnName()))  return false;
			if (!this.tableName.equals(((DBTableStruct)   obj).getTableName()))   return false;
			if (!this.columnType.equals(((DBTableStruct)  obj).getColumnType()))  return false;
			if (!this.columnKey.equals(((DBTableStruct)   obj).getColumnKey()))   return false;
		}
		return true;
	}
}
