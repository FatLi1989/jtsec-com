package com.jtsec.common.exception.handler;

import com.jtsec.common.exception.JtsecException;
import com.jtsec.common.util.enums.ExceptionEnum;
import com.jtsec.common.util.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author NovLi
 * @Title: 全局异常处理
 * @ProjectName database_parent
 * @date 2018/7/915:18
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
	@ExceptionHandler (value = JtsecException.class)
	public HttpResponse ExceptionHandler (Exception e) {
		HttpResponse httpResponse = new HttpResponse ();

		if (e instanceof JtsecException) {
			httpResponse.setCode (((JtsecException) e).getCode ());
			httpResponse.setMeg (ExceptionEnum.getMsgs (((JtsecException) e).getCode ()));
		} else if (e instanceof Exception) {
			httpResponse.setCode (1);
			httpResponse.setMeg (e.getMessage ());
		}
		return httpResponse;
	}
}
