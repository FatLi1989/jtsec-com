package com.jtsec.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.common.util.string.StringUtils;
import com.jtsec.manager.mapper.RoleMapper;
import com.jtsec.manager.mapper.RoleMenuMapper;
import com.jtsec.manager.pojo.model.Role;
import com.jtsec.manager.pojo.model.RoleMenuKey;
import com.jtsec.manager.pojo.vo.RoleVo;
import com.jtsec.manager.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RoleMenuMapper roleMenuMapper;

	@Override
	public ServiceResult<Role> selectRoleById (Integer id) {

		Role role = roleMapper.selectRoleById (id);
		return new ServiceResult<> (role, true);
	}

	@Override
	public Set<String> getRoles (Integer userId) {
		List<Role> perms = roleMapper.selectRolesByUserId (userId);

		Set<String> permsSet = new HashSet<> ();

		for (Role perm : perms) {
			if (StringUtils.isNotNull (perms)) {
				permsSet.addAll (Arrays.asList (perm.getRoleKey ().trim ().split (",")));
			}
		}
		return permsSet;
	}

	/**
	 * @Description: 删除角色对应菜单
	 * @author NovLi
	 * @date 2018/8/21 13:44
	 */
	@Override
	@Transactional (propagation = Propagation.REQUIRED, rollbackFor = JtsecException.class)
	public ServiceResult<Boolean> delRole (List<Integer> roleIdList) {
		ServiceResult<Boolean> serviceResult = new ServiceResult<> ();
		if (roleIdList.isEmpty ()) {
			return new ServiceResult<> (false, "还没勾选数据呢 ~ ~");
		}
		Integer delRoleMenuNum = roleMenuMapper.delBatch (roleIdList);
		Integer delRoleNum = roleMapper.delBatch (roleIdList);
		if (delRoleNum == 0) {
			return new ServiceResult<> (false, "失败了, 后台没有要删除的数据哦 ~ ~");
		}
		return new ServiceResult<> (true, "删除成功喽 ~ ~");
	}

	/**
	 * @Description: 变更角色对应菜单信息
	 * @author NovLi
	 * @date 2018/8/21 10:49
	 */
	@Override
	@Transactional (propagation = Propagation.REQUIRED, rollbackFor = JtsecException.class)
	public ServiceResult<Boolean> editRole (RoleVo roleVo) {
		List<Integer> menuIdList = roleVo.getMenuIdList ();
		Integer result, results;
		ServiceResult<Boolean> serviceResult = new ServiceResult<> ();
		Role role = (Role) DataConvert.convertVoToEntity (new Role (), roleVo);
		if (role.getRoleId () == null) {
			result = roleMapper.insert (role);
			results = editRoleMenu (menuIdList, role, "save");
		} else {
			result = roleMapper.updateByPrimaryKeySelective (role);
			results = editRoleMenu (menuIdList, role, "edit");
		}
		if (result != 0 && results != 0)
			serviceResult.setSuccess (true);
		else
			serviceResult.setSuccess (false);
		return serviceResult;
	}

	/**
	 * @Description: 修改角色对应菜单
	 * @author NovLi
	 * @date 2018/8/21 10:38
	 */
	private Integer editRoleMenu (List<Integer> menuIdList, Role role, String variable) {
		Integer result = null;
		List<RoleMenuKey> roleMenuKeyList = new ArrayList<> ();
		if ("edit".equals (variable)) {
			List<Integer> roleIdList = new ArrayList<> ();
			roleIdList.add (role.getRoleId ());
			Integer delUserRoleNum = roleMenuMapper.delBatch (roleIdList);
		}
		for (Integer menuId : menuIdList) {
			roleMenuKeyList.add (new RoleMenuKey (role.getRoleId (), menuId));
		}
		result = roleMenuMapper.insertBatch (roleMenuKeyList);
		return result;
	}

	/**
	 * @Description: 查询角色
	 * @author NovLi
	 * @date 2018/8/21 10:48
	 */
	@Override
	public ServiceResult<List<Role>> selectRoleByVarible (RoleVo roleVo) {
		Map<String, Object> map = new HashMap<> ();

		ServiceResult<List<Role>> serviceResult = new ServiceResult<> ();

		if (roleVo != null) {
			map = DataConvert.ConvertDataToMapWithoutException (roleVo);
		}
		if (roleVo.getPage () != null && roleVo.getRow () != null) {
			PageHelper.startPage (roleVo.getPage (), roleVo.getRow ());
			List<Role> roleList = roleMapper.selectRoleByVarible (map);
			PageInfo<Role> info = new PageInfo<> (roleList);
			serviceResult.setPageInfo (info);
		} else if (roleVo.getPage () == null && roleVo.getRow () == null) {
			List<Role> roleList = roleMapper.selectRoleByVarible (map);
			serviceResult.setData (roleList);
		}
		return serviceResult;
	}
}
