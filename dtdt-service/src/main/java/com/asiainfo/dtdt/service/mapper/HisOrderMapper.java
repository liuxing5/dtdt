package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.HisOrder;

public interface HisOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(HisOrder record);

    int insertSelective(HisOrder record);

    HisOrder selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(HisOrder record);

    int updateByPrimaryKey(HisOrder record);
}