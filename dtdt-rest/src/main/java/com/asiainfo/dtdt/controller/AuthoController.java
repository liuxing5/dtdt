package com.asiainfo.dtdt.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.util.PhoneUtil;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.common.util.SignUtil;
import com.asiainfo.dtdt.config.SMSContentConfig;
import com.asiainfo.dtdt.entity.ResponseCode;
import com.asiainfo.dtdt.entity.ResponseData;
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
	
	@Resource
	private RedisAssistant redis;
	
	@Resource
	private SMSContentConfig smsContentConfig;
	
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "getCode", method = RequestMethod.GET)
	public ResponseData<?> getSMSCode(HttpServletRequest request)
	{
		log.debug("directional data traffic!");
		
		getHeaderCommonData(request);
		JSONObject result = new JSONObject();
		String phone = request.getParameter("phone");
		try
		{
			if (StringUtils.isEmpty(phone))
			{
				return new ResponseData("10000", "手机号号码为空！");
			}
			
			if(!PhoneUtil.isCUCMobile(phone))
			{
				return new ResponseData("10000", "非联通号码！");
			}

			StringBuilder sb = new StringBuilder(RedisKey.SMSC);
			sb.append("_").append(headers.getString("partnerCode"));
			sb.append("_").append(headers.getString("appkey"));
			sb.append("_").append(phone);
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
			/*String smsvalidStr = redisUtil.get("smsc_v_t");
			if (StringUtils.isEmpty(smsvalidStr))
			{
				smsvalidStr = "5";
				redisUtil.set("smsc_v_t", "5");
			}
			redisUtil.set(sb.toString(), String.valueOf(code),
					Integer.valueOf(smsvalidStr) * 60);*/
			String smsvalidStr = redis.getStringValue("smsc_v_t");
			if (StringUtils.isEmpty(smsvalidStr))
			{
				smsvalidStr = "5";
				redis.setForever("smsc_v_t", "5");
			}
			redis.setValue(sb.toString(), String.valueOf(code), Long.valueOf(smsvalidStr), TimeUnit.MINUTES);

			String path = "";
			if (System.getProperty("os.name").startsWith("win") || System.getProperty("os.name").startsWith("Win")){
				path = AuthoController.class.getResource("/").getPath();
			}else{
				path = System.getProperty("user.dir");
			}

			log.info("configPath:{}", path);
			String content = smsContentConfig.getCodeContent();
			if(StringUtils.isEmpty(content))
			{
				content = "";
			}
			content = content.replace("{0}", String.valueOf(code));
			SGIPSendMSGUtil.CONF_PATH = path + File.separator + "sgip.properties";
			log.info("configPath:{}", SGIPSendMSGUtil.CONF_PATH);
			SGIPSendMSGUtil.sendMsg(phone, content);

			log.info("{}|{}|{}|{}",
					headers.getString("partnerCode"),
					headers.getString("appkey"), phone, content);

			//return result;
		} catch (Exception e)
		{
			result.put(ResponseCode.CODE, ResponseCode.COMMON_ERROR_CODE);
			result.put(ResponseCode.MSG, "系统异常");
			log.error("{}|{}|{}|{}|{}",
					headers.getString("partnerCode"),
					headers.getString("appkey"), phone,
					result.get("smsCode"), e);
			return new ResponseData(ResponseCode.CODE, ResponseCode.COMMON_ERROR_CODE);
		}
		return new ResponseData(ResponseCode.COMMON_SUCCESS_CODE, "成功");
	}
    
    
    @RequestMapping(value = "/test/getSign", method = POST)
    public String getSign(@RequestBody JSONObject json)
    {
    	log.info(json.toJSONString());
    	return SignUtil.createSign(json, "UTF-8");
    }
	
}