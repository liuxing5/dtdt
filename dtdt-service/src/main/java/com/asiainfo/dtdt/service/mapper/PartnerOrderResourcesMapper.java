package com.asiainfo.dtdt.service.mapper;

import java.util.List;

import com.asiainfo.dtdt.entity.PartnerOrderResources;

public interface PartnerOrderResourcesMapper {
    int deleteByPrimaryKey(String batchId);

    int insert(PartnerOrderResources record);

    int insertSelective(PartnerOrderResources record);

    PartnerOrderResources selectByPrimaryKey(String batchId);

    int updateByPrimaryKeySelective(PartnerOrderResources record);
    
    List<PartnerOrderResources> getAll();
    
    List<PartnerOrderResources> getByPartnerCode(String partnerCode);
}