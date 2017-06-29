package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.OrderRecord;

public interface OrderRecordMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderRecord record);

    int insertSelective(OrderRecord record);

    OrderRecord selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderRecord record);

    int updateByPrimaryKey(OrderRecord record);
}