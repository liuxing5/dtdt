package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.Param;

public interface ParamMapper {
    int insert(Param record);

    int insertSelective(Param record);
}