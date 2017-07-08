package com.asiainfo.dtdt.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.dtdt.interfaces.order.IOrderService;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月28日 下午9:15:51 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Controller
@RequestMapping(value="/product",method=RequestMethod.POST)
public class OrderController extends BaseController{
	
	private Logger logger = Logger.getLogger(OrderController.class);
	
	@Resource
	private IOrderService orderService;
	
	/**
	* @Title: OrderController 
	* @Description: (订购定向流量) 
	* @param request
	* @param response
	* @return
	* @throws IOException        
	* @throws
	 */
	/*@RequestMapping(value="/pre-order",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String order(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String orderJson = sb.toString(); //接收到通知信息。
        logger.info("pre-order param data:"+orderJson);
        String result = orderService.preOrder(orderJson);
		return result;
	}*/
	
	/**
	* @Title: OrderController 
	* @Description: (这里用一句话描述这个方法的作用) 
	* @param request
	* @param response
	* @return
	* @throws IOException        
	* @throws
	 */
	@RequestMapping(value="/pre-order",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String order(HttpServletRequest request, HttpServletResponse response) throws IOException{
		getHeaderCommonData(request);
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String orderJson = sb.toString(); //接收到通知信息。
        logger.info("pre-order param data:"+orderJson);
        String result = orderService.forwardOrder(orderJson);
		logger.info("pre-order return data:"+result);
		return result;
	}
	
	@RequestMapping(value="/postfix-order",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String postfixOrder(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		String orderJson = sb.toString(); //接收到通知信息。
		logger.info("pre-order param data:"+orderJson);
		String result = orderService.postfixOrder(orderJson);
		return result;
	}
	
	@RequestMapping(value="/batch/postfix-order",produces = "application/json; charset=utf-8")
	@ResponseBody
	public String batchPostfixOrder(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		String orderJson = sb.toString(); //接收到通知信息。
		logger.info("pre-order param data:"+orderJson);
		String result = orderService.batchPostfixOrder(orderJson);
		return result;
	}
	
	/**
	* @Title: OrderController 
	* @Description: (退订定向流量) 
	* @param request
	* @param response
	* @return
	* @throws IOException        
	* @throws
	 */
	@RequestMapping(value = "/closeOrder", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String closeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String orderJson = sb.toString();
        logger.info("closeOrder param data:" + orderJson);
        return orderService.closeOrder(orderJson, request.getHeader("appkey"), request.getHeader("partnerCode"));
	}
	
	/**
	* @Title: OrderController 
	* @Description: (退订定向流量)-new 
	* @param request
	* @param response
	* @return
	* @throws IOException        
	* @throws
	 */
	@RequestMapping(value = "/closeOrderNew", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String closeOrderNew(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String orderJson = sb.toString();
        logger.info("closeOrderNew param data:" + orderJson);
        return orderService.closeOrderNew(orderJson, request.getHeader("appkey"), request.getHeader("partnerCode"));
	}
}
