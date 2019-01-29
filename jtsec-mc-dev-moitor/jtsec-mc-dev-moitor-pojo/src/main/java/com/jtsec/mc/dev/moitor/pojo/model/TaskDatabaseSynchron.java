package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskDatabaseSynchron implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private    Integer 		id;
	private    String 		hostIp;
	private    String 		hostPort;
	private    String 		hostUsername;
	private    String 		hostPassword;
	private    Integer 		hostDatabaseType;
	private    String       hostDatabaseName;
	private    String       slaveIp;
	private    String       slavePort;
	private    String       slaveUsername;
	private    String       slavePassword;
	private    Integer      slaveDatabaseType;
	private    String       synchronisationStrategy;
	private    String       createTime;
	private    String       updateTime;

	@Override
	public String toString() {
		return "TaskDatabaseSynchron [id=" + id + ", hostIp=" + hostIp + ", hostPort=" + hostPort + ", hostUsername="
				+ hostUsername + ", hostPassword=" + hostPassword + ", hostDatabaseType=" + hostDatabaseType
				+ ", hostDatabaseName=" + hostDatabaseName + ", slaveIp=" + slaveIp + ", slavePort=" + slavePort
				+ ", slaveUsername=" + slaveUsername + ", slavePassword=" + slavePassword + ", slaveDatabaseType="
				+ slaveDatabaseType + ", synchronisationStrategy=" + synchronisationStrategy + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}
}
