package com.asiainfo.dtdt.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.dtdt.interfaces.order.IQueryOrderService;

/**
* @ClassName: QueryOrderController 
* @Description: 订单查询控制类
* @author liuxing5
* @date 2017年6月30日 下午3:49:39 
*
 */
@Controller
@RequestMapping(value="/queryOrder", method=RequestMethod.GET)
public class QueryOrderController {
	
	private final static Logger logger = Logger.getLogger(QueryOrderController.class);
	
	@Autowired
	private IQueryOrderService queryOrderService;
	
	/**
	* @Title: queryOrder
	* @Description: 查询订购信息服务
	* @return String
	* @throws
	 */
	@RequestMapping(value="/queryOrderRecord", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String queryOrder(HttpServletRequest request) {
		logger.info("OrderController queryOrderRecord() phone=" + request.getParameter("phone"));
		return queryOrderService.queryOrderRecord(request.getParameter("phone"));
	}
	
	/**
	* @Title: queryOrderState 
	* @Description: 查询订单状态服务
	* @return String
	* @throws
	 */
	@RequestMapping(value="/queryOrderState", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String queryOrderState(HttpServletRequest request) {
		logger.info("OrderController queryOrderState() orderId=" + request.getParameter("orderId"));
		return queryOrderService.queryOrderState(request.getParameter("orderId"));
	}
}
