package com.asiainfo.dtdt.service.impl.order;

import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.common.util.DateUtil;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.config.sms.SMSContentConfig;
import com.asiainfo.dtdt.entity.PartnerOrderResources;
import com.asiainfo.dtdt.service.IOrderResourceService;
import com.asiainfo.dtdt.service.IPartnerOrderResourcesService;
import com.huawei.insa2.util.SGIPSendMSGUtil;

/**
 * 
 * Description: 订购资源
 * Date: 2017年7月10日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Service
@Log4j2
public class OrderResourceServiceImpl implements IOrderResourceService{

	@Resource
	private RedisAssistant redisAssistant;
	
	@Resource
	private IPartnerOrderResourcesService pORService;
	
	@Resource
	private SMSContentConfig smsContentConfig;
	
	@Override
	public String checkCounts(String partnerCode)
	{
		String key = RedisKey.PARTNER_OR_KEY_PREFIX + partnerCode;

		/*if (!redisSingle.exist(key))
		{
			
			PartnerOrderResources por = pORService.loadByPartnerCode(partnerCode);
			if(null != por)
			{
				redisSingle.set(key, por.getPreCount().toString());
				log.info("无可用次数！key不存在，key:{},load成功，预存次数{}", key, por.getPreCount());
			}
			else
			{
				log.info("无可用次数！key不存在，key:{},load无记录", key);
				return ReturnUtil.returnJsonInfo(Constant.NO_ORDER_RESOURCE_CODE,
						Constant.NO_ORDER_RESOURCE_MSG, null);
			}
		Long count = Long.valueOf(redisSingle.get(key));
		if (count == 0l)
		{
			log.info("订购次数已用完，key：{}", key);
			return ReturnUtil.returnJsonInfo(Constant.NO_ORDER_RESOURCE_CODE,
					Constant.NO_ORDER_RESOURCE_MSG, null);
		}
		log.info("使用一次key:{}", key);
		redisSingle.incrBy(key, -1);*/
		
		/*集群方式*/
		if (org.springframework.util.StringUtils.isEmpty(redisAssistant
				.getStringValue(key)))
		{

			PartnerOrderResources por = pORService
					.loadByPartnerCode(partnerCode);
			if (null != por)
			{
				Long canUsedCount = por.getPreCount() - por.getUseCount();
				if (!canUsedCount.equals(0l))
				{
					redisAssistant.setForever(key, canUsedCount.toString());
					log.info("无可用次数！key不存在，key:{},load成功，预存次数{},告警阈值{},可用次数{}",
							key, por.getPreCount(), por.getWarnThreshold(),
							canUsedCount);
				} else
				{
					log.info("无可用次数！key不存在，key:{},load成功，但是无可用次数", key);
					return ReturnUtil.returnJsonInfo(
							Constant.NO_ORDER_RESOURCE_CODE,
							Constant.NO_ORDER_RESOURCE_MSG, null);
				}

			} else
			{
				log.info("无可用次数！key不存在，key:{},load无记录", key);
				return ReturnUtil.returnJsonInfo(
						Constant.NO_ORDER_RESOURCE_CODE,
						Constant.NO_ORDER_RESOURCE_MSG, null);
			}

		}
		
		Long warThreshold = 0l;
		String hkey = RedisKey.PARTNER_OR_WARN_KEY_PREFIX + partnerCode;
		
		if(redisAssistant.hexist(hkey, RedisKey.PARTNER_OR_WARN_KEY_WARNTHRESHOLD))
		{
			warThreshold = Long.valueOf(redisAssistant.hgetString(hkey,
					RedisKey.PARTNER_OR_WARN_KEY_WARNTHRESHOLD));
		}
		Long count = Long.valueOf(redisAssistant.getStringValue(key));
		if (count <= warThreshold)
		{
			log.info("订购次数已用完，key：{}", key);

			sendWarnMsg(partnerCode, hkey);

			if(count <= 0l)
			{
				return ReturnUtil.returnJsonInfo(Constant.NO_ORDER_RESOURCE_CODE,
						Constant.NO_ORDER_RESOURCE_MSG, null);
			}
		}
		log.info("deduct one from key:{}", key);
		redisAssistant.increateValue(key, -1l);
		
		return null;
	}
	
	/**
	 * 
	 * Description: 次数不够则发送短信(暂定一天发一次)
	 * @param partnerCode
	 * 
	 * Date: 2017年7月9日 
	 * @author Liuys5
	 */
	@Override
	public void sendWarnMsg(String partnerCode, String hkey)
	{
		String sendFlagKey = RedisKey.PARTNER_OR_WARN_SENDFLAG + partnerCode;
		if(!redisAssistant.exist(sendFlagKey))
		{
			log.info("key:{}不存在，发送告警短信！", sendFlagKey);
			// key 不存在则设置key，发送短信，设置发短信标记一天有效 
			Long vt = 24l * 60l * 60l * 1000l;
			try
			{
				vt = DateUtil.getTodayEndTime() - new Date().getTime();
			} catch (ParseException e)
			{
			}
			redisAssistant.setValue(sendFlagKey, "1", vt, TimeUnit.MILLISECONDS);
			
			String path = "";
			if (System.getProperty("os.name").startsWith("win")
					|| System.getProperty("os.name").startsWith("Win"))
			{
				path = OrderServiceImpl.class.getResource("/").getPath();
			} else
			{
				path = System.getProperty("user.dir");
			}
			
			String content = smsContentConfig.getPorMonitorSmsContent();
			if (StringUtils.isEmpty(content))
			{
				content = "";
			}
			content = content.replace("{0}", String.valueOf(partnerCode));
			SGIPSendMSGUtil.CONF_PATH = path + File.separator + "sgip.properties";
			log.info("configPath:{}", SGIPSendMSGUtil.CONF_PATH);
			
			String phones = redisAssistant.hgetString(hkey,
					RedisKey.PARTNER_OR_WARN_KEY_MOBILEPHONE);
			if (!org.springframework.util.StringUtils.isEmpty(phones))
			{
				log.info("send warn msg {}", phones);
				List<String> phoneList = Arrays.asList(phones.split(","));
				SGIPSendMSGUtil.sendMsg(phoneList, content);
			}
		}
		else{
			log.info("key:{}存在，已发送告警短信！", sendFlagKey);
		}
	}
	
	@Override
	public void refundOrderResource(String partnerCode)
	{
		String key = RedisKey.PARTNER_OR_KEY_PREFIX + partnerCode;
		
		if(redisAssistant.exist(key))
		{
			redisAssistant.increateValue(key, 1l);
			log.info("订购失败，进行次数补偿,key:{},补偿后剩余次数{}", key,
					redisAssistant.getStringValue(key));
		}
		else
		{
			// 资源key不存在，存放到回退补偿key上
			log.info("订购失败，进行次数补偿,key:{}不存在", key);
			String newkey = RedisKey.PARTNER_OR_RF_KEY_PREFIX + partnerCode;
			if(redisAssistant.exist(key))
			{
				redisAssistant.increateValue(newkey, 1l);
			}
			else
			{
				redisAssistant.setForever(newkey, "1");
			}
		}
	}
}
