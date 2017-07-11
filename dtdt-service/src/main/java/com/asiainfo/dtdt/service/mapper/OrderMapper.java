package com.asiainfo.dtdt.service.mapper;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.Order;

public interface OrderMapper {
	int deleteByPrimaryKey(String orderId);

    /**
    * @Title: OrderMapper 
    * @Description: (插入在途订单信息) 
    * @param record
    * @return        
    * @throws
     */
    int insertOrder(Order record);

    Order selectByPrimaryKey(String orderId);
    
    /**
    * @Title: queryOrderState 
    * @Description: 查询订单状态
    * @param orderId
    * @param partnerCode
    * @param appkey
    * @return Order
    * @throws
     */
    Order queryOrderState(@Param("orderId")String orderId, @Param("partnerCode")String partnerCode, @Param("appkey")String appkey);

    int updateByPrimaryKeySelective(Order record);
    
	/**
	* @Title: queryOrder 
	* @Description: 查询订单状态服务
	* @param orderId
	* @return String
	* @throws
	 */
    String queryOrder(String orderId);
    
    Order queryOrderByWoOrderId(@Param("woOrderId")String woOrderId);
    
    /**
     * @Title: OrderMapper 
     * @Description: (将在途订购信息存放到备份表中) 
     * @param orderId
     * @param copyType
     * @param copyRemark
     * @return        
     * @throws
      */
     int insertFromHisOrderById(@Param("orderId")String orderId,@Param("copyType")String copyType,@Param("copyRemark")String copyRemark);
     
     /**
     * @Title: insertHisOrder 
     * @Description:  (将在途订购信息存放到备份表中-状态改) 
     * @param orderId
     * @param state
     * @param copyType
     * @param copyRemark
     * @return int
     * @throws
      */
      int insertHisOrder(@Param("orderId")String orderId,@Param("state")String state,@Param("copyType")String copyType,@Param("copyRemark")String copyRemark);
     
     /**
     * @Title: OrderMapper 
     * @Description: (将在途订购信息沉淀到订购关系表) 
     * @param orderId 订单ID
     * @param cycleType 产品周期
     * @param woOrder  沃家总管方订购记录 0：我方初始化订购 1：其他代理商订购 2：其他代理商订购失效、到期或退订由我方续订
     * @return        
     * @throws
      */
     int insertFromOrderRecordById(@Param("orderId")String orderId,@Param("cycleType")byte cycleType,@Param("woOrder")String woOrder);

     /**
      * 订单号存在
      * @param partnerOrderId
      * @return
      */
     int existPartnerOrderId(String partnerOrderId);
}