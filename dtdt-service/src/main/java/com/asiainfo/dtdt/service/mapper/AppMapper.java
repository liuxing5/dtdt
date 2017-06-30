package com.asiainfo.dtdt.service.mapper;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.App;


public interface AppMapper {
	
	App queryAppInfo(@Param("appKey")String appKey);
	
}