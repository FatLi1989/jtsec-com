package com.jtsec.common.util.enums;

import lombok.Getter;

/**
 * @author NovLi
 * @Title:
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2019/1/25 15:47
 */
@Getter
public enum DBEnum {

	url("jdbc:mysql://192.168.18.158:3306/jtsec_log?useUnicode=true&characterEncoding=utf-8") ,
	username("root"),
	password("root"),
	driver("com.mysql.cj.jdbc.Driver");

	public String code;

	DBEnum (String code) {
		this.code = code;
	}
}
