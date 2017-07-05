package com.asiainfo.dtdt.common.util;

import org.apache.commons.lang.StringUtils;

public class IsMobileNo {
	/**
	 * 验证手机格式
	 * 
	 * @param mobiles
	 */
	public static boolean isMobile(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 ／／130 131 132 145 155 156 171 175
		 * 176 185 186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
//		String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//		String telRegex = "[1][34578][01256]\\d{8}";
		final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[156]|8[56])\\d{8}$)|(^1709\\d{7}$)";
		if (StringUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(CHINA_UNICOM_PATTERN);
	}

}
