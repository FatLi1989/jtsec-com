package com.jtsec.mc.dev.moitor.pojo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * [STRATO MyBatis Generator]
 * Table: nem_device_info
 *
 * @mbggenerated do_not_delete_during_merge 2018-11-19 14:01:02
 */
@Data
public class NemDeviceInfo implements Serializable {

	private String id;

	private String devIdentify;

	private String devSyslogGather;

	private String devType;

	private String devName;

	private String devOsType;

	private String devVender;

	private String devVersion;

	private String devHardVersion;

	private String devIp;

	private String devPort;

	private String devStatus;

	private String devTableName;

	private String devManagerUrl;

	private String devMac;

	private String devNetmask;

	private String devOid;

	private String devHostname;

	private String devDesc;

	private Date monitorTime;

	private String monitorRecord;

	private String memo;

	private String snmpType;

	private String snmpPort;

	private Integer snmpDelay;

	private Integer snmpReconnect;

	private String snmpRcommunity;

	private String snmpWcommunity;

	private String snmpUserName;

	private String snmpSecLevel;

	private String snmpAuthDeal;

	private String snmpAuthPwd;

	private String snmpEncryptDeal;

	private String snmpEncryptPwd;

	private Integer regionId;

	//区域信息的主键
	private String regionName;
	private String ids = "";
	private String disConn = "1";
	//eg snmpwalk -v2c -c public@8 x.x.x.x 1.3.6.1.2.1  此时snmpRcommunityParam=@8，用于采集三层交换机网口对应的设备地址
	private String snmpRcommunityParam = "";

	private static final long serialVersionUID = 1L;
}
