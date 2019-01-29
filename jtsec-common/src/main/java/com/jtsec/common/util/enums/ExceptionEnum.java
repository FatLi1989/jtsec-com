package com.jtsec.common.util.enums;
 /**
 　* @Description: Exception Enum
 　* @author NovLi
 　* @date 2018/7/15 22:50 
 　*/
public enum ExceptionEnum {

	 IncorrectCredentialsException (1, "密码错误"), LockedAccountException (2, "登录失败，该用户已被冻结"),

	 AuthenticationException (3, "该用户不存在"), MissLoginName (4, "输入的登录名称为空"),

	 LoginUserMisMatchesCurrentUser (5, "请退出重新登录"), PleaseSignIn (6, "请先登录哦"),

	 NoPermission (7, "没有权限哦"), LoginOutError (8, "退出异常了哦"),

	 SystemError(9, "系统后台异常"), SelectUserByVarible(10, "用户查询模块出现异常"),

	 DataConvertError(11, "数据转换异常"),  ThingError(12, "事物异常了哦"),

	 SchedulerException(13, "定时任务出错了");

	 private Integer code;

	 private String msg;

	 public String getMsg () {
		 return msg;
	 }

	 public Integer getCode () {
		 return code;
	 }

	 public void setCode (Integer code) {
		 this.code = code;
	 }

	 public void setMsg (String msg) {
		 this.msg = msg;
	 }

	 private ExceptionEnum (Integer code, String msg) {
		 this.code = code;
		 this.msg = msg;

	 }

	 public static String getMsgs (Integer value) {
		 String msg = null;
		 for (ExceptionEnum commonEnum : ExceptionEnum.values ())
			 if (commonEnum.getCode ().equals (value)) {
				 msg = commonEnum.getMsg ();
				 break;
			 }
		 return msg;
	 }
}
