package com.asiainfo.dtdt.interfaces;

public interface IProductService {
	
	/**
	* @Title: getProductList 
	* @Description: 查询可订购产品列表服务
	* @return String
	* @throws
	 */
	String getProductList();
	
	String queryProduct(String productCode);
}
