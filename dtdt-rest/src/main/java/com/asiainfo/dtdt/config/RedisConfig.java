package com.asiainfo.dtdt.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ConfigurationProperties(prefix = "jedis.pool")
@Data
public class RedisConfig {

	/**
	 * 连接池配置
	 */
	private String host;
	private int port;
	private int maxIdle;
	private int maxTotal;
	private int maxWaitMillis;
	
	@Primary
    @RefreshScope
    @Bean
    public JedisPool pool()
    {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		return new JedisPool(config, host, port);
    }
	
}
