package com.jtsec.manager.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/115:09
 */
@Data
@JsonIgnoreProperties(value = {"loginName"})
public class MenuVo implements Serializable {

	private Integer id;

	private String name;

	private String url;

	private String img;

	private Integer parentId;

	private Integer orderNum;

	private String menuType;

	private Integer visible;

	private String perms;

	private Boolean expanded = false;

	private List<MenuVo> children;

	public List<MenuVo> getChildren () {
		if (children == null )
			return new ArrayList<> ();
		else
			return children;
	}

	public MenuVo () {
    }

    public MenuVo (Integer id, String name, String url, String img,  Integer orderNum, String menuType, Integer visible, String perms) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.img = img;
        this.orderNum = orderNum;
        this.menuType = menuType;
        this.visible = visible;
        this.perms = perms;
    }

	public MenuVo (Integer id, String name, String url, String img, Integer parentId, Integer orderNum, String menuType, Integer visible, String perms) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.img = img;
		this.parentId = parentId;
		this.orderNum = orderNum;
		this.menuType = menuType;
		this.visible = visible;
		this.perms = perms;
	}
}
