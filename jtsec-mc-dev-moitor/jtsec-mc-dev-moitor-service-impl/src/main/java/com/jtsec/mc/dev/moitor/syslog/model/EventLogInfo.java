package com.jtsec.mc.dev.moitor.syslog.model;

import lombok.Data;

@Data
public class EventLogInfo {
	//事件唯一标识
	private Integer eveSid = 0;
	//事件类型
	private String eveType = "";
	//事件时间
	private String eveTime = "";
	//事件主体
	private String eveActor = "";
	//事件客体
	private String eveObject = "";
	//事件动作
	private String eveAction = "";
	//事件结果
	private String eveResult = "";
	//事件描述
	private String eveDsc = "";
	//事件主体的IP地址
	private String eveActorIp = "";
	//事件主体标识(99普通用户，2系统管理员，3安全保密员，4审计管理员)
	private String eveActorSid = "99";
	
}
