package com.asiainfo.dtdt.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.interfaces.IProductService;

@Service("productServiceImpl")
public class ProductServiceImpl implements IProductService {
	
	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);
	
	@Autowired
//	private ProductMapper productMapper;
	
	public List<Product> getProductList() {
		logger.info("ProductServiceImpl getProductList()");
		
		try {
			
		} catch (Exception e) {
		}
		return null;
	}

}
