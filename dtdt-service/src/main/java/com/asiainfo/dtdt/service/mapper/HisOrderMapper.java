package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.HisOrder;

public interface HisOrderMapper {

	HisOrder selectByPrimaryKey(String orderId);
	
    int insertSelective(HisOrder record);

    int updateByPrimaryKeySelective(HisOrder record);

}