package com.jtsec.manager.pojo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Table: sys_user
 */
@Data
public class User implements Serializable {
    /**
     * Column: sys_user.user_id
     */
    private Integer userId;

    /**
     * Column: sys_user.dept_id
     */
    private Integer deptId;

    /**
     * Column: sys_user.login_name
     */
    private String loginName;

    /**
     * Column: sys_user.user_name
     */
    private String userName;

    /**
     * Column: sys_user.email
     */
    private String email;

    /**
     * Column: sys_user.phonenumber
     */
    private String phonenumber;

    /**
     * Column: sys_user.sex
     */
    private String sex;

    /**
     * Column: sys_user.avatar
     */
    private String avatar;

    /**
     * Column: sys_user.password
     */
    private String password;

    /**
     * Column: sys_user.salt
     */
    private String salt;

    /**
     * Column: sys_user.status
     */
    private Integer status;

    /**
     * Column: sys_user.login_ip
     */
    private String loginIp;

    /**
     * Column: sys_user.login_date
     */
    private Date loginDate;

    /**
     * Column: sys_user.create_by
     */
    private String createBy;

    /**
     * Column: sys_user.create_time
     */
    private Date createTime;

    /**
     * Column: sys_user.update_by
     */
    private String updateBy;

    /**
     * Column: sys_user.update_time
     */
    private Date updateTime;

    /**
     * Column: sys_user.remark
     */
    private String remark;

    /**
     * Table: sys_user
     */
    private Set<Role> roles = new HashSet<> ();

    private static final long serialVersionUID = 1L;
}