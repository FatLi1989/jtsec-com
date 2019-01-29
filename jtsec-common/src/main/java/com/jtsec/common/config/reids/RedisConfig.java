package com.jtsec.common.config.reids;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author NovLi
 * @Description: redis配置
 * @date 2018/7/1317:14
 */
@Data
@Slf4j
@Configuration
@EnableAutoConfiguration
public class RedisConfig {

    @Value ("${spring.redis.host}")
    private String host;
    @Value ("${spring.redis.port}")
    private int port;
    @Value ("${spring.redis.password}")
    private String password;
    @Value ("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdel;
    @Value ("${spring.redis.jedis.pool.min-idle}")
    private Integer minIdle;
    @Value ("${spring.redis.jedis.pool.max-wait}")
    private Integer maxWaitMillis;
    @Value ("${spring.redis.jedis.pool.max-total}")
    private Integer maxTotal;

    @Bean ("JedisPool")
    public JedisPool JedisPoolFactory(@Qualifier ("Config") GenericObjectPoolConfig poolConfig ){
        log.info("init RedisPool ...");
        JedisPool pool = new JedisPool(poolConfig, host, port, 1200,password);
        log.info("init RedisPool success");
        return pool;
    }

    @Bean ("Config")
    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdel);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
		config.setMaxTotal (maxTotal);
		config.setTestOnBorrow (true);
		config.setTestOnBorrow (true);
		config.setTestWhileIdle (true);

        return config;
    }
}
