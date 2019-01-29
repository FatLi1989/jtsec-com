package com.jtsec.manager.shiro.config;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/7/18 17:26
 */
public class CustomShiroHttpServletResponse extends ShiroHttpServletResponse {

	public CustomShiroHttpServletResponse (HttpServletResponse wrapped, ServletContext context, ShiroHttpServletRequest request) {
		super(wrapped, context, request);
	}
	@Override
	protected String toEncoded(String url, String sessionId) {
		if ((url == null) || (sessionId == null))
			return (url);
		String path = url;
		String query = "";
		String anchor = "";
		int question = url.indexOf('?');
		if (question >= 0) {
			path = url.substring(0, question);
			query = url.substring(question);
		}
		int pound = path.indexOf('#');
		if (pound >= 0) {
			anchor = path.substring(pound);
			path = path.substring(0, pound);
		}
		StringBuilder sb = new StringBuilder(path);
		//重写toEncoded方法，注释掉这几行代码就不会再生成JESSIONID了。
//        if (sb.length() > 0) { // session id param can't be first.
//            sb.append(";");
//            sb.append(DEFAULT_SESSION_ID_PARAMETER_NAME);
//            sb.append("=");
//            sb.append(sessionId);
//        }
		sb.append(anchor);
		sb.append(query);
		return (sb.toString());
	}
}
