package com.asiainfo.dtdt.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms.content")
@Data
public class SMSContentConfig {

	private String codeContent;
}
