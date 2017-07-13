package com.asiainfo.dtdt.common.util;


public class RedisKey {

	// 短信验证码key结构SMSC_PARTNERCODE_APPKEY_PHONENUM
	public static final String SMSC = "SmsC";
	// 合作方订购资源
	public static final String PARTNER_OR_KEY_PREFIX = "POR_";
	// 合作方订购资源回退补偿
	public static final String PARTNER_OR_RF_KEY_PREFIX = "POR_RF";
	/* 合作方监控告警配置 
	 * Hash结构 field: phone 发送告警短信的手机号码
	 *               mail 发送告警邮件
	 *               warntd 告警值
	 *               name 名称
	 */
	public static final String PARTNER_OR_WARN_KEY_PREFIX = "POR_WARN_";
	public static final String PARTNER_OR_WARN_KEY_MOBILEPHONE = "phone";
	public static final String PARTNER_OR_WARN_KEY_MAIL = "mail";
	public static final String PARTNER_OR_WARN_KEY_WARNTHRESHOLD = "warntd";
	public static final String PARTNER_OR_WARN_KEY_NAME = "name";
	/* 告警短信发送标记 每天一次，设置失效时间到当天晚上23:59:59*/
	public static final String PARTNER_OR_WARN_SENDFLAG = "POR_WSM_";
	
	/*服务请求有效时间*/
	public static final String SERVICE_VALID_TIME = "ser_v_t";
	/*短信有效时间*/
	public static final String SMSCODE_VALID_TIME = "smsc_v_t";
	
	/*
	 * 业务校验开关
	 */
	public static final String SERVICE_CHECK_SWITCH = "SER_CH_SW";
	/*校验业务列表*/
	public static final String SERVICE_CHECK_LIST = "SER_CH_LIST";
	/*
	 * 业务配置  Hash结构 ser_业务ID_partnercode
	 * field: app appkey
	 *        total 合作方可用总数
	 *        spl   剩余可以次数（实际操作该字段）
	 */
	public static final String SERVICE_CONFIG_PREFIX = "ser_";
	public static final String SERVICE_CONFIG_SWITCH = "swi";
	public static final String SERVICE_CONFIG_APP = "app";
	public static final String SERVICE_CONFIG_TOTAL = "total";
	public static final String SERVICE_CONFIG_SURPLUS = "spl";
	
	
}
