package com.asiainfo.dtdt.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.dtdt.interfaces.IProductService;
/**
 * @author liuixng5
 */
@Controller
@RequestMapping(value="/product", method=RequestMethod.GET)
public class ProductController {
	
	private static final Logger logger = Logger.getLogger(ProductController.class);
	
	@Autowired
	private IProductService productService;
	
	/**
	* @Title: getProductList 
	* @Description: 查询可订购产品列表服务
	* @return String
	* @throws
	 */
	@RequestMapping(value="/getProductList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getProductList() {
		logger.info("ProductController getProductList()");
		return productService.getProductList();
	}
}