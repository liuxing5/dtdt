package com.asiainfo.dtdt.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
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
public class OrderController {
	
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
	@RequestMapping("/pre-order")
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
	@RequestMapping("/closeOrder")
	@ResponseBody
	public String closeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String orderJson = sb.toString(); //接收到通知信息。
        logger.info("notice data:"+orderJson);
        JSONObject jsonObject =new JSONObject(orderJson);
		String seq = jsonObject.getString("seq");
		String pertnerCode = jsonObject.getString("pertnerCode").toString();
		String appKey = jsonObject.getString("appKey").toString();
		String token = jsonObject.getString("token").toString();
		String phone = jsonObject.getString("orderId").toString();
		String productCode = jsonObject.getString("productCode").toString();
		String timeStamp = jsonObject.get("timeStamp").toString();
		String appSignature = jsonObject.get("appSignature").toString();
		/**验签开始*/
		
		/**验签结束*/
		/**处理业务开始*/
		
		/**处理业务结束*/
		JSONObject returnJson = new JSONObject();
		returnJson.append("code", "00000");
		returnJson.append("msg", "成功");
		returnJson.append("seq", seq);
		return returnJson.toString();
	}
}
