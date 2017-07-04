package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.Partner;

public interface PartnerMapper {
    int deleteByPrimaryKey(Long partnerId);

    int insert(Partner record);

    int insertSelective(Partner record);

    Partner selectByPrimaryKey(Long partnerId);

    int updateByPrimaryKeySelective(Partner record);

    int updateByPrimaryKey(Partner record);
    
    Partner getByPartnerCode(String partnerCode);
}