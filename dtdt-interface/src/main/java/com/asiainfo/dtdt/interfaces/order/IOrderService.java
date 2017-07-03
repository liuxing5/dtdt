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
	
	/**
	* @Title: IOrderService 
	* @Description: (支付成功处理订购信息) 
	* @param resultCode 支付返回码
	* @param orderId   订购ID     
	* @throws
	 */
	boolean paySuccessOrderDeposition(String resultCode,String orderId);
	
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
	* @return        
	* @throws
	 */
	String closeOrder(String orderStr);
	
}
