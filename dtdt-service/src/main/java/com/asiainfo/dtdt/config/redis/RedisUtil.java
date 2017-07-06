package com.asiainfo.dtdt.config.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * Description: redis 工具类
 * Date: 2017年7月3日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Component 
public class RedisUtil {

	@Resource
	private JedisPool pool;

	public JedisPool getPool()
	{
		return pool;
	}

	public boolean exist(String key)
	{
		Jedis jedis = pool.getResource();
		boolean isOk = jedis.exists(key);
		jedis.close();
		return isOk;
	}

	public boolean hexist(String hkey, String key)
	{
		Jedis jedis = pool.getResource();
		boolean isOk = jedis.hexists(hkey, key);
		jedis.close();
		return isOk;
	}

	public void hdel(String hkey, String key)
	{
		Jedis jedis = pool.getResource();
		jedis.hdel(hkey, key);
		jedis.close();
	}

	public void hset(String hkey, String key, String value)
	{
		Jedis jedis = pool.getResource();
		jedis.hset(hkey, key, value);
		jedis.close();
	}

	public void set(String key, String value)
	{
		Jedis jedis = pool.getResource();
		jedis.set(key, value);
		jedis.close();
	}
	
	public void set(String key, String value, int expire)
	{
		Jedis jedis = pool.getResource();
		jedis.setex(key, expire, value);
		jedis.close();
	}

	public String get(String key)
	{
		Jedis jedis = pool.getResource();
		String value = jedis.get(key);
		jedis.close();
		return value;

	}

	public void rename(String oldKey, String newKey)
	{
		Jedis jedis = pool.getResource();
		jedis.rename(oldKey, newKey);
		jedis.close();
	}

	public Set<String> hkeys(String hkey)
	{
		Jedis jedis = pool.getResource();
		Set<String> hkeys = jedis.hkeys(hkey);
		jedis.close();
		return hkeys;
	}

	public void del(String hkey)
	{
		Jedis jedis = pool.getResource();
		jedis.del(hkey);
		jedis.close();
	}

	public String hget(String hkey, String key)
	{
		Jedis jedis = pool.getResource();
		String result = jedis.hget(hkey, key);
		jedis.close();
		return result;
	}

	public List<String> lrangeAll(String key)
	{
		return lrange(key, 0, -1);
	}

	public List<String> lrange(String key, long start, long end)
	{
		Jedis jedis = pool.getResource();
		List<String> result = jedis.lrange(key, start, end);
		jedis.close();
		return result;
	}

	public Map<String, String> hgetAll(String hkey)
	{
		Jedis jedis = pool.getResource();
		Map<String, String> result = jedis.hgetAll(hkey);
		jedis.close();
		return result;
	}

	public void lpush(String key, String value)
	{
		Jedis jedis = pool.getResource();
		jedis.lpush(key, value);
		jedis.close();
	}

	public void sadd(String key, String value)
	{
		Jedis jedis = pool.getResource();
		jedis.sadd(key, value);
		jedis.close();
	}

	public Set<String> smembers(String key)
	{
		Jedis jedis = pool.getResource();
		Set<String> result = jedis.smembers(key);
		jedis.close();
		return result;
	}

	public void hIncreasePositiveOne(String hkey, String key)
	{
		Jedis jedis = pool.getResource();
		jedis.hincrBy(hkey, key, 1);
		jedis.close();
	}

	public long hIncrease(String hkey, String key, int num)
	{
		Jedis jedis = pool.getResource();
		long result = jedis.hincrBy(hkey, key, num);
		jedis.close();
		return result;
	}

	public long hlen(String hkey)
	{
		Jedis jedis = pool.getResource();
		long size = jedis.hlen(hkey);
		jedis.close();
		return size;
	}

	public boolean setnx(String key, String value)
	{
		Jedis jedis = pool.getResource();
		long lock = jedis.setnx(key, value);
		jedis.close();
		return lock == 1;
	}

	public void delKey(String key)
	{
		Jedis jedis = pool.getResource();
		jedis.del(key);
		jedis.close();
	}

	public void expire(String key, int seconds)
	{
		Jedis jedis = pool.getResource();
		jedis.expire(key, seconds);
		jedis.close();
	}
}
