package com.asiainfo.dtdt.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.OrderRecord;

public interface OrderRecordMapper {
	
	OrderRecord selectByPrimaryKey(String orderId);
	
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
    OrderRecord queryOrderRecordByParam(@Param("productCode")String productCode,@Param("mobilephone")String mobilephone);

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
 	List<OrderRecord> queryOrderRecord(String phone);
}