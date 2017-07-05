package com.asiainfo.dtdt.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;

import javax.annotation.Resource;

import lombok.extern.log4j.Log4j2;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.constants.RedisKey;
import com.asiainfo.dtdt.common.util.SignUtil;
import com.asiainfo.dtdt.config.RedisUtil;
import com.asiainfo.dtdt.entity.ResponseCode;
import com.asiainfo.dtdt.interfaces.IAuthoService;


/**
 * 
 * Description: 
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 * @author 
 */
 
@RestController
@Log4j2
@RequestMapping(value="/autho")
public class AuthoController {

	@Resource
	private IAuthoService authoService;
	
	@Resource//(name="redisObject")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Resource
	private RedisUtil redisUtil; 
	
    @RequestMapping(value = "getCode", method = POST)
    public JSONObject getSMSCode(@RequestBody JSONObject json) {
    	log.debug("directional data traffic!");
    	
    	JSONObject result = new JSONObject();
    	try
    	{
    		if(!json.containsKey("phone") || StringUtils.isEmpty(json.getString("phone")))
        	{
    			result.put("code", "10000");
    			result.put("msg", "手机号号码为空！");
    			return result;
        	}
    		
    		StringBuilder sb = new StringBuilder(RedisKey.SMSC);
    		sb.append("_").append(json.getString("partnerCode"));
    		sb.append("_").append(json.getString("appkey"));
    		sb.append("_").append(json.getString("phone"));
    		int code = (int)((Math.random()*9 + 1) * 100000);
    		if(redisUtil.hexist(sb.toString(), "cd"))
    		{
    			int counts = Integer.valueOf(redisUtil.hget(sb.toString(), "ts"));
    			if(counts >= 5)
    			{
    				result.put("code", "30003");
    				result.put("msg", "获取超过5次");
    				return result;
    			}
    			
    			redisUtil.hset(sb.toString(), "ts", String.valueOf(counts++));
    			// 时间超过配置限制默认（5分钟） 重新生成，否则返回原来的
    			result.put("smsCode", redisUtil.hget(sb.toString(), "cd"));
    		}
    		else
    		{
    			redisUtil.hset(sb.toString(), "cd", String.valueOf(code));
    			redisUtil.hset(sb.toString(), "t", new Date().toString());
    			result.put("smsCode", code);
    		}
    		
    		result.put("code", "00000");
			result.put("msg", "成功");
			
    		return result;
    	}
    	catch (Exception e)
    	{
    		result.put(ResponseCode.CODE, ResponseCode.COMMON_ERROR_CODE);
			result.put(ResponseCode.MSG, "系统异常");
			log.error("getSMSCode error!", e);
    	}
    	return result;
    }
    
    public static void main(String[] ag)
    {
    	for(int i=0; i<1000; i++)
    	{
    		//System.out.println(new Random().nextInt(999999));
    		System.out.println((int)((Math.random()*9 + 1) * 100000));
    	}
    }
    
    @RequestMapping(value = "/test/getSign", method = POST)
    public String getSign(@RequestBody JSONObject json)
    {
    	return SignUtil.createSign(json, "UTF-8");
    }
	
}