package com.asiainfo.dtdt.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Description: 联通手机号码判断
 * Date: 2017年7月5日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
public class PhoneUtil {
	
	/**
	 * 验证手机格式
	 * 
	 * @param mobiles
	 */
	public static boolean isCUCMobile(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 ／／130 131 132 145 155 156 171 175
		 * 176 185 186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[156]|8[56])\\d{8}$)|(^1709\\d{7}$)";
		if (StringUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(CHINA_UNICOM_PATTERN);
	}

}
