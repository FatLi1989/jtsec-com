package com.jtsec.manager.shiro.config;

import com.jtsec.common.util.datahandle.DataConvert;
import com.jtsec.manager.pojo.model.User;
import com.jtsec.manager.service.IMenuService;
import com.jtsec.manager.service.IRoleService;
import com.jtsec.manager.service.IUserService;
import com.jtsec.manager.shiro.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/7/1010:41
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {


	@Autowired
	private IUserService iUserService;
	@Autowired
	private IRoleService iRoleService;
	@Autowired
	private IMenuService iMenuService;

	/**
	 * @param
	 * @return
	 * @throws
	 * @Description: 授权
	 * @author NovLi
	 * @date 2018/7/10 10:49
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo (PrincipalCollection principals) {
		Integer userId = ShiroUtil.getUser ().getUserId ();
		SimpleAccount simpleAccount = new SimpleAccount ();
		simpleAccount.setRoles (iRoleService.getRoles(userId));
		simpleAccount.setStringPermissions (iMenuService.selectPermsByUserId(userId));
		return simpleAccount;
	}

	/**
	 * @param token
	 * @throws
	 * @Description: 登陆
	 * @author NovLi
	 * @date 2018/7/10 10:51
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo (AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken userToken = (UsernamePasswordToken) token;
		User user = iUserService.getUserByLoginName(userToken.getUsername ());
		return new SimpleAuthenticationInfo (user, user.getPassword (), getName ());
	}
}
