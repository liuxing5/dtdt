package com.asiainfo.dtdt.interfaces.pay;
/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月30日 下午3:20:54 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public interface IPayOrderService {
	
	/**
	* @Title: IPayOrderService 
	* @Description: (查询支付信息) 
	* @param payId
	* @return        
	* @throws
	 */
	String queryPayOrderByPayId(String payId);
	
	void updatePayOrderStatusAfterPayNotify(String resultCode, String out_trade_no,String transaction_id);
	
	/**
	* @Title: IPayOrderService 
	* @Description: (记录支付信息) 
	* @param payId 支付id
	* @param orderId 支付订单号
	* @param payMoney	支付金额
	* @param operType	支付类型
	* @param state	支付状态0-初始化，1-支付中，2-支付成功，3-支付失败，4-退款中，5退款成功，6退款失败
	* @return        
	* @throws
	 */
	int insertPayOrder(String payId,String orderId,Long payMoney,byte operType,String state);
	
}
