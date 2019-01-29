package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class JtSftsFlow implements Serializable {

	//sfts文件传输流量表
	public static final String TABLE_JtSftsFlow = "jt_sfts_flow";
	//ftp文件传输流量表
	public static final String TABLE_JtFtpFlow = "jt_ftp_flow";

	private Integer id;

	private String time;

	private String fromIp;

	private String servName;

	private String cliIp;

	private String cliPort;

	private String direction;

	private String fileName;

	private String fileType;

	private String filePath;

	private Integer fileSize;

	private static final long serialVersionUID = 1L;

	/*----- 后添加属性 -----*/
	private String startTime;

	private String endTime;

	private Integer fileCount;

	private Long flowSize;

	public JtSftsFlow (String direction, String startTime, String endTime) {
		this.direction = direction;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public JtSftsFlow (String time, String fromIp, String servName, String cliIp, String cliPort, String direction,
					   String fileName, String fileType, String filePath, Integer fileSize) {
		this.time = time;
		this.fromIp = fromIp;
		this.servName = servName;
		this.cliIp = cliIp;
		this.cliPort = cliPort;
		this.direction = direction;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.fileSize = fileSize;
	}

	public JtSftsFlow () {
	}
}