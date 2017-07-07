package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.PartnerSmsConfig;

public interface PartnerSmsConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(PartnerSmsConfig record);

    int insertSelective(PartnerSmsConfig record);

    PartnerSmsConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PartnerSmsConfig record);
}