package com.asiainfo.dtdt.service.mapper;

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

    int updateByPrimaryKeySelective(Order record);
}