package com.asiainfo.dtdt.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.asiainfo.dtdt.entity.Product;

public interface ProductMapper {
	
	Product queryProduct(@Param("productCode")String productCode);
	
	Product selectByPrimaryKey(Long id);
	
	/**
	* @Title: getProductList 
	* @Description: 查询可订购产品列表服务
	* @return List
	* @throws
	 */
	List<Product> getProductList();
	
}