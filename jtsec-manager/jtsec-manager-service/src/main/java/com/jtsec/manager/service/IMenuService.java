package com.jtsec.manager.service;

import com.jtsec.manager.pojo.model.User;
import com.jtsec.manager.pojo.vo.IndexVo;
import com.jtsec.manager.pojo.vo.MenuVo;
import java.util.List;
import java.util.Set;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/113:07
 */
public interface IMenuService {
	IndexVo selectMenuByLoginName (User user);

	List<MenuVo> selectMenus ();

	Integer delMenu (Integer id);

	Integer editMenu (MenuVo menuVo);

	Set<String> selectPermsByUserId (Integer userId);
}
