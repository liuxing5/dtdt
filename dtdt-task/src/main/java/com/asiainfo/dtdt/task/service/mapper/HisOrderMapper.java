package com.asiainfo.dtdt.task.service.mapper;

import com.asiainfo.dtdt.task.entity.HisOrder;

public interface HisOrderMapper {

	HisOrder selectByPrimaryKey(String orderId);
	
    int updateByPrimaryKeySelective(HisOrder record);

}