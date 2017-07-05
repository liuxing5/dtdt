package com.asiainfo.dtdt.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.dtdt.interfaces.IProductService;

import lombok.extern.log4j.Log4j2;
/**
* @ClassName: ProductController 
* @Description: 产品控制类
* @author liuxing5
* @date 2017年7月5日 下午2:27:19 
 */
@Log4j2
@Controller
@RequestMapping(value="/product", method=RequestMethod.GET)
public class ProductController {
	
	@Autowired
	private IProductService productService;
	
	/**
	 * @throws IOException 
	* @Title: getProductList 
	* @Description: 查询可订购产品列表服务
	* @return String
	* @throws
	 */
	@RequestMapping(value="/getProductList")
	@ResponseBody
	public String getProductList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("getProductList request.getHeader('appkey')=" + request.getHeader("appkey"));
		return productService.getProductList(request.getHeader("appkey"));
	}
}