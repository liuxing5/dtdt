package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.ProductPrice;

public interface ProductPriceMapper {
	
	int deleteByPrimaryKey(Long id);

	int insert(ProductPrice record);

	int insertSelective(ProductPrice record);

	ProductPrice selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ProductPrice record);

	int updateByPrimaryKey(ProductPrice record);

}