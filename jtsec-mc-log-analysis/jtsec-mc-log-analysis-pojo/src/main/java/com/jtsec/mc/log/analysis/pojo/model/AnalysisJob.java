package com.jtsec.mc.log.analysis.pojo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnalysisJob implements Serializable {

	private Integer jobId;

	private String jobName;

	private String jobGroup;

	private String methodName;

	private String params;

	private String cronExpression;

	private Integer status;

	private String createBy;

	private String createTime;

	private String updateBy;

	private String updateTime;

	private String remark;

	private static final long serialVersionUID = 1L;
}