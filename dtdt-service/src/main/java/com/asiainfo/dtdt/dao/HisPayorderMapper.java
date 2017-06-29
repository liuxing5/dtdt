package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.HisPayorder;

public interface HisPayorderMapper {
    int deleteByPrimaryKey(String payId);

    int insert(HisPayorder record);

    int insertSelective(HisPayorder record);

    HisPayorder selectByPrimaryKey(String payId);

    int updateByPrimaryKeySelective(HisPayorder record);

    int updateByPrimaryKey(HisPayorder record);
}