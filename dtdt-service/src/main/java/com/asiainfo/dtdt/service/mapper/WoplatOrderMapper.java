package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.WoplatOrder;

public interface WoplatOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(WoplatOrder record);

    int insertSelective(WoplatOrder record);

    WoplatOrder selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(WoplatOrder record);

    int updateByPrimaryKey(WoplatOrder record);
}