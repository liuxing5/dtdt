package com.asiainfo.dtdt.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern.Flag;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.util.AliPayCommonUtil;
import com.asiainfo.dtdt.common.util.WcPayCommonUtil;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayService;


/**
* @ClassName: PayController 
* @Description: (支付回调) 
* @author xiangpeng 
* @date 2017年6月30日 下午2:42:44 
*
 */
@Controller
@RequestMapping(value="/pay",method=RequestMethod.POST)
@SuppressWarnings("all")
public class PayController {
     
	private Logger logger = Logger.getLogger(PayController.class);
	
	@Resource
	private IPayService payService;
	
	@Resource
	private IPayOrderService payOrderService;
	
	@Resource
	private IOrderService orderService;
	
	/**
	* @Title: PayController 
	* @Description: (微信支付回调) 
	* @param request
	* @param response
	* @throws Exception        
	* @throws
	 */
	@RequestMapping(value="/wcPayNotify")
	public void wcPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("payController wcPayNotify  begin");
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String payNotifyXml = sb.toString(); //接收到支付相应信息。
        boolean flag = payService.wcPayNotify(payNotifyXml);
        if(flag){
        	WcPayCommonUtil.weChatPayResponse("SUCCESS","OK", response);
        	return ;
        }else{
        	WcPayCommonUtil.weChatPayResponse("FAIL","EXCEPTION", response);
        	return ;
        }
	}
	
	/**
	* @Title: PayController 
	* @Description: (支付宝支付异步回调) 
	* @param request
	* @param response
	* @throws Exception        
	* @throws
	 */
	@RequestMapping(value="/aliPayNotify")
	public void aliPayNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]:valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		logger.info("aliPayNotify begin,params:"+params);
		//异步通知ID
		String notify_id= params.get("notify_id");
		if(StringUtils.isEmpty(notify_id)){
			logger.error("==aliPayNotify no notify message=="+"params:"+params);
			AliPayCommonUtil.aliPayResponse("no notify message", response);
			return;
		}
		//验签不通过
		if(!payService.aliPayCheckSign(params)){
			logger.error("==aliPayNotify==aliPayCheckSign fail=="+" params:"+params);
			AliPayCommonUtil.aliPayResponse("wrong sign", response);
			return;
		}
		boolean flag = payService.aliPayNotify(params);
		if(flag){
			AliPayCommonUtil.aliPayResponse("success", response);
			return ;
		}else{
			AliPayCommonUtil.aliPayResponse("fail", response);
			return ;
		}
	}
	
}



