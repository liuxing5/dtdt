package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.BatchOrder;

public interface BatchOrderMapper {
    int deleteByPrimaryKey(String batchId);

    int insert(BatchOrder record);

    int insertSelective(BatchOrder record);

    BatchOrder selectByPrimaryKey(String batchId);

    int updateByPrimaryKeySelective(BatchOrder record);
    
    int updateBatchOrderState(String batchOrderId);

    BatchOrder getBatchOrder(String partnerOrderId);
    
    int getBatchOrderCountByPOID(String partnerOrderId);
}