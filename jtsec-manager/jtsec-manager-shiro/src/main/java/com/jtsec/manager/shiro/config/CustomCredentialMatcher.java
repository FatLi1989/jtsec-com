package com.jtsec.manager.shiro.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author NovLi
 * @Title:
 * @ProjectName manager-parent
 * @Description: 密码校验
 * @date 2018/7/1013:35
 */
public class CustomCredentialMatcher extends SimpleCredentialsMatcher {
	/**
	 * @Description: 密码校验
	 * @param  token 用户传入值  info 数据库对应用户数据
	 * @return
	 * @throws
	 * @author NovLi
	 * @date 2018/7/10 13:43 
	 */
	@Override
	public boolean doCredentialsMatch (AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordToken userToken = (UsernamePasswordToken)token;
		String password = new String(userToken.getPassword ());
		String dbPassword = (String) info.getCredentials ();
		return this.equals (password, dbPassword);
	}
}
