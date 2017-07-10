package com.asiainfo.dtdt.task.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.task.entity.PartnerSmsConfig;
import com.asiainfo.dtdt.task.service.IPartnerSMSConfService;
import com.asiainfo.dtdt.task.service.mapper.PartnerSmsConfigMapper;

/**
 * 
 * Description: 
 * Date: 2017年7月8日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Service
@Log4j2
public class PartnerSMSConfImp implements IPartnerSMSConfService {

	@Resource
	private PartnerSmsConfigMapper partnerSMSConfMapper;
	
	@Resource
	private RedisAssistant redis;
	
	@Override
	public void loadPartnerSMSConf()
	{
		try
		{
			List<PartnerSmsConfig> list = partnerSMSConfMapper.loadAll();
			if(null != list && list.size() > 0)
			{
				list.forEach(psc -> {
					String hkey = RedisKey.PARTNER_OR_WARN_KEY_PREFIX
							+ psc.getPartnerCode();

					redis.hset(hkey, RedisKey.PARTNER_OR_WARN_KEY_MOBILEPHONE,
							psc.getMobilephone());
					
					redis.hset(hkey, RedisKey.PARTNER_OR_WARN_KEY_MAIL,
							psc.getMail());
					redis.hset(hkey,
							RedisKey.PARTNER_OR_WARN_KEY_WARNTHRESHOLD, psc
									.getWarnThreshold().toString());
					
					log.info("hkey:{}|{}:{}|{}:{}|{}:{}", hkey,
							RedisKey.PARTNER_OR_WARN_KEY_MOBILEPHONE,
							redis.hgetString(hkey, RedisKey.PARTNER_OR_WARN_KEY_MOBILEPHONE),
							RedisKey.PARTNER_OR_WARN_KEY_MAIL,
							redis.hgetString(hkey, RedisKey.PARTNER_OR_WARN_KEY_MAIL),
							RedisKey.PARTNER_OR_WARN_KEY_WARNTHRESHOLD,
							redis.hgetString(hkey, RedisKey.PARTNER_OR_WARN_KEY_WARNTHRESHOLD));
				});
			}
		}
		catch(Exception e)
		{
			log.error("loadPartnerSMSConf error: {}", e);
		}
	}

}
