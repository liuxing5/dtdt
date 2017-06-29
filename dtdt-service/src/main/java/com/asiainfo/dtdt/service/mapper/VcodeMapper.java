package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.Vcode;

public interface VcodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Vcode record);

    int insertSelective(Vcode record);

    Vcode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Vcode record);

    int updateByPrimaryKey(Vcode record);
}