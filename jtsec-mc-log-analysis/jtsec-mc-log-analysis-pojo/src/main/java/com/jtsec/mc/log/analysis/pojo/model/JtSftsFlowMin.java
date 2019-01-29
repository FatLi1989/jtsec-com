package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class JtSftsFlowMin implements Serializable {

	private Integer id;

	private String time;

	private String servIp;

	private String servName;

	private Integer fileCount;

	private String direction;

	private Long flowSize;

	//添加属性
	private String startTime;

	private String endTime;

	private static final long serialVersionUID = 1L;

	public JtSftsFlowMin () {
	}

	public JtSftsFlowMin (String time, String servIp, String servName, Integer fileCount, String direction, Long flowSize) {
		this.time = time;
		this.servIp = servIp;
		this.servName = servName;
		this.fileCount = fileCount;
		this.direction = direction;
		this.flowSize = flowSize;
	}

	public JtSftsFlowMin (String time, String direction, String endTime) {
		this.startTime = time;
		this.direction = direction;
		this.endTime = endTime;
	}
}