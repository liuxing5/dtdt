package com.asiainfo.dtdt.task.operation;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.asiainfo.dtdt.task.service.IPartnerSMSConfService;

/**
 * 
 * Description: 系统启动时，初始化redis
 * Date: 2017年7月8日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Component
public class PartnerORSMSConfTask {

	@Resource
	private IPartnerSMSConfService partnerSMSConf;
	
	@PostConstruct
    public void init() {
		partnerSMSConf.loadPartnerSMSConf();
    }
	
}
