package com.jtsec.manager.pojo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Table: sys_role
 */
@Data
public class Role implements Serializable {
    /**
     * Column: sys_role.role_id
     */
    private Integer roleId;

    /**
     * Column: sys_role.role_name
     */
    private String roleName;

    /**
     * Column: sys_role.role_key
     */
    private String roleKey;

    /**
     * Column: sys_role.role_sort
     */
    private Integer roleSort;

    /**
     * Column: sys_role.status
     */
    private Integer status;

    /**
     * Column: sys_role.create_by
     */
    private String createBy;

    /**
     * Column: sys_role.create_time
     */
    private Date createTime;

    /**
     * Column: sys_role.update_by
     */
    private String updateBy;

    /**
     * Column: sys_role.update_time
     */
    private Date updateTime;

    /**
     * Column: sys_role.remark
     */
    private String remark;

    private Set<User> users = new HashSet<> ();

    private Set<Menu> menus = new HashSet<> ();
    /**
     * Table: sys_role
     */
    private static final long serialVersionUID = 1L;

}