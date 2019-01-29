package com.jtsec.manager.pojo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Table: sys_menu
 */
@Data
public class Menu implements Serializable {
    /**
     * Column: sys_menu.menu_id
     */
    private Integer menuId;

    /**
     * Column: sys_menu.menu_name
     */
    private String menuName;

    /**
     * Column: sys_menu.parent_id
     */
    private Integer parentId;

    /**
     * Column: sys_menu.order_num
     */
    private Integer orderNum;

    /**
     * Column: sys_menu.url
     */
    private String url;

    /**
     * Column: sys_menu.menu_type
     */
    private String menuType;

    /**
     * Column: sys_menu.visible
     */
    private Integer visible;

    /**
     * Column: sys_menu.perms
     */
    private String perms;

    /**
     * Column: sys_menu.icon
     */
    private String icon;

    /**
     * Column: sys_menu.create_by
     */
    private String createBy;

    /**
     * Column: sys_menu.create_time
     */
    private Date createTime;

    /**
     * Column: sys_menu.update_by
     */
    private String updateBy;

    /**
     * Column: sys_menu.update_time
     */
    private Date updateTime;

    /**
     * Column: sys_menu.remark
     */
    private String remark;

    private Set<Role> roles = new HashSet<> ();

	public Menu (Integer menuId, String menuName, Integer parentId, Integer orderNum, String url, String menuType, Integer visible, String perms, String icon, String createBy, String remark) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.parentId = parentId;
		this.orderNum = orderNum;
		this.url = url;
		this.menuType = menuType;
		this.visible = visible;
		this.perms = perms;
		this.icon = icon;
		this.createBy = createBy;
		this.remark = remark;
	}
	/**

     * Table: sys_menu
     */
    private static final long serialVersionUID = 1L;


	public Menu () {
	}
}