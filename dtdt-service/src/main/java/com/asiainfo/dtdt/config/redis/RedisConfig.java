package com.asiainfo.dtdt.config.redis;

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
	
	

	/*
	@Bean(name = "jedis.pool")
	@Autowired
	public JedisPool jedisPool(
			@Qualifier("jedis.pool.config") JedisPoolConfig config,
			@Value("${jedis.pool.host}") String host,
			@Value("${jedis.pool.port}") int port)
	{
		return new JedisPool(config, host, port);
	}*/
	/*
	@Bean(name = "jedis.pool.config")
	public JedisPoolConfig jedisPoolConfig(
			@Value("${jedis.pool.config.maxTotal}") int maxTotal,
			@Value("${jedis.pool.config.maxIdle}") int maxIdle,
			@Value("${jedis.pool.config.maxWaitMillis}") int maxWaitMillis)
	{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		return config;
	} */
	      

	/*@Bean
	public KeyGenerator keyGenerator()
	{
		return new KeyGenerator() {

			@Override
			public Object generate(Object target, Method method,
					Object... params)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params)
				{
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}
	 @Bean
	    public KeyGenerator keyGenerator(@Qualifier(value = "defaultGeneratorKey") MyKeyGenerator myKeyGenerator) {
	        return (target, method, params) -> myKeyGenerator.generate(target, method.getName(), params);
	    }

	@SuppressWarnings("rawtypes")
	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate)
	{
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		//设置缓存过期时间
		//rcm.setDefaultExpiration(60);//秒
		return rcm;
	}

	@Bean
	@RefreshScope
	public JedisConnectionFactory jedisConnectionFactory()
	{
		if (pool == null)
			pool = new JedisPoolConfig();
		return new JedisConnectionFactory(pool);
	}

	@SuppressWarnings("unchecked")
	//@Bean(name = "redisCache")
	@RefreshScope
	public RedisTemplate<String, String> redisTemplate(
			RedisConnectionFactory factory)
	{
		StringRedisTemplate template = new StringRedisTemplate(factory);
		@SuppressWarnings("rawtypes")
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	//@Bean(name = "redisObject")
	@RefreshScope
	public RedisTemplate<String, Object> redisObject()
	{
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		//template.setConnectionFactory(jedisConnectionFactory());
		template.setKeySerializer(new StringRedisSerializer());
		return template;
	}*/
}
