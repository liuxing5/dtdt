package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.Param;

public interface ParamMapper {
    int insert(Param record);

    int insertSelective(Param record);
}