package com.jtsec.manager.pojo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Table: sys_role_menu
 */
@Data
public class RoleMenuKey implements Serializable {
    /**
     * Column: sys_role_menu.role_id
     */
    private Integer roleId;

    /**
     * Column: sys_role_menu.menu_id
     */
    private Integer menuId;

    /**
     * Table: sys_role_menu
     */
    private static final long serialVersionUID = 1L;

    public RoleMenuKey() {
    }

    public RoleMenuKey(Integer roleId, Integer menuId) {

        this.roleId = roleId;
        this.menuId = menuId;
    }
}