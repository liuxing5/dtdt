package com.asiainfo.dtdt.controller;

import java.util.List;

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
@RequestMapping(value="/product", method=RequestMethod.POST)
public class ProductController {
	
	private static final Logger logger = Logger.getLogger(ProductController.class);
	
	@Autowired
	private IProductService productService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/getProductList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public List getProductList() {
		logger.info("ProductController getProductList() partnerCode=");
		return productService.getProductList();
	}
}
