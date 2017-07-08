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
	 * @Description: (后向流量单个订购接口) 
	 * @param jsonStr
	 * @return        
	 * @throws
	 */
	String postfixOrder(String jsonStr);
	
	/**
	 * @Title: IOrderService 
	 * @Description: (后向流量批量订购接口) 
	 * @param jsonStr
	 * @return        
	 * @throws
	 */
	String batchPostfixOrder(String jsonStr);
	
	/**
	 * @Title: IOrderService 
	 * @Description: (更新后向流量批量订单状态) 
	 * @param jsonStr
	 * @return        
	 * @throws
	 */
	int updateBatchOrderState(String batchOrderId);
	
	/**
	* @Title: IOrderService 
	* @Description: (插入在途订单信息) 
	* @param orderStr
	* @return        
	* @throws
	 */
	int insertOrder(String orderStr);
	
	/**
	* @Title: IOrderService 
	* @Description: (支付成功处理订购信息) 
	* @param resultCode 支付返回码
	* @param orderId   订购ID     
	* @throws
	 */
	void paySuccessOrderDeposition(String resultCode,String orderId);
	
	/**
    * @Title: OrderMapper 
    * @Description: (将在途订购信息存放到备份表中) 
    * @param orderId
    * @param copyType
    * @param copyRemark
    * @return        
    * @throws
     */
    int insertFromHisOrderById(String orderId,String copyType,String copyRemark);
    
    /**
    * @Title: OrderMapper 
    * @Description: (将在途订购信息沉淀到订购关系表) 
    * @param orderId 订单ID
    * @param cycleType 产品周期
    * @param woOrder  沃家总管方订购记录 0：我方初始化订购 1：其他代理商订购 2：其他代理商订购失效、到期或退订由我方续订
    * @return        
    * @throws
     */
    int insertFromOrderRecordById(String orderId,byte cycleType,String woOrder);
    
	/**
	* @Title: closeOrder 
	* @Description: (定向流量退订接口) 
	* @param orderStr
	* @param appkey
	* @return        
	* @throws
	 */
	String closeOrder(String orderStr, String appkey, String partnerCode);
	
	/**
	* @Title: closeOrderNew
	* @Description: (定向流量退订接口)-new
	* @param orderStr
	* @param appkey
	* @return        
	* @throws
	 */
	String closeOrderNew(String orderStr, String appkey, String partnerCode);
	
	/**
	* @Title: OrderServiceImpl 
	* @Description: (将在途订单订购信息存放在备份表中并删除在途表信息) 
	* @param orderId 订单ID
	* @param copyType	入表方式（0：订单完工 1：未支付失效 2：支付失败 3：人工操作）
	* @param copyRemark  入表备注      
	* @throws
	 */
    void insertOrderBakAndDelOrder(String orderId,String copyType,String copyRemark);
    
    /**
	* @Title: OrderServiceImpl 
	* @Description: (更新在途订购订单状态) 
	* @param orderId
	* @param state
	* 	状态1：未付款，此时邮箱侧合作方查询该笔订购状态为：未付款；
	*	状态2：付款中，此时邮箱侧合作方查询该笔订购状态为：付款中；
	*	状态3：付款失败，此时邮箱侧合作方查询该笔订购状态为：付款失败；
	*	状态4：付款成功&邮箱侧发起订购中，此时邮箱侧合作方查询该笔订购状态为：付款成功，待订购；
	*	状态5：邮箱侧订购失败&未原路原付款金额退款，此时邮箱侧合作方查询该笔订购状态为：订购失败，待原路退款；
	*	状态6：邮箱侧订购失败&原路原付款金额退款中，此时邮箱侧合作方查询该笔订购状态为：订购失败，原路退款中；
	*	状态7：邮箱侧订购失败&原路原付款金额退款成功，此时邮箱侧合作方查询该笔订购状态为：订购失败，原路退款成功；
	*	状态8：邮箱侧订购失败&原路原付款金额退款失败，此时邮箱侧合作方查询该笔订购状态为：订购失败，原路退款失败，人工处理中；
	*	状态9：邮箱侧订购中，此时邮箱侧合作方查询该笔订购状态为：订购受理中；
	*	状态10：邮箱侧订购成功&沃家总管侧存在有效订购关系&待返充话费，此时邮箱侧合作方查询该笔订购状态为：订购成功；
	*	状态11：邮箱侧订购成功&沃家总管侧存在有效订购关系&返充话费成功，此时邮箱侧合作方查询该笔订购状态为：订购成功；
	*	状态12：邮箱侧订购成功&沃家总管侧存在有效订购关系&返充话费失败，此时邮箱侧合作方查询该笔订购状态为：订购成功；
	*	状态13：邮箱侧订购成功&沃家总管侧存在有效订购关系&无需返充话费，此时邮箱侧合作方查询该笔订购状态为：订购成功；
	*	状态14：邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求，此时邮箱侧合作方查询该笔订购状态为：订购成功；
	*	状态15：邮箱侧订购失败&沃家总管侧不存在有效订购关系&未原路非付款金额退款，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，待原路部分退款；
	*	状态16：邮箱侧订购失败&沃家总管侧不存在有效订购关系&原路非付款金额退款中，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，原路部分退款中；
	*	状态17：邮箱侧订购失败&沃家总管侧不存在有效订购关系&原路非付款金额退款失败，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，原路部分退款失败，人工处理中；
	*	状态18：邮箱侧订购失败&沃家总管侧不存在有效订购关系&原路非付款金额退款成功，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，原路部分退款成功；
	*	状态19：邮箱侧退订成功，此时邮箱侧合作方查询该笔订购状态为：退订成功；
	*	状态20：邮箱侧退订中，此时邮箱侧合作方查询该笔订购状态为：退订中；
	*	状态21：邮箱侧已作废（订购状态在X小时内一直为“未支付”，X小时后将该订单状态设置为“已作废”），此时邮箱侧合作方查询该笔订购状态为：订购作废；
	*	状态22：服务到期（半年包、年包产品自然达到有效期截止日），此时邮箱侧合作方查询该笔订购状态为：服务到期；
	* @return        
	* @throws
	 */
    int updateOrder(String orderId,String woOrderId,String state,byte isNeedCharge,byte isRealRequestWoplat);
	
    /**
    * @Title: IOrderService 
    * @Description: (前向订购接口) 
    * @param jsonStr
    * @return        
    * @throws
    * @version 0.0.5
     */
    String forwardOrder(String jsonStr);
    
    /**
    * @Title: closeOrderUpdateTable 
    * @Description: 沃家退订接口回调后，更新 order和orderRecord表状态并迁移到历史表
    * @param orderId
    * @param orderRecord
    * @param state void
    * @throws
     */
    void closeOrderUpdateTable(String orderId, String orderRecordJson, String state);
}
