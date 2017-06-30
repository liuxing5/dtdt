package com.asiainfo.dtdt.interfaces.order;
/**
* @ClassName: IOrderService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author liuxing5
* @date 2017年6月30日 下午6:55:14 
*
 */
public interface IQueryOrderService {
	
	/**
	* @Title: queryOrderRecord 
	* @Description: 查询订单状态服务
	* @param phone
	* @return String
	* @throws
	 */
	String queryOrderRecord(String phone);
	
	/**
	* @Title: queryOrderState 
	* @Description: 查询订单状态服务
	* @param orderId
	* @return String
	* @throws
	 */
	String queryOrderState(String orderId);
}
