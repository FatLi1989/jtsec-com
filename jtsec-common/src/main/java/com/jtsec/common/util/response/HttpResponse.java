package com.jtsec.common.util.response;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author NovLi
 * @Description: 返回数据封装
 * @date 2018/6/2015:02
 */
@Data
public class HttpResponse<T> implements Serializable {

	private int code = 100;

	private String meg = "操作成功";

	private T data;

	private PageInfo pageInfo;

	public HttpResponse (int code, String meg, T data) {
		this.code = code;
		this.meg = meg;
		this.data = data;
	}

	public HttpResponse (int code, String meg) {
		this.code = code;
		this.meg = meg;
	}

	public HttpResponse () {
	}

	public HttpResponse (T data) {
		this.data = data;
	}

	@Override
	public String toString () {
		return "HttpReponse{" +
				"code=" + code +
				", meg='" + meg + '\'' +
				", data=" + data +
				'}';
	}
}
