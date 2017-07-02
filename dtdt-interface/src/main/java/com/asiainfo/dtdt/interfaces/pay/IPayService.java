package com.asiainfo.dtdt.interfaces.pay;

import java.util.Map;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月30日 下午2:54:22 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public interface IPayService {

	/**
	* @Title: IPayService 
	* @Description: (微信验签) 
	* @param params
	* @return        
	* @throws
	 */
	boolean weChatCheckSign(Map<String,String> params);
	
	/**
	* @Title: IPayService 
	* @Description: (支付宝验签) 
	* @param params
	* @return        
	* @throws
	 */
	boolean aliPayCheckSign(Map<String,String> params);
	
	/**
	* @Title: IPayService 
	* @Description: (判断业务是否已经处理过) 
	* @param out_trade_no
	* @return        
	* @throws
	 */
	boolean isProcessed(String out_trade_no);
}
