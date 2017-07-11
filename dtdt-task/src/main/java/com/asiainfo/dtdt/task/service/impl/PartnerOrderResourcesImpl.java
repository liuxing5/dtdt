package com.asiainfo.dtdt.task.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.task.entity.PartnerOrderResources;
import com.asiainfo.dtdt.task.service.IPartnerOrderResourcesService;
import com.asiainfo.dtdt.task.service.mapper.PartnerHisOrderResourcesMapper;
import com.asiainfo.dtdt.task.service.mapper.PartnerOrderResourcesMapper;


/**
 * 
 * Description: 合作方订购资源
 * Date: 2017年7月7日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Service
@Log4j2
public class PartnerOrderResourcesImpl implements IPartnerOrderResourcesService{

	@Resource
	private PartnerOrderResourcesMapper partnerORMapper;
	
	@Resource
	private PartnerHisOrderResourcesMapper partnerHisORMapper;
	
	@Resource
	private RedisAssistant redis;
	
	@Override
	public void refreshTask()
	{
		try
		{
			log.info("start refresh partner order resource");

			List<PartnerOrderResources> list = partnerORMapper.getAll();
			if(null != list)
			{
				list.forEach(por -> {
					String key = RedisKey.PARTNER_OR_KEY_PREFIX
							+ por.getPartnerCode();

					// 如果到期 则清掉key，否则刷新key的值
					if (por.getEndTime().getTime() < new Date().getTime())
					{
						if (redis.exist(key))
						{
							log.info("{}到期，移除，预存次数{}，剩余次数{}", key,
									por.getPreCount(), 
									redis.getStringValue(key));
							por.setUseCount(por.getPreCount()
									- Long.valueOf(redis.getStringValue(key)));
							redis.clear(key);
						}
						partnerORMapper.updateByPrimaryKeySelective(por);
						partnerHisORMapper.copyFromInstance(por.getBatchId());
						partnerORMapper.deleteByPrimaryKey(por.getBatchId());
					} else if (!redis.exist(key))
					{
						Long canUseCount = por.getPreCount();
						redis.setForever(key, canUseCount.toString());
						log.info("{}不存在，增加，预存次数{},可用次数{}", key,
								por.getPreCount(), 
								redis.getStringValue(key));
						
					} else
					{
						Long oldCanUsed = Long.valueOf(redis
								.getStringValue(key));
						log.info(
								"redisValue:{}***oldPreCount:{}****used:{}********************",
								oldCanUsed, por.getPreCount(),
								 por.getPreCount()
										- oldCanUsed);
						por.setUseCount(por.getPreCount() - oldCanUsed);

						redis.increateValue(key, por.getChargeCount());

						log.info(
								"{}存在未过期，刷新，原预存次数{}，剩余次数{}，充值次数{}，刷新后次数{}",
								key, por.getPreCount(), 
								oldCanUsed, por.getChargeCount(),
								redis.getStringValue(key));
						por.setPreCount(por.getPreCount()
								+ por.getChargeCount());
						por.setChargeCount(0l);
						partnerORMapper.updateByPrimaryKeySelective(por);
					}
				});
			}
		}
		catch(Exception e)
		{
			log.error("{}", "刷新订购资源", e);
		}
		
	}
	
	@Override
	public void lookRedisPartnerOR()
	{
		try
		{
			List<PartnerOrderResources> list = partnerORMapper.getAll();
			if (null != list)
			{

				list.forEach(por -> {
					String key = RedisKey.PARTNER_OR_KEY_PREFIX
							+ por.getPartnerCode();
					log.info("key:{}|exist:{}|value:{}", key, redis.exist(key),
							 redis.getStringValue(key));
				});
			}
		} catch (Exception e)
		{
			log.error("{}", "test()", e);
		}
	}
	
	@Override
	public PartnerOrderResources loadByPartnerCode(String partnerCode)
	{
		try
		{
			List<PartnerOrderResources> list = partnerORMapper
					.getByPartnerCode(partnerCode);
			if (null != list && list.size() > 0)
			{
				return list.get(0);
			}
		} catch (Exception e)
		{
			log.error("loadByPartnerCode{}|Error:{}", partnerCode, e);
		}

		return null;
	}
}
