package com.jtsec.manager.controller;

import com.jtsec.common.util.response.HttpResponse;
import com.jtsec.manager.pojo.model.User;
import com.jtsec.manager.pojo.vo.IndexVo;
import com.jtsec.manager.service.IMenuService;
import com.jtsec.manager.shiro.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/110:56
 */
@Slf4j
@RestController
public class IndexController {

	@Autowired
	private IMenuService iMenuService;

	@GetMapping ( value = "/index")
    public HttpResponse index () {
		HttpResponse http = new HttpResponse<> ();
		User user = ShiroUtil.getUser ();
		IndexVo indexVo = iMenuService.selectMenuByLoginName(user);
		http.setData (indexVo);
		return http;
	}
}
