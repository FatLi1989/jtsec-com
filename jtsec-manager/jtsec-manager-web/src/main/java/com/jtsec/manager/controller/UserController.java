package com.jtsec.manager.controller;

import com.jtsec.common.util.response.HttpResponse;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.pojo.model.User;
import com.jtsec.manager.pojo.vo.UserVo;
import com.jtsec.manager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/1316:22
 */
@Slf4j
@RestController
public class UserController {

	@Autowired
	private IUserService iUserService;

	/**
	 * @param userVo
	 * @Description: 查询用户
	 * @date 2018/8/13 17:11
	 */
	@PostMapping ("/user/select/all")
	public HttpResponse selectUser (UserVo userVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<List<User>> serviceResult = iUserService.selectUser (userVo);
		httpResponse.setPageInfo (serviceResult.getPageInfo ());
		httpResponse.setData (serviceResult.getData ());
		return httpResponse;
	}

	@GetMapping ("/user/select/{id}")
	public HttpResponse selectUserById (@PathVariable ("id") Integer id) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<User> serviceResult = iUserService.selectUserById (id);
		httpResponse.setData (serviceResult.getData ());
		return httpResponse;
	}

    /**
     * @param userVo
     * @Description: 新增用户
     * @date 2018/8/13 17:11
     */
    @PostMapping ("/user/edit")
    public HttpResponse editUser (UserVo userVo) {
        HttpResponse httpResponse = new HttpResponse ();
        ServiceResult<Boolean> serviceResult = iUserService.editUser (userVo);
        if (!serviceResult.getSuccess()) {
            httpResponse.setCode(0);
            httpResponse.setMeg("信息变更失败了");
            return httpResponse;
        }
        httpResponse.setMeg("信息变更成功");
        return httpResponse;
    }

	/**
	 * @param userVo
	 * @Description: 删除用户
	 * @date 2018/8/13 17:11
	 */
	@PostMapping ("/user/del")
	public HttpResponse delUser (UserVo userVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = iUserService.delUser (userVo.getUserIdList());
		if (!serviceResult.getSuccess()) {
			httpResponse.setCode(0);
			httpResponse.setMeg(serviceResult.getMsg());
			return httpResponse;
		}
		httpResponse.setMeg(serviceResult.getMsg());
		return httpResponse;
	}
}
