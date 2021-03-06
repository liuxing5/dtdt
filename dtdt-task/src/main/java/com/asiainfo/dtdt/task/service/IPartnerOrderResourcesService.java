package com.asiainfo.dtdt.task.service;

import com.asiainfo.dtdt.task.entity.PartnerOrderResources;

/**
 * 
 * Description: 合作方订购资源
 * Date: 2017年7月7日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
public interface IPartnerOrderResourcesService {

	public void refreshTask();

	public void lookRedisPartnerOR();

	public PartnerOrderResources loadByPartnerCode(String partnerCode);

}
