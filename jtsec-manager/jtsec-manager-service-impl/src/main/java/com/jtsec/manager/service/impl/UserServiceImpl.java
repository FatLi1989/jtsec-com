package com.jtsec.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.common.util.enums.ExceptionEnum;
import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.mapper.UserMapper;
import com.jtsec.manager.mapper.UserRoleMapper;
import com.jtsec.manager.pojo.model.User;
import com.jtsec.manager.pojo.model.UserRoleKey;
import com.jtsec.manager.pojo.vo.UserVo;
import com.jtsec.manager.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @ProjectName manager-parent
 * @date 2018/7/1016:23
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper usersMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;

	/**
	 * 　* @Description: 删除用户和对应角色信息
	 * 　* @author NovLi
	 * 　* @date 2018/8/19 10:16
	 */
	@Override
	@Transactional (propagation = Propagation.REQUIRED, rollbackFor = JtsecException.class)
	public ServiceResult delUser (List userIdList) {
		if (userIdList.isEmpty ()) {
			return 	new ServiceResult<> (false,"还没勾选数据呢 ~ ~");
		}
		Integer delUserNum = usersMapper.delBatch (userIdList);
		Integer delUserRoleNum = userRoleMapper.delBatch (userIdList);
		if (delUserNum == 0) {
			return 	new ServiceResult<> (false,"失败了, 后台没有要删除的数据哦 ~ ~");
		}
		return 	new ServiceResult<> (true,"删除成功喽 ~ ~");
	}

	/**
	 * 　* @Description: 增加和修改用户
	 * 　* @author NovLi
	 * 　* @date 2018/8/18 20:57
	 */
	@Override
	@Transactional (propagation = Propagation.REQUIRED, rollbackFor = JtsecException.class)
	public ServiceResult<Boolean> editUser (UserVo userVo) {
		List<Integer> roleIdList = userVo.getRoleIdList ();
		Integer result, results;
		ServiceResult<Boolean> serviceResult = new ServiceResult<> ();
		User user = (User) DataConvert.convertVoToEntity (new User (), userVo);
		if (user.getUserId () == null) {
			result = usersMapper.insert (user);
			results = editUserRole (roleIdList, user, "save");
		} else {
			result = usersMapper.updateByPrimaryKeySelective (user);
			results = editUserRole (roleIdList, user, "edit");
		}
		if (result != 0 && results != 0)
			serviceResult.setSuccess (true);
		return serviceResult;
	}

	/**
	 * 　* @Description: 修改角色
	 * 　* @author NovLi
	 * 　* @date 2018/8/18 20:58
	 */
	private Integer editUserRole (List<Integer> roleIdList, User user, String variable) {
		Integer result = null;
		List<UserRoleKey> userRoleKeyList = new ArrayList<> ();
		if ("edit".equals (variable)) {
			List<Integer> userIdList = new ArrayList<> ();
			userIdList.add (user.getUserId ());
			Integer delUserRoleNum = userRoleMapper.delBatch (userIdList);
		}
		for (Integer roleId : roleIdList) {
			userRoleKeyList.add (new UserRoleKey (user.getUserId (), roleId));
		}
		result = userRoleMapper.insertBatch (userRoleKeyList);
		return result;
	}

	@Override
	public ServiceResult<User> selectUserById (Integer id) {
		User user = usersMapper.selectUser (id);
		return new ServiceResult<> (user, true);
	}

	@Override
	public User getUserByLoginName (String loginName) {
		User user = usersMapper.getUserByLoginName(loginName);
		if (user == null) {
			throw new JtsecException (ExceptionEnum.getMsgs (3));
		}
		return user;
	}

	/**
	 * 　* @Description: 查询全部用户
	 * 　* @author NovLi
	 * 　* @date 2018/8/18 20:58
	 */
	@Override
	public ServiceResult<List<User>> selectUser (UserVo userVo) {
		Map<String, Object> map = new HashMap<> ();

		ServiceResult<List<User>> serviceResult = new ServiceResult<> ();
		List<User> userList = new ArrayList<User> ();

		if (userVo != null) {
			map = DataConvert.ConvertDataToMapWithoutException (userVo);
		}
		if (userVo.getPage () != null && userVo.getRow () != null) {
			PageHelper.startPage (userVo.getPage (), userVo.getRow ());
			userList = usersMapper.selectUsers (map);
			PageInfo<User> info = new PageInfo<> (userList);
			serviceResult.setPageInfo (info);
		} else if (userVo.getPage () == null && userVo.getRow () == null) {
			userList = usersMapper.selectUsers (map);
			serviceResult.setData (userList);
		}
		return serviceResult;
	}

	/**
	 * 　* @Description: 通过变量查询用户
	 * 　* @author NovLi
	 * 　* @date 2018/8/18 20:58
	 */
	@Override
	public User queryUserbyVarible (Map<String, Object> map) {

		List<User> userList = usersMapper.queryUserbyVarible (map);
		if (!userList.isEmpty ())
			return userList.get (0);
		else
			throw new JtsecException (ExceptionEnum.SelectUserByVarible.getCode ());
	}
}
