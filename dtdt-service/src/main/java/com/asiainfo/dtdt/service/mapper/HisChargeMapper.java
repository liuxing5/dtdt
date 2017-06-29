package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.HisCharge;

public interface HisChargeMapper {
    int insert(HisCharge record);

    int insertSelective(HisCharge record);
}