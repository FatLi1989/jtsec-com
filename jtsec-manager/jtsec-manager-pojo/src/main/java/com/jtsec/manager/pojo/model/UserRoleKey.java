package com.jtsec.manager.pojo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Table: sys_user_role
 */
@Data
public class UserRoleKey implements Serializable {
	/**
	 * Column: sys_user_role.user_id
	 */
	private Integer userId;

	/**
	 * Column: sys_user_role.role_id
	 */
	private Integer roleId;

	/**
	 * Table: sys_user_role
	 */
	private static final long serialVersionUID = 1L;

	public UserRoleKey() {
	}

	public UserRoleKey(Integer userId, Integer roleId) {

		this.userId = userId;
		this.roleId = roleId;
	}
}

