package com.asiainfo.dtdt.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.HisOrderRecord;

public interface HisOrderRecordMapper {
	
    int deleteByPrimaryKey(String orderId);

    int insertSelective(HisOrderRecord record);

    HisOrderRecord selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(HisOrderRecord record);

    /**
    * @Title: queryOrderState 
    * @Description: 查询订单状态
    * @param orderId
    * @param partnerCode
    * @param appkey
    * @return Order
    * @throws
     */
    HisOrderRecord queryOrderState(String orderId, String partnerCode, String appkey);
    
    /**
     * @Title: queryOrderRecord 
     * @Description: 查询订购信息服务--失败
     * @param mobilephone
     * @param appkey
     * @return List<HisOrderRecord>
     * @throws
      */
 	List<HisOrderRecord> queryOrderRecord(@Param("mobilephone")String phone, @Param("appkey")String appkey);
}