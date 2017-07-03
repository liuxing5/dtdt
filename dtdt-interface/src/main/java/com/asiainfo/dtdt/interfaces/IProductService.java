package com.asiainfo.dtdt.interfaces;

public interface IProductService {
	
	/**
	* @Title: getProductList 
	* @Description: 查询可订购产品列表服务
	* @return String
	* @throws
	 */
	String getProductList();
	
	/**
	 * 
	* @Title: queryProduct 
	* @Description: (根据产品 编码查询产品)
	* @param productCode
	* @return String
	* @throws
	 */
	String queryProduct(String productCode);
}
