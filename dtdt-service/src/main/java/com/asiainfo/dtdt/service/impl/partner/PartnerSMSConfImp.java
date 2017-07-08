package com.asiainfo.dtdt.service.impl.partner;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.log4j.Log4j2;

import com.alibaba.dubbo.config.annotation.Service;
import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.entity.PartnerSmsConfig;
import com.asiainfo.dtdt.service.IPartnerSMSConfService;
import com.asiainfo.dtdt.service.mapper.PartnerSmsConfigMapper;

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
							psc.getMobilephone(),
							RedisKey.PARTNER_OR_WARN_KEY_MAIL, psc.getMail(),
							RedisKey.PARTNER_OR_WARN_KEY_WARNTHRESHOLD,
							psc.getWarnThreshold());
				});
			}
		}
		catch(Exception e)
		{
			log.error("loadPartnerSMSConf error: {}", e);
		}
	}

}
