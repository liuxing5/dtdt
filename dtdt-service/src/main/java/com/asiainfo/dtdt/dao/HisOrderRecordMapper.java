package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.HisOrderRecord;

public interface HisOrderRecordMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(HisOrderRecord record);

    int insertSelective(HisOrderRecord record);

    HisOrderRecord selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(HisOrderRecord record);

    int updateByPrimaryKey(HisOrderRecord record);
}