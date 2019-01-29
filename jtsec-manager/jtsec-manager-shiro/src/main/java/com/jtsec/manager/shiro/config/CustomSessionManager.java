package com.jtsec.manager.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author NovLi
 * @Title: what
 * @ProjectName manager-parent
 * @Description: TODO
 * @date 2018/7/16 13:19
 */
@Slf4j
public class CustomSessionManager extends DefaultWebSessionManager {

    private static final String AUTHORIZATION = "Authorization";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public CustomSessionManager() {
        super();
    }
    /**
	 * @Description: 实现自定义sessionManager 为了减少查找session信息访问redis次数 重写这部分只是为了把session存到servletRequest容器中
	 * @author NovLi
	 * @date 2018/7/16 14:36
	 */
	@Override
	protected Session retrieveSession (SessionKey sessionKey) throws UnknownSessionException {

		Serializable sessionId = getSessionId (sessionKey);

		ServletRequest request = null;
		if (sessionKey instanceof WebSessionKey)
			request = ((WebSessionKey) sessionKey).getServletRequest ();

		if (request != null && sessionId != null) {
			Session session = (Session) request.getAttribute (sessionId.toString ());
			if (session != null)
				return session;
		}

		Session session = super.retrieveSession (sessionKey);

		if (request != null && sessionId != null)
			request.setAttribute (sessionId.toString (), session);

		return session;
	}
}