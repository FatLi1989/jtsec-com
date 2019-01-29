package com.jtsec.manager.scheduler.util;

import com.jtsec.common.util.string.StringUtils;
import com.jtsec.manager.scheduler.Constants.ScheduleConstants;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 执行定时任务
 *
 * @author ruoyi
 */
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	private String params;


	public ScheduleRunnable (String beanName, String methodName, String params)
			throws NoSuchMethodException, SecurityException {
		try {
			this.target = Class.forName (ScheduleConstants.REFLECT + beanName).getConstructor ().newInstance ();
		} catch (InstantiationException e) {
			e.printStackTrace ();
		} catch (IllegalAccessException e) {
			e.printStackTrace ();
		} catch (InvocationTargetException e) {
			e.printStackTrace ();
		} catch (ClassNotFoundException e) {
			e.printStackTrace ();
		}
		this.params = params;

		if (StringUtils.isNotEmpty (params)) {
			this.method = target.getClass ().getDeclaredMethod (methodName, String.class);
		} else {
			this.method = target.getClass ().getDeclaredMethod (methodName);
		}
	}

	@Override
	public void run () {
		try {
			ReflectionUtils.makeAccessible (method);
			if (StringUtils.isNotEmpty (params)) {
				method.invoke (target, params);
			} else {
				method.invoke (target);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}

}
