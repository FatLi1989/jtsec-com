package com.jtsec.mc.log.analysis.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/1417:05
 */
@Data
public class BasicVo implements Serializable {
	//页数
	protected Integer page;
	//每页条数
	protected Integer row;
	//总条数
	protected Integer count;
}
