package com.jtsec.common.exception;

import lombok.Data;

/**
 * @Description: 自定义异常
 * @author NovLi
 * @date 2018/8/15 15:16 
 */
@Data
public class JtsecException extends RuntimeException {

	public Integer code;

	public JtsecException (Integer code, String msg) {
		super (msg);
		this.code = code;
	}

	public JtsecException (String msg) {
		super (msg);
	}

	public JtsecException (Integer code) {
		this.code = code;
	}

	public JtsecException () {

	}

}

