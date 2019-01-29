package com.jtsec.manager.service;

import redis.clients.jedis.Jedis;
import java.util.Set;

/**
 * 　* @Description: redis Service
 * 　* @author NovLi
 * 　* @date 2018/7/15 22:31
 *
 */
public interface IRedisService {

    public Jedis getResource ();

    public void setKeyAndValue (String key, Object value, Integer second, Integer index);

    public Object getValueByKey (String key, Integer index);

    void delByKey (String key, Integer index);

    Set<byte[]> getKeys (String shiro_session_prefix, Integer index);

}
