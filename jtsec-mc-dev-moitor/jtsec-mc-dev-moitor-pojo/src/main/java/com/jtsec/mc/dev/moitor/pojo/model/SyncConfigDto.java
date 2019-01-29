package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SyncConfigDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private     Integer         	 databasetype;
	private     String         		 url;
	private     String         		 port;
	private     String         		 userName;
	private     String    			 password;
	private   	String  			 action;
	private     String         		 databaseName;
	private   	String         		 tableName;
	private     String          	 columnName;
	private     String          	 columnType;
	private     String          	 dataLength;
	private     String          	 comment;
	private     String          	 constraint;
	
	@Override
	public String toString() {
		return "SyncConfigFile [databasetype=" + databasetype + ", url=" + url + ", port=" + port + ", userName="
				+ userName + ", password=" + password + ", action=" + action + ", databaseName=" + databaseName
				+ ", tableName=" + tableName + ", columnName=" + columnName + ", columnType=" + columnType
				+ ", dataLength=" + dataLength + ", comment=" + comment + ", constraint=" + constraint + "]";
	}
}
