package com.jtsec.manager.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/1510:09
 */
@Data
public class RoleVo extends  BasicVo{

	private Integer roleId;

	private String roleName;

	private String roleKey;

	private Integer status;

	private Integer roleSort;

	private String createTime;

	private String createTimeBegin;

	private String createTimeEnd;

	private String remark;

	private List<Integer> roleIdList;

	private List<Integer> menuIdList;

}
