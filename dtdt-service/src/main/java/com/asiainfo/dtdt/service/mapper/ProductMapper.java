package com.asiainfo.dtdt.service.mapper;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.Product;

public interface ProductMapper {
	
	Product queryProduct(@Param("productCode")String productCode);
	
	Product selectByPrimaryKey(Long id);
	
}