package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.PartnerHisOrderResources;

public interface PartnerHisOrderResourcesMapper {
    int deleteByPrimaryKey(String batchId);

    int insert(PartnerHisOrderResources record);

    int insertSelective(PartnerHisOrderResources record);

    PartnerHisOrderResources selectByPrimaryKey(String batchId);

    int updateByPrimaryKeySelective(PartnerHisOrderResources record);
}