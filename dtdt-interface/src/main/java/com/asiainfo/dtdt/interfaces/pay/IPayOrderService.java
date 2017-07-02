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
	
}
