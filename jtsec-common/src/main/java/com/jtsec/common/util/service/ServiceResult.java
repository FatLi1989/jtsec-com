package com.jtsec.common.util.service;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/8/1510:39
 */
@Data
public class ServiceResult<T> implements Serializable {

	private PageInfo pageInfo;

	private T data;

	private Boolean success;

	private String msg;

	public ServiceResult (PageInfo pageInfo, T data, Boolean success, String msg) {
		this.pageInfo = pageInfo;
		this.data = data;
		this.success = success;
		this.msg = msg;
	}

	public ServiceResult (T data, Boolean success) {
		this.data = data;
		this.success = success;
	}

	public ServiceResult (T data, Boolean success, String msg) {
		this.data = data;
		this.success = success;

		this.msg = msg;
	}

	public ServiceResult () {
	}

	public ServiceResult (Boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	/**
	 * @Description: 操作两次数据时使用
	 * @author NovLi
	 * @date 2018/11/19 13:24
	 */
	public static ServiceResult<Boolean> judgeServiceResult (Integer conditionF, Integer conditionS) {
		ServiceResult<Boolean> serviceResult = null;
		if (conditionF != null && conditionS != null)
			serviceResult = new ServiceResult<> (true, "操作成功");
		else if (conditionF == null || conditionS == null) {
			serviceResult = new ServiceResult<> (false, "操作失败");
		}
		return serviceResult;
	}
}
