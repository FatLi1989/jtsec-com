package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;

import java.io.Serializable;

/**对应tabble
 * task_table_column_log
 */
@Data
public class TaskTableColumnLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private    Integer     id;
	private    String      dyncTime;
	private    String      dyncDatabaseName;
	private    String      dyncTableName;
	private    String      dyncColumnName;
	private    String      dyncColumnComment;
	private    String      dyncColumnType;
	private    String      dyncConstraint;
	private    String      dyncDataLength;
	private    String      hostIp;
	private    String      hostPort;
	private    String      slaveIp;
	private    String      slavePort;
	private    String      dyncCollectionType;
	
	//-------------------------------------------------新添加属性-----------------------------------
	//同步开始时间
	private    String      syncStartTime;
	//同步结束时间
	private    String      syncEndTime;
	@Override
	public String toString() {
		return "TaskTableColumnLog [id=" + id + ", dyncTime=" + dyncTime + ", dyncDatabaseName=" + dyncDatabaseName
				+ ", dyncTableName=" + dyncTableName + ", dyncColumnName=" + dyncColumnName + ", dyncColumnComment="
				+ dyncColumnComment + ", dyncColumnType=" + dyncColumnType + ", dyncConstraint=" + dyncConstraint
				+ ", dyncDataLength=" + dyncDataLength + ", hostIp=" + hostIp + ", hostPort=" + hostPort + ", slaveIp="
				+ slaveIp + ", slavePort=" + slavePort + ", dyncCollectionType=" + dyncCollectionType
				+ ", syncStartTime=" + syncStartTime + ", syncEndTime=" + syncEndTime + "]";
	}
	public String getSlavePort() {
		return slavePort;
	}
	public void setSlavePort(String slavePort) {
		this.slavePort = slavePort;
	}
	
    public String getColumn() {
		return "dync_time,dync_database_name,dync_table_name,dync_column_name,host_ip,host_port,slave_ip,slave_port,dync_collection_type,dync_column_comment,dync_column_type,dync_constraint,dync_dataLength";
    }	

}