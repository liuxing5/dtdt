package com.asiainfo.dtdt.common.util;


public class RedisKey {

	// 短信验证码key结构SMSC_PARTNERCODE_APPKEY_PHONENUM
	public static final String SMSC = "SmsC";
	// 合作方订购资源
	public static final String PARTNER_OR_KEY_PREFIX = "POR_";
	/* 合作方监控告警配置 */
	public static final String PARTNER_OR_WARN_KEY_PREFIX = "POR_WARN_";
	public static final String PARTNER_OR_WARN_KEY_MOBILEPHONE = "phone";
	public static final String PARTNER_OR_WARN_KEY_MAIL = "mail";
	public static final String PARTNER_OR_WARN_KEY_WARNTHRESHOLD = "warntd";
	
	
}
