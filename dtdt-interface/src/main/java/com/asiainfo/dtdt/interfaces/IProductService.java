package com.asiainfo.dtdt.interfaces;

import java.util.List;


public interface IProductService {
	
	@SuppressWarnings("rawtypes")
	List getProductList();
	
	String queryProduct(String productCode);
}
