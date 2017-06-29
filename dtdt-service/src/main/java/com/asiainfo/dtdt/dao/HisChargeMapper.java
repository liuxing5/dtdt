package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.HisCharge;

public interface HisChargeMapper {
    int insert(HisCharge record);

    int insertSelective(HisCharge record);
}