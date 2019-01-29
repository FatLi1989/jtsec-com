package com.jtsec.common.util.date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName jtsec
 * @Description: TODO
 * @date 2018/10/2617:32
 */
@Data
@EqualsAndHashCode
public class MessageEntity {

	private String title;
	private String body;

	@Override
	public String toString() {
		return "MessageEntity{" +
				"title='" + title + '\'' +
				", body='" + body + '\'' +
				'}';
	}
}
