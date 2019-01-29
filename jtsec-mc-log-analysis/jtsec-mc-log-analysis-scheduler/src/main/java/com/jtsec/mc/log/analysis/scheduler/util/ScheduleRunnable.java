package com.jtsec.mc.log.analysis.scheduler.util;

import com.jtsec.common.util.spring.SpringUtils;
import com.jtsec.common.util.string.StringUtils;
import org.springframework.util.ReflectionUtils;
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

		this.target = SpringUtils.getBean(beanName);
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
