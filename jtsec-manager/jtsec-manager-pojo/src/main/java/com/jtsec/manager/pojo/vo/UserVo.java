package com.jtsec.manager.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author NovLi
 * @Title: userVo
 * @ProjectName database_parent
 * @Description: 登陆页面传接值
 * @date 2018/7/916:00
 */
@Data
public class UserVo extends BasicVo {

	private String userName;

	private String password;

	private Boolean rememberMe;

	private String loginName;

	private Integer userId;

	private String phonenumber;

	private Integer status;

	private Date createTime;

	private String createTimeBegin;

	private String createTimeEnd;

	private String email;

	private String sex;

	private List<Integer> roleIdList;

	private Set<RoleVo> roles = new HashSet<> ();

	private List<Integer> userIdList;

	public UserVo (String username, String password, Boolean rememberMe, String loginName) {
		this.userName = username;
		this.password = password;
		this.rememberMe = rememberMe;
		this.loginName = loginName;
	}

	public UserVo (String password, String loginName) {
		this.password = password;
		this.loginName = loginName;
	}

	public UserVo (String loginName) {
		this.loginName = loginName;
	}

	public UserVo () {
	}
}
