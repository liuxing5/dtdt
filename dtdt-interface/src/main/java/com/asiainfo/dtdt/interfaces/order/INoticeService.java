package com.asiainfo.dtdt.interfaces.order;
/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年7月3日 上午11:29:56 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public interface INoticeService {
	
	/**
	* @Title: INoticeService 
	* @Description: (沃家总管回调通知) 
	* @param notifyJson        
	* @throws
	 */
	String optNoticeOrder(String notifyJson);
	
	/**
	* @Title: INoticeService 
	* @Description: (免流平台回调通知接入方) 
	* @param orderId
	* @return        
	* @throws
	 */
	void dtdtNoticeOrder(String orderId);
}
