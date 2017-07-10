package com.asiainfo.dtdt.task.service.mapper;

import com.asiainfo.dtdt.task.entity.PartnerHisOrderResources;

public interface PartnerHisOrderResourcesMapper {
    int deleteByPrimaryKey(String batchId);

    int insert(PartnerHisOrderResources record);

    int insertSelective(PartnerHisOrderResources record);

    PartnerHisOrderResources selectByPrimaryKey(String batchId);

    int updateByPrimaryKeySelective(PartnerHisOrderResources record);
    
    int copyFromInstance(String batchId);
}