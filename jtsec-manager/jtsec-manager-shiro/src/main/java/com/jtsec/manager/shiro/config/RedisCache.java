package com.jtsec.manager.shiro.config;

import com.jtsec.common.config.reids.RedisDBIndex;
import com.jtsec.manager.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Component
public class RedisCache<k, V> implements Cache<k, V> {

	@Autowired
	private IRedisService iRedisService;

	private final String JTSEC_CACHE = "jtsec_cache:";

	private String getKey (k k) {
		log.info ("cache getKey={}", k);
		if (k instanceof String) {
			return (JTSEC_CACHE + k);
		}
		return k.toString ();
	}

	@Override
	public V get (k k) throws CacheException {
		log.info ("cache getKey={}", k);
		V value = (V) iRedisService.getValueByKey (k.toString (), RedisDBIndex.SessionDb);
		if (value != null) return value;

		return null;
	}

	@Override
	public V put (k k, V v) throws CacheException {
		log.info ("cache putKey={}", k);
		String key = getKey (k);
		iRedisService.setKeyAndValue (key, v, RedisDBIndex.SessionDb, 1200);
		return v;
	}

	@Override
	public V remove (k k) throws CacheException {
		log.info ("cache removeKey={}", k);
		String key = getKey (k);
		V v = (V) iRedisService.getValueByKey (key, RedisDBIndex.SessionDb);
		iRedisService.delByKey (key, RedisDBIndex.SessionDb);
		if (v != null) {
			return v;
		}
		return null;
	}

	@Override
	public void clear () throws CacheException {

	}

	@Override
	public int size () {
		return 0;
	}

	@Override
	public Set<k> keys () {
		return null;
	}

	@Override
	public Collection<V> values () {
		return null;
	}
}
