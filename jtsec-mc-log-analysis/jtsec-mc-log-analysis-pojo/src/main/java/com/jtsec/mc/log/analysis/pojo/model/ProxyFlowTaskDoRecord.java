package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProxyFlowTaskDoRecord {

	private Integer id; //序号 无意义

	private String servProtocol; //服务协议

	private String recordType; //时间类型（min：分钟，hour：小时，day：天）

	private String direction; // 流量方向

	private String countTime; //任务执行记录的时间

	private String existData; //这条记录是否真的存在，存在：1，不存在：0

	//额外属性
	private String startTime; //开始时间

	private String endTime;	 //结束时间

	private static final long serialVersionUID = 1L;

	public ProxyFlowTaskDoRecord (String servProtocol, String recordType, String direction, String existData, String startTime, String endTime) {
		this.servProtocol = servProtocol;
		this.recordType = recordType;
		this.direction = direction;
		this.existData = existData;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public ProxyFlowTaskDoRecord () {
	}

	public ProxyFlowTaskDoRecord (String servProtocol,
								  String recordType,
								  String direction,
								  String countTime,
								  String existData) {
		this.servProtocol = servProtocol;
		this.recordType = recordType;
		this.direction = direction;
		this.countTime = countTime;
		this.existData = existData;
	}

	public ProxyFlowTaskDoRecord (String servProtocol, String recordType, String direction, String existData) {
		this.servProtocol = servProtocol;
		this.recordType = recordType;
		this.direction = direction;
		this.existData = existData;
	}

	public ProxyFlowTaskDoRecord (String direction, String startTime) {
		this.direction = direction;
		this.startTime = startTime;
	}

	public ProxyFlowTaskDoRecord (String servProtocol, String recordType, String direction) {
		this.servProtocol = servProtocol;
		this.recordType = recordType;
		this.direction = direction;
	}
}