package com.asiainfo.dtdt.task.operation;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.asiainfo.dtdt.task.service.IPartnerOrderResourcesService;

/**
 * 
 * Description: 合作方订购资源初始化以及 定时刷新
 * Date: 2017年7月7日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Component
public class PartnerOrderResourceTask {
	
	@Resource
	IPartnerOrderResourcesService partnerORServ;
	
	@PostConstruct
    public void init() {
		partnerORServ.lookRedisPartnerOR();
		partnerORServ.refreshTask();
    }

	@Scheduled(cron = "0/30 * * * * ?")
    public void lookRedisPartnerOR() {
    	partnerORServ.lookRedisPartnerOR();
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    public void reload() {
    	partnerORServ.refreshTask();
    } 

}
