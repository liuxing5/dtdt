package com.asiainfo.dtdt.interfaces;

/**
* @ClassName: ICodeService 
* @Description: (记录短信接口) 
* @author xiangpeng 
* @date 2017年7月6日 上午11:17:56 
*
 */
public interface ICodeService {
	
	/**
	* @Title: ICodeService 
	* @Description: (记录验证码) 
	* @param record
	* @return        
	* @throws
	 */
	int insertVcode(String vcode,String vcodeSendTime,String orderId,String userInputVcode,String userInputTime,String vcodeValidResut);
}
