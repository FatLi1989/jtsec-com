package com.jtsec.manager.service.impl;

import com.jtsec.manager.mapper.MenuMapper;
import com.jtsec.manager.pojo.model.Menu;
import com.jtsec.manager.pojo.model.User;
import com.jtsec.manager.pojo.vo.IndexVo;
import com.jtsec.manager.pojo.vo.MenuVo;
import com.jtsec.manager.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.MenuRecursion;

import java.util.*;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/116:00
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

	@Autowired
	private MenuMapper menuMapper;
	/**
	 * @Description: 为什么用java递归 sql做递归影响性能 写存储太麻烦 所以java做递归查询
	 * @author NovLi
	 * @date 2018/8/3 15:06 
	 */
	@Override
	public IndexVo selectMenuByLoginName (User user) {
        IndexVo indexVo = new IndexVo();
		Map<String, Object> map = new HashMap<> ();
		map.put ("loginName", user.getLoginName ());
		List<Menu> menuList = menuMapper.selectMenuByUserVarible(map);
	    List<MenuVo> menuVoList = MenuRecursion.recursion (menuList);
		indexVo.setMenuVo (menuVoList);
		return indexVo;
	}

	@Override
	public Integer editMenu (MenuVo menuVo) {
		Menu menu = convertMenu (menuVo);
		Integer result = null;
		if (menu.getMenuId () == null)
			result = menuMapper.insertSelective (menu);
		else
			result = menuMapper.updateByPrimaryKeySelective (menu);
		return result;
	}

	@Override
	public Set<String> selectPermsByUserId (Integer userId) {
		List<String> perms = menuMapper.selectPermsByUserId (userId);
		Set<String> permsSet = new HashSet<> ();
		for (String perm : perms) {
			if (StringUtils.isNotEmpty (perm)) {
				permsSet.addAll (Arrays.asList (perm.trim ().split (",")));
			}
		}
		return permsSet;
	}

	@Override
	public List<MenuVo> selectMenus () {
		List<Menu> menuList = menuMapper.selectMenus ();
		List<MenuVo> menuVoList = MenuRecursion.recursion (menuList);
		return menuVoList;
	}

	@Override
	public Integer delMenu (Integer id) {
		Integer result = menuMapper.deleteByPrimaryKey(id);
		return result;
	}

	private Menu convertMenu (MenuVo menuVo) {
		String menuType = "";
		if (menuVo.getMenuType ().equals ("1")) {
			menuType = "M";
		} else if (menuVo.getMenuType ().equals ("2")) {
			menuType = "C";
		} else if (menuVo.getMenuType ().equals ("3")) {
			menuType = "F";
		}
		Menu menu = new Menu (menuVo.getId () == null ? null : menuVo.getId(), menuVo.getName (), menuVo.getParentId (),
				menuVo.getOrderNum (), menuVo.getUrl (),
				menuType, menuVo.getVisible (),
				menuVo.getPerms (), menuVo.getImg (),
				"admin", "what");
		return menu;
	}

}
