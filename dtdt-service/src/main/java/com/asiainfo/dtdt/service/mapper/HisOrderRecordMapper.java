package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.HisOrderRecord;

public interface HisOrderRecordMapper {
    int deleteByPrimaryKey(String orderId);

    int insertSelective(HisOrderRecord record);

    HisOrderRecord selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(HisOrderRecord record);

}