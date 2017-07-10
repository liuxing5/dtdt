package com.asiainfo.dtdt.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.OrderRecord;

public interface OrderRecordMapper {
	
	OrderRecord selectByPrimaryKey(String orderId);
	
    /**
    * @Title: queryOrderState 
    * @Description: 查询订单状态
    * @param orderId
    * @param partnerCode
    * @param appkey
    * @return Order
    * @throws
     */
	OrderRecord queryOrderState(@Param("orderId")String orderId, @Param("partnerCode")String partnerCode, @Param("appkey")String appkey);
    
	/**
	* @Title: OrderRecordMapper 
	* @Description: (删除订购关系信息) 
	* @param orderId
	* @return        
	* @throws
	 */
    int deleteOrderRecord(String orderId);

    /**
    * @Title: OrderRecordMapper 
    * @Description: (插入订购关系) 
    * @param record
    * @return        
    * @throws
     */
    int insertOrderRecord(OrderRecord record);

    /**
    * @Title: OrderRecordMapper 
    * @Description: (查询订购关系) 
    * @param productCode
    * @param mobilephone
    * @return        
    * @throws
     */
    List<OrderRecord> queryOrderRecordByParam(@Param("appKey")String appKey,@Param("productCode")String productCode,@Param("mobilephone")String mobilephone);

    /**
    * @Title: OrderRecordMapper 
    * @Description: (更新订购关系) 
    * @param record
    * @return        
    * @throws
     */
    int updateOrderRecord(OrderRecord record);
    
    /**
     * @Title: queryOrderRecord 
     * @Description: 查询订购信息服务
     * @param phone
     * @return List<OrderRecord>
     * @throws
      */
 	List<OrderRecord> queryOrderRecord(@Param("mobilephone")String phone, @Param("partnerCode")String partnerCode, @Param("appkey")String appkey);
 	
 	/**
 	* @Title: selectMonthProduct 
 	* @Description: 校验包月类订购
 	* @param orderId
 	* @param appkey
 	* @return OrderRecord
 	* @throws
 	 */
 	OrderRecord selectMonthProduct(@Param("orderId")String orderId, @Param("appkey")String appkey, @Param("partnerCode")String partnerCode);
 	
 	/**
 	* @Title: selectMonthProduct 
 	* @Description: 校验订购
 	* @param orderId
 	* @param appkey
 	* @return OrderRecord
 	* @throws
 	 */
 	OrderRecord selectOrderRecord(@Param("orderId")String orderId, @Param("appkey")String appkey, @Param("partnerCode")String partnerCode);
 	
 	/**
 	* @Title: queryOrderByWoOrderId 
 	* @Description: TODO(这里用一句话描述这个方法的作用)
 	* @param woOrderId
 	* @return Order
 	* @throws
 	 */
 	OrderRecord queryOrderRecordByWoOrderId(@Param("woOrderId")String woOrderId);
 	
}