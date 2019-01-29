package com.jtsec.manager.service;

import com.jtsec.common.util.service.ServiceResult;
import com.jtsec.manager.pojo.model.User;
import com.jtsec.manager.pojo.vo.UserVo;
import java.util.List;
import java.util.Map;

/**
 * @author NovLi
 * @ProjectName manager-parent
 * @date 2018/7/1016:22
 */
public interface IUserService {

	User queryUserbyVarible (Map<String, Object> map);

	ServiceResult<List<User>> selectUser (UserVo userVo);

    ServiceResult<Boolean> editUser (UserVo userVo);

    ServiceResult delUser (List userIdList);

	ServiceResult<User> selectUserById (Integer id);

	User getUserByLoginName (String username);
}
