package com.asiainfo.dtdt.service.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.service.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements IProductService {
	
	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductMapper productMapper;
	
	public List<Product> getProductList() {
		logger.info("ProductServiceImpl getProductList()");
		
		try {
			List<Product>  test = new ArrayList<Product>();
			test.add(productMapper.selectByPrimaryKey(0l));
			 return test;
		} catch (Exception e) {
			return null;
		}
		
	}

}
