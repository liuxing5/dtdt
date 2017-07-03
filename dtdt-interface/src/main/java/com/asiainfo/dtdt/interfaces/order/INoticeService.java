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
	* @Title: OrderServiceImpl 
	* @Description: (将在途订单订购信息存放在备份表中并删除在途表信息) 
	* @param orderId 订单ID
	* @param copyType	入表方式（0：订单完工 1：未支付失效 2：支付失败 3：人工操作）
	* @param copyRemark  入表备注      
	* @throws
	 */
	void optNoticeOrder(String resultCode,String orderId);
	
}
