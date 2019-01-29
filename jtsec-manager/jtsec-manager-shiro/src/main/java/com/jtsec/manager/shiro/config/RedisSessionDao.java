package com.jtsec.manager.shiro.config;

import com.jtsec.common.config.reids.RedisDBIndex;
import com.jtsec.manager.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author NovLi
 * @Description: 自定义sessionDao 继承CacheSessionDao 先去缓存中查询 没有的话在去操作redis
 * @date 2018/7/16 11:04
 */
@Slf4j
public class RedisSessionDao extends CachingSessionDAO {

	@Autowired
	private IRedisService iRedisService;

	public final String SHIRO_SESSION_PREFIX_KEY = "Jtsec";

	public String getKey (Serializable key) {
		return SHIRO_SESSION_PREFIX_KEY + key;
	}

	public void saveSession (Session session) {
		if (session != null && session.getId () != null) {
			log.info ("saveSession session", session.toString ());
			String key = session.getId ()+ "";
			iRedisService.setKeyAndValue (key, session, RedisDBIndex.SessionDb, 1800000);
		}
	}

	/**
	 * @Description: 保存key sessionId value session
	 * @author NovLi
	 * @date 2018/7/16 13:35
	 */
	@Override
	protected Serializable doCreate (Session session) {
		Serializable sessionId = generateSessionId (session);
		log.info ("doCreateSession sessionId={}", sessionId.toString ());
		assignSessionId (session, getKey(sessionId));
		saveSession (session);
		return getKey (sessionId);
	}

	/**
	 * @Description: 获取session
	 * @author NovLi
	 * @date 2018/7/16 13:36
	 */
	@Override
	protected Session doReadSession (Serializable sessionId) {
		if (sessionId == null) return null;

		log.info ("doReadSession sessionId={}", sessionId.toString ());
		Session value = (Session) iRedisService.getValueByKey (getKey (sessionId), RedisDBIndex.SessionDb);
		if (value == null) return null;
		return value;
	}
    /**
     * @Description: 更新key vakue
     * @author NovLi
     * @date 2018/7/16 13:36
     */
    @Override
    protected void doUpdate(Session session) {
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return; //如果会话过期/停止 没必要再更新了
        }
        log.info ("updateSession session={}", session.toString ());
        saveSession (session);
    }

    /**
	 * @Description: key 删除 value
	 * @author NovLi
	 * @date 2018/7/16 13:37
	 */
    @Override
    protected void doDelete(Session session) {
        log.info ("deleteSession session={}", session.toString ());
        if (session == null || session.getId () == null) return;
        iRedisService.delByKey (session.getId ()+"", RedisDBIndex.SessionDb);
    }

    /**
	 * @Description: 获取全部没过期的session 会话验证机制
	 * @author NovLi
	 * @date 2018/7/16 13:37
	 */
	@Override
	public Collection<Session> getActiveSessions () {
		log.info (this.getClass ().getName () + "getActiveSessions");
		Set<byte[]> keys = iRedisService.getKeys (SHIRO_SESSION_PREFIX_KEY, RedisDBIndex.SessionDb);
		Set<Session> sessions = new HashSet<Session> ();
		if (!CollectionUtils.isEmpty (keys)) {
			for (byte[] key : keys) {
				Session session = (Session) iRedisService.getValueByKey (key.toString (), RedisDBIndex.SessionDb);
				sessions.add (session);
			}
		}
		return sessions;
	}
}
