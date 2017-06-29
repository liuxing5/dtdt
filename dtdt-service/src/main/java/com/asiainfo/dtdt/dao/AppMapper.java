package com.asiainfo.dtdt.dao;

import com.asiainfo.dtdt.entity.App;

public interface AppMapper {
    int deleteByPrimaryKey(Long appId);

    int insert(App record);

    int insertSelective(App record);

    App selectByPrimaryKey(Long appId);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);
}