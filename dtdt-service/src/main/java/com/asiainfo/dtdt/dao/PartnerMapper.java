package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.Partner;

public interface PartnerMapper {
    int deleteByPrimaryKey(Long partnerId);

    int insert(Partner record);

    int insertSelective(Partner record);

    Partner selectByPrimaryKey(Long partnerId);

    int updateByPrimaryKeySelective(Partner record);

    int updateByPrimaryKey(Partner record);
}