package com.asiainfo.dtdt.interfaces;

import java.util.Date;

public interface ICodeService {
	/**
	 * @param partnerCode
	 * @param appKey
	 * @param phone
	 * @return
	 */
	String getCode(String partnerCode, String appKey, String phone);
	
	/**
	* @Title: ICodeService 
	* @Description: (记录验证码) 
	* @param record
	* @return        
	* @throws
	 */
	int insertVcode(String vcode,String vcodeSendTime,String orderId,String userInputVcode,String userInputTime,String vcodeValidResut);
}
