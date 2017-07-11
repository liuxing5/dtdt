package com.asiainfo.dtdt.task.service.mapper;

import java.util.List;

import com.asiainfo.dtdt.task.entity.OrderRecord;


public interface OrderRecordMapper {
	
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
    * @Description: (更新订购关系) 
    * @param record
    * @return        
    * @throws
     */
    int updateOrderRecord(OrderRecord record);
 	
 	/**
 	* @Title: getCloseOrderList 
 	* @Description: 查询退订回调成功，且失效时间为本月月底的数据
 	* @return List<OrderRecord>
 	* @throws
 	 */
	List<OrderRecord> getCloseOrderList();
 	
}