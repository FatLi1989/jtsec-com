package com.jtsec.manager.controller;

import com.jtsec.common.util.response.HttpResponse;
import com.jtsec.manager.pojo.vo.MenuVo;
import com.jtsec.manager.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NovLi
 * @Title: no yi si
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/110:56
 */
@Slf4j
@RequestMapping(value = "/menu")
@RestController
public class MenuController {

	@Autowired
	private IMenuService iMenuService;

	@RequestMapping (value = "/editMenu", method = {RequestMethod.POST})
	public HttpResponse index (MenuVo menuVo) {
		HttpResponse http = new HttpResponse<> ();

		Integer result = iMenuService.editMenu (menuVo);

		if (result > 0) {
			return http;
		}
		return http;
	}

	@RequestMapping (value = "/select/all", method = {RequestMethod.GET})
	public HttpResponse selectMenus () {
		HttpResponse http = new HttpResponse<> ();

		List<MenuVo> menuVoList = iMenuService.selectMenus ();
		http.setData (menuVoList);
		return http;
	}

	@RequestMapping (value = "/del/{id}", method = {RequestMethod.GET})
	public HttpResponse delMenu (@PathVariable("id") Integer id) {
		HttpResponse http = new HttpResponse<> ();
		Integer result = iMenuService.delMenu (id);
		if (result > 0) {
			return http;
		}
		return http;
	}

}
