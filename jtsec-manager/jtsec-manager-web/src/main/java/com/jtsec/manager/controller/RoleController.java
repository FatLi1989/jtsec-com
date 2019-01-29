package com.jtsec.manager.controller;

import com.jtsec.common.util.response.HttpResponse;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.pojo.model.Role;
import com.jtsec.manager.pojo.vo.RoleVo;
import com.jtsec.manager.service.IRoleService;
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
 * @date 2018/8/1715:34
 */
@Slf4j
@RestController
public class RoleController {

	@Autowired
	private IRoleService iRoleService;

	/**
	 * @Description: 角色查询
	 * @author NovLi
	 * @date 2018/8/17 15:34 
	 */
	@PostMapping ("/role/select/all")
	public HttpResponse selectRole (RoleVo roleVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<List<Role>> serviceResult = iRoleService.selectRoleByVarible (roleVo);
		httpResponse.setPageInfo (serviceResult.getPageInfo ());
		httpResponse.setData (serviceResult.getData ());
		return httpResponse;
	}

	/**
	 * @Description: 角色查询
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/role/eidt")
	public HttpResponse editRole (RoleVo roleVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = iRoleService.editRole (roleVo);
		if (!serviceResult.getSuccess ()) {
			httpResponse.setCode (0);
			httpResponse.setMeg (serviceResult.getMsg ());
			return httpResponse;
		}
		httpResponse.setMeg ("信息变更成功");
		return httpResponse;
	}

	/**
	 * @Description: 删除角色信息
	 * @author NovLi
	 * @date 2018/8/17 15:34
	 */
	@PostMapping ("/role/del")
	public HttpResponse delRole (RoleVo roleVo) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Boolean> serviceResult = iRoleService.delRole (roleVo.getRoleIdList ());
		if (!serviceResult.getSuccess ()) {
			httpResponse.setCode (0);
			httpResponse.setMeg (serviceResult.getMsg ());
			return httpResponse;
		}
		httpResponse.setMeg (serviceResult.getMsg ());
		return httpResponse;
	}

	@GetMapping ("/role/select/{id}")
	public HttpResponse selectRoleById (@PathVariable ("id") Integer id) {
		HttpResponse httpResponse = new HttpResponse ();
		ServiceResult<Role> serviceResult = iRoleService.selectRoleById (id);
		httpResponse.setData (serviceResult.getData ());
		return httpResponse;
	}

}
