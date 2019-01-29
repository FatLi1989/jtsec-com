package com.jtsec.manager.service;

import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.pojo.model.Role;
import com.jtsec.manager.pojo.vo.RoleVo;
import java.util.List;
import java.util.Set;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/1715:36
 */
public interface IRoleService {
	ServiceResult<List<Role>> selectRoleByVarible (RoleVo roleVo);

	ServiceResult<Boolean> editRole (RoleVo roleVo);

	ServiceResult<Boolean> delRole (List<Integer> roleIdList);

	ServiceResult<Role> selectRoleById (Integer id);

	Set<String> getRoles (Integer userId);
}
