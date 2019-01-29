package com.jtsec.manager.shiro.util;

import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.enums.ExceptionEnum;
import com.jtsec.manager.pojo.model.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import java.lang.reflect.InvocationTargetException;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/111:26
 */
public class ShiroUtil {

	public static Subject getSubject () {
		return SecurityUtils.getSubject ();
	}

	public static User getUser () {
		User user = new User ();
		Object obj = getSubject ().getPrincipal ();
		System.out.println (obj);
		try {
			BeanUtils.copyProperties (user, obj);
		} catch (IllegalAccessException e) {
			throw new JtsecException (ExceptionEnum.DataConvertError.getCode ());
		} catch (InvocationTargetException e) {
			throw new JtsecException (ExceptionEnum.DataConvertError.getCode ());
		}
		return user;
	}
}
