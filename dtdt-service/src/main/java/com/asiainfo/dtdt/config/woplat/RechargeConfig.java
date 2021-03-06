package com.asiainfo.dtdt.config.woplat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


import lombok.Data;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年7月3日 下午9:18:52 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@ConfigurationProperties(prefix = "recharge.return_code")
@Data
@Configuration
public class RechargeConfig {
	
	private String recharge_fail_code;
	
	private String recharge_success_code;
	
	private String recharge_timeout_code;
}
