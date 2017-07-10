package com.asiainfo.dtdt.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.HisOrder;

public interface HisOrderMapper {

	HisOrder selectByPrimaryKey(String orderId);
	
    int insertSelective(HisOrder record);

    int updateByPrimaryKeySelective(HisOrder record);
    
    List<HisOrder> getListByPartnerOrderId(String partnerOrderId);
    
    /**
    * @Title: queryOrderState 
    * @Description: 查询订单状态
    * @param orderId
    * @param partnerCode
    * @param appkey
    * @return Order
    * @throws
     */
    HisOrder queryOrderState(@Param("orderId")String orderId, @Param("partnerCode")String partnerCode, @Param("appkey")String appkey);

}