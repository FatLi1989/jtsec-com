package util;

import com.jtsec.manager.pojo.model.Menu;
import com.jtsec.manager.pojo.vo.MenuVo;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * @author NovLi
 * @Description: 烦 递归 菜
 * @date 2018/8/99:40
 */
public class MenuRecursion {
	private static MenuRecursion ourInstance = new MenuRecursion ();

	public static MenuRecursion getInstance () {
		return ourInstance;
	}

	private MenuRecursion () {

	}

	public static List<MenuVo> recursion (List<Menu> menuList) {
		List<MenuVo> menuVoList = new ArrayList<> ();
		for (Menu menu : menuList) {
			if (menu.getParentId () == 0) {
				MenuVo menuVo = setMenyVo (menu);
				List<MenuVo> childList = getChild (menuVo.getId (), menuList);
				menuVo.setChildren (childList);
				menuVoList.add (menuVo);
			}
		}
		return menuVoList;
	}

	private static MenuVo setMenyVo (Menu menu) {
		MenuVo menuVo = new MenuVo (menu.getMenuId (), menu.getMenuName (),
				menu.getUrl (), menu.getIcon (),
				menu.getParentId (), menu.getOrderNum (),
				menu.getMenuType (), menu.getVisible (),
				menu.getPerms ());
		return menuVo;
	}

	private static List<MenuVo> getChild (Integer id, List<Menu> menuList) {
		List<MenuVo> childList = new ArrayList<> ();
		for (Menu menu : menuList) {
			if (menu.getParentId ().equals (id)) {
				MenuVo menuVo = setMenyVo (menu);
				childList.add (menuVo);
			}
		}
		for (MenuVo menuVo : childList) {
			if (!StringUtils.isEmpty (menuVo.getUrl ()))
				menuVo.setChildren (getChild (menuVo.getId (), menuList));
		}
		if (childList.size () == 0)
			return null;
		return childList;
	}
}
