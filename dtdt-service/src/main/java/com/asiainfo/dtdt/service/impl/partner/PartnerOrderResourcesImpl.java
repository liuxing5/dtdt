package com.asiainfo.dtdt.service.impl.partner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.log4j.Log4j2;

import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.entity.PartnerOrderResources;
import com.asiainfo.dtdt.service.IPartnerOrderResourcesService;
import com.asiainfo.dtdt.service.mapper.PartnerHisOrderResourcesMapper;
import com.asiainfo.dtdt.service.mapper.PartnerOrderResourcesMapper;


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
	
	/*@Resource
	private RedisUtil redisSingle;*/
	
	@Override
	public void refreshTask()
	{
		try
		{/*
			List<PartnerOrderResources> list = partnerORMapper.getAll();
			if(null != list)
			{
				// 过期列表 移到历史表
				List<PartnerOrderResources> outDate = new ArrayList<PartnerOrderResources>();

				List<PartnerOrderResources> charge = new ArrayList<PartnerOrderResources>();

				list.forEach(por -> {
					String key = RedisKey.PARTNER_OR_KEY_PREFIX
							+ por.getPartnerCode();

					// 如果到期 则清掉key，否则刷新key的值
					if (por.getEndTime().getTime() < new Date().getTime())
					{
						if (redisSingle.exist(key))
						{
							log.info("{}到期，移除，预存次数{}，剩余次数{}", key,
									por.getPreCount(), redisSingle.get(key));
							por.setUseCount(por.getPreCount()
									- Long.valueOf(redisSingle.get(key)));
						}
						outDate.add(por);
					} else if (StringUtils.isEmpty(redisSingle.get(key)))
					{
						log.info("{}不存在，增加，预存次数{}", key, por.getPreCount(),
								redisSingle.get(key));
						redisSingle.set(key, por.getPreCount().toString());
					} else
					{
						por.setUseCount(por.getPreCount()
								- Long.valueOf(redisSingle.get(key)));
						Long newCount = Long.valueOf(redisSingle.get(key))
								+ por.getChargeCount();
						log.info("{}存在未过期，刷新，预存次数{}，剩余次数{}，充值次数{}，刷新后次数{}", key,
								por.getPreCount(), redisSingle.get(key),
								por.getChargeCount(), newCount);
						redisSingle.set(key, String.valueOf(newCount));

                        por.setPreCount(por.getPreCount() + por.getChargeCount());
						por.setChargeCount(0l);
						charge.add(por);
					}
				});

				// 过期的copy到历史表
				outDate.forEach(od -> {
					partnerHisORMapper.copyFromInstance(od.getBatchId());
					partnerORMapper.deleteByPrimaryKey(od.getBatchId());
				});

				// 充值的将充值次数加到预存上去
				charge.forEach(c -> {
					partnerORMapper.updateByPrimaryKeySelective(c);
				});

			}
		*/
			

			List<PartnerOrderResources> list = partnerORMapper.getAll();
			if(null != list)
			{
				// 过期列表 移到历史表
				List<PartnerOrderResources> outDate = new ArrayList<PartnerOrderResources>();

				List<PartnerOrderResources> charge = new ArrayList<PartnerOrderResources>();

				list.forEach(por -> {
					String key = RedisKey.PARTNER_OR_KEY_PREFIX
							+ por.getPartnerCode();

					// 如果到期 则清掉key，否则刷新key的值
					if (por.getEndTime().getTime() < new Date().getTime())
					{
						if (!StringUtils.isEmpty(redis.getStringValue(key)))
						{
							log.info("{}到期，移除，预存次数{}，告警阈值{}，剩余次数{}", key,
									por.getPreCount(), por.getWarnThreshold(),
									redis.getStringValue(key));
							por.setUseCount(por.getPreCount()
									- Long.valueOf(redis.getStringValue(key)));
						}
						outDate.add(por);
					} else if (StringUtils.isEmpty(redis.getStringValue(key)))
					{
						log.info("{}不存在，增加，预存次数{},告警阈值{}", key,
								por.getPreCount(), redis.getStringValue(key),
								por.getWarnThreshold());
						Long canUseCount = por.getPreCount()
								- por.getWarnThreshold();
						redis.setForever(key, canUseCount.toString());
					} else
					{
						por.setUseCount(por.getPreCount()
								- por.getWarnThreshold()
								- Long.valueOf(redis.getStringValue(key)));
						Long newCount = Long.valueOf(redis.getStringValue(key))
								+ por.getChargeCount();
						log.info(
								"{}存在未过期，刷新，预存次数{}，告警阈值{}，剩余次数{}，充值次数{}，刷新后次数{}",
								key, por.getPreCount(), por.getWarnThreshold(),
								redis.getStringValue(key),
								por.getChargeCount(), newCount);
						redis.setForever(key, String.valueOf(newCount));

						por.setPreCount(por.getPreCount()
								+ por.getChargeCount());
						por.setChargeCount(0l);

						charge.add(por);
					}
				});

				// 过期的copy到历史表
				outDate.forEach(od -> {
					partnerHisORMapper.copyFromInstance(od.getBatchId());
					partnerORMapper.deleteByPrimaryKey(od.getBatchId());
				});

				// 充值的将充值次数加到预存上去
				charge.forEach(c -> {
					partnerORMapper.updateByPrimaryKeySelective(c);
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
					log.info("key:{}|exist:{}|value:{}", key,
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
	
	
	public static void main(String[] ag)
	{
		System.out.println(11l - 10l - Long.valueOf(-69));
	}
}
