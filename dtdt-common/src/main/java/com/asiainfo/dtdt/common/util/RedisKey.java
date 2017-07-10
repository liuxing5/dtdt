package com.asiainfo.dtdt.common.util;


public class RedisKey {

	// 短信验证码key结构SMSC_PARTNERCODE_APPKEY_PHONENUM
	public static final String SMSC = "SmsC";
	// 合作方订购资源
	public static final String PARTNER_OR_KEY_PREFIX = "POR_";
	// 合作方订购资源回退补偿
	public static final String PARTNER_OR_RF_KEY_PREFIX = "POR_RF";
	/* 合作方监控告警配置 */
	public static final String PARTNER_OR_WARN_KEY_PREFIX = "POR_WARN_";
	public static final String PARTNER_OR_WARN_KEY_MOBILEPHONE = "phone";
	public static final String PARTNER_OR_WARN_KEY_MAIL = "mail";
	public static final String PARTNER_OR_WARN_KEY_WARNTHRESHOLD = "warntd";
	public static final String PARTNER_OR_WARN_SENDFLAG = "POR_WSM_";
	
	/*服务请求有效时间*/
	public static final String SERVICE_VALID_TIME = "ser_v_t";
	/*短信有效时间*/
	public static final String SMSCODE_VALID_TIME = "smsc_v_t";
	
	
}
