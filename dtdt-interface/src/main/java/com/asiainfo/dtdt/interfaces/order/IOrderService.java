package com.asiainfo.dtdt.interfaces.order;
/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 上午11:39:11 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public interface IOrderService {
	
	/**
	* @Title: IOrderService 
	* @Description: (定向流量订购接口) 
	* @param jsonStr
	* @return        
	* @throws
	 */
	String preOrder(String jsonStr);
	
	/**
	* @Title: IOrderService 
	* @Description: (插入在途订单信息) 
	* @param orderStr
	* @return        
	* @throws
	 */
	int insertOrder(String orderStr);
}
