package com.asiainfo.dtdt.service.impl.product;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.service.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements IProductService {
	
	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class);
	
	@Autowired
	private ProductMapper productMapper;
	
	/**
	* @Title: getProductList 
	* @Description: 查询可订购产品列表服务
	* @return List
	* @throws
	 */
	public String getProductList() {
		logger.info("ProductServiceImpl getProductList()");
		
		try {
			List<Product> list = productMapper.getProductList();
			return ReturnUtil.returnJsonList(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, list);
		} catch (Exception e) {
			logger.info("ProductServiceImpl getProductList() Exception e" + e);
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
	}

	/**
	* @Title: queryProduct 
	* @Description: 根据产品 编码查询产品
	* @return String
	* @throws
	 */
	public String queryProduct(String productCode) {
		logger.info("ProductServiceImpl queryProduct param:productCode="+productCode);
		
		try {
			Product product = productMapper.queryProduct(productCode);
			if(product == null){
				return null;
			}
			return JSONObject.toJSONString(product);
		} catch (Exception e) {
			logger.info("ProductServiceImpl queryProduct() Exception e" + e);
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
	}
}
