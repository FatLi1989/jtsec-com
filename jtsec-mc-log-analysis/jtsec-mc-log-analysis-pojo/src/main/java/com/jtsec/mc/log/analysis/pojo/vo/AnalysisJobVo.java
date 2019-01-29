package com.jtsec.mc.log.analysis.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/249:51
 */
@Data
public class AnalysisJobVo extends BasicVo {

	private Integer jobId;

	private String jobName;

	private String jobGroup;

	private String methodName;

	private String params;

	private String cronExpression;

	private String status;

	private String remark;

	private String createBy;

	private String createTime;

	private String updateBy;

	private String updateTime;

	private List<Integer> jobIdList;
}
