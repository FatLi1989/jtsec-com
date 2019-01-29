package com.jtsec.manager.service.impl;

import com.jtsec.manager.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 　* @Description: redis serviceImpl
 * 　* @author NovLi
 * 　* @date 2018/7/15 22:23
 */
@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {

	@Autowired
	private JedisPool jedisPool;

	/**
	 * 　* @Description: 获取jedis Pool
	 * 　* @author NovLi
	 * 　* @date 2018/7/15 22:23
	 */
	@Override
	public Jedis getResource () {
		log.info ("获取redis-pool连接");
		return jedisPool.getResource ();
	}

	/**
	 * 　* @Description: 连接放回连接池
	 * 　* @author NovLi
	 * 　* @date 2018/7/15 22:24
	 */
	@SuppressWarnings ("deprecation")
	public void returnResource (Jedis jedis, Boolean isBroken) {
		if (jedis != null) {
			if(isBroken){
				log.info ("异常释放redis-pool连接");
				jedisPool.returnBrokenResource (jedis);
			}
			else {
				log.info ("正常释放redis-pool连接");
				jedisPool.returnResourceObject (jedis);
			}
		}
	}

	/**
	 * 　* @Description: String类型 set
	 * 　* @author NovLi
	 * 　* @date 2018/7/15 22:28
	 */
	@Override
	public void setKeyAndValue (String key, Object value, Integer index, Integer second) {
		Boolean isBroken = false;
		Jedis jedis = getResource ();
		try {
			jedis.select (index);
			jedis.set (key.getBytes (), SerializationUtils.serialize ((Serializable) value));
			jedis.expire (key.getBytes (), second);
		} catch (Exception e) {
			isBroken = true;
			throw e;
		} finally {
			returnResource (jedis, isBroken);
		}
	}

	/**
	 * 　* @Description: 获取对应库所有的key
	 * 　* @author NovLi
	 * 　* @date 2018/7/16 0:23
	 */
	@Override
	public Set<byte[]> getKeys (String shiro_session_prefix, Integer index) {
		Boolean isBroken = false;
		Set<byte[]> keys = new HashSet<> ();
		Jedis jedis = getResource ();
		try {
			jedis.select (index);
			keys = jedis.keys ((shiro_session_prefix + "*").getBytes ());
			log.info ("getKeys"+keys);
		} catch (Exception e) {
			isBroken = true;
			throw e;
		} finally {
			returnResource (jedis, isBroken);
		}
		return keys;
	}
	/**
	 * 　* @Description: 用key 删除value
	 * 　* @author NovLi
	 * 　* @date 2018/7/16 0:16
	 */
	@Override
	public void delByKey (String key, Integer index) {
		{
			Boolean isBroken = false;
			Jedis jedis = getResource ();
			try {
				jedis.select (index);
				jedis.del (key.getBytes ());
			} catch (Exception e) {
				isBroken = true;
				throw e;
			} finally {
				returnResource (jedis, isBroken);
			}
		}
	}

	/**
	 * 　* @Description: String 类型 key获取value
	 * 　* @author NovLi
	 * 　* @date 2018/7/15 22:28
	 */
	@Override
	public Object getValueByKey (String key, Integer index) {
		Boolean isBroken = false;
		Jedis jedis = getResource ();
		try {
			jedis.select (index);
			byte[] result = jedis.get (key.getBytes ());
			if (result != null)
				return SerializationUtils.deserialize (result);
			else
				return null;
		} catch (Exception e) {
			isBroken = true;
			throw e;
		} finally {
			returnResource (jedis, isBroken);
		}
	}
}
