package com.asiainfo.dtdt.config.sms;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Description: 平台内短信内容配置
 * Date: 2017年7月8日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Configuration
@ConfigurationProperties(prefix = "sms.content")
@Data
public class SMSContentConfig {

	// 验证短信码内容
	private String codeContent;
	// 合作方订购资源监控告警内容
	private String porMonitorSmsContent;
	// 订购成功，给用户下发短信内容
	private String orderSuccessContent;
}
