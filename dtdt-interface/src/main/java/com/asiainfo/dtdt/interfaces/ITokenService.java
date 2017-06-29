package com.asiainfo.dtdt.interfaces;

public interface ITokenService {
	
	/**
	 * @param partnerCode
	 * @param appKey
	 * @return
	 */
	String getToken(String partnerCode, String appKey);
}
