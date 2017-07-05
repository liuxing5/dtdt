package com.asiainfo.dtdt.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.constants.RedisKey;
import com.asiainfo.dtdt.common.util.SignUtil;
import com.asiainfo.dtdt.config.RedisUtil;
import com.asiainfo.dtdt.entity.ResponseCode;
import com.asiainfo.dtdt.interfaces.IAuthoService;
import com.huawei.insa2.util.SGIPSendMSGUtil;


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
public class AuthoController extends BaseController{

	@Resource
	private IAuthoService authoService;
	
	@Resource//(name="redisObject")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Resource
	private RedisUtil redisUtil; 
	
    @RequestMapping(value = "getCode", method = RequestMethod.GET)
	public JSONObject getSMSCode(HttpServletRequest request)
	{
		log.debug("directional data traffic!");

		getHeaderCommonData(request);
		JSONObject result = new JSONObject();
		String phone = request.getParameter("phone");
		try
		{
			if (StringUtils.isEmpty(phone))
			{
				result.put("code", "10000");
				result.put("msg", "手机号号码为空！");
				return result;
			}

			StringBuilder sb = new StringBuilder(RedisKey.SMSC);
			sb.append("_").append(headers.getString("partnerCode"));
			sb.append("_").append(headers.getString("appkey"));
			sb.append("_").append(headers.getString("phone"));
			int code = (int) ((Math.random() * 9 + 1) * 100000);

			/*
			 * if (redisUtil.hexist(sb.toString(), "cd")) {
			 * redisUtil.hset(sb.toString(), "ts", String.valueOf(counts++)); //
			 * 时间超过配置限制默认（5分钟） 重新生成，否则返回原来的 result.put("smsCode",
			 * redisUtil.hget(sb.toString(), "cd")); } else {
			 * redisUtil.hset(sb.toString(), "cd", String.valueOf(code));
			 * redisUtil.hset(sb.toString(), "t", new Date().toString());
			 * result.put("smsCode", code); }
			 */

			// 短信有效时间(单位分钟)
			String smsvalidStr = redisUtil.get("smsc_v_t");
			if (StringUtils.isEmpty(smsvalidStr))
			{
				smsvalidStr = "5";
				redisUtil.set("smsc_v_t", "5");
			}
			redisUtil.set(sb.toString(), String.valueOf(code),
					Integer.valueOf(smsvalidStr) * 60);

			SGIPSendMSGUtil.sendMsg(phone, String.valueOf(code));

			result.put("code", "00000");
			result.put("msg", "成功");
			//result.put("SmdCode", code);

			log.info("getsmscode", "{}|{}|{}",
					headers.getString("partnerCode"),
					headers.getString("appkey"), headers.getString("phone"));

			return result;
		} catch (Exception e)
		{
			result.put(ResponseCode.CODE, ResponseCode.COMMON_ERROR_CODE);
			result.put(ResponseCode.MSG, "系统异常");
			log.error("getSMSCode error!", "{}|{}|{}|{}|{}",
					headers.getString("partnerCode"),
					headers.getString("appkey"), headers.getString("phone"),
					result.get("smsCode"), e);
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