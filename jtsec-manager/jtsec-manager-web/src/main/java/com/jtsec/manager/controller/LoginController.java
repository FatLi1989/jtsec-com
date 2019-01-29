package com.jtsec.manager.controller;

import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.enums.ExceptionEnum;
import com.jtsec.common.util.response.HttpResponse;
import com.jtsec.common.util.salt.Salt;
import com.jtsec.manager.pojo.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
	/**
	 * @Description: 登入
	 * @author NovLi
	 * @date 2018/8/13 17:26
	 */
	@PostMapping (value = "login")
	public HttpResponse Login (UserVo user) {

		HttpResponse httpResponse = new HttpResponse ();

		Subject subject = SecurityUtils.getSubject ();

		UsernamePasswordToken token = new UsernamePasswordToken (
				user.getLoginName (),
				Salt.encryptPassword (
						user.getLoginName (),
						user.getPassword (),
						Salt.salt),
				user.getRememberMe ());

		try {
			subject.login (token);
			httpResponse.setCode (100);
			httpResponse.setData (subject.getSession ().getId ());
			httpResponse.setMeg ("登陆成功");
		} catch (IncorrectCredentialsException e) {
			throw new JtsecException (ExceptionEnum.IncorrectCredentialsException.getCode ());
		} catch (LockedAccountException e) {
			throw new JtsecException (ExceptionEnum.LockedAccountException.getCode ());
		} catch (AuthenticationException e) {
			throw new JtsecException (ExceptionEnum.AuthenticationException.getCode ());
		}
		return httpResponse;
	}

	/**
	 * @Description: 未登录跳转
	 * @author NovLi
	 * @date 2018/8/13 17:25
	 */
	@GetMapping (value = "unLogin")
	public void unLogin () {
		throw new JtsecException (ExceptionEnum.PleaseSignIn.getCode ());
	}

	/**
	 * @Description: 无权限跳转
	 * @author NovLi
	 * @date 2018/8/13 17:25
	 */
	@GetMapping (value = "unAuthor")
	public void unAuthor () {
		throw new JtsecException (ExceptionEnum.NoPermission.getCode ());
	}

	/**
	 * @Description: 登出
	 * @author NovLi
	 * @date 2018/8/13 17:26
	 */
	@GetMapping (value = "loginOut")
	public HttpResponse loginOut () {
		HttpResponse htpResponse = new HttpResponse ();
		try {
			SecurityUtils.getSubject ().logout ();
		} catch (Exception e) {
			throw new JtsecException (ExceptionEnum.LoginOutError.getCode ());
		}
		htpResponse.setMeg ("已经退出登录了");
		return htpResponse;
	}
}
