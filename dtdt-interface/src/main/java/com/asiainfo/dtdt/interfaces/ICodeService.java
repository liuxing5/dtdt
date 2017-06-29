package com.asiainfo.dtdt.interfaces;

public interface ICodeService {
	/**
	 * @param partnerCode
	 * @param appKey
	 * @param phone
	 * @return
	 */
	String getCode(String partnerCode, String appKey, String phone);
}
