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
import com.asiainfo.dtdt.common.util.XmlUtils;
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
        Map map = XmlUtils.doXMLParse(payNotifyXml);
        logger.info("==wcPayNotify==weChatCheckSign success=="+" map:"+map);
        String returnCode = (String) map.get("return_code");
		if (returnCode.equals("SUCCESS")) {
			//验签不通过
			if(!payService.weChatCheckSign(map)){
				logger.error("==wcPayNotify==weChatCheckSign fail=="+" map:"+map);
				WcPayCommonUtil.weChatPayResponse("FAIL","WRONG_SIGN", response);
				return;
			}
				
			String resultCode = (String) map.get("result_code");
			// 获取商户订单号(payId)
			String out_trade_no = (String) map.get("out_trade_no");
			String transaction_id = (String) map.get("transaction_id");
			//判断业务是否已经处理过
			if(payService.isProcessed(out_trade_no)){
				logger.info("==wcPayNotify==isProcessed==微信回调,业务已经处理过");
				WcPayCommonUtil.weChatPayResponse("SUCCESS","OK", response);
				return;
			}
			logger.info("==wcPayNotify==isProcessed==微信回调,业务正常处理");
			
			try {
				logger.info("==wcPayNotify==updatePayOrderStatusAfterPayNotify==begin");
				//数据沉淀
				/**更新充值状态 start**/
				payOrderService.updatePayOrderStatusAfterPayNotify(resultCode, out_trade_no,transaction_id);
				/**更新充值状态 end**/
				logger.info("==wcPayNotify==updatePayOrderStatusAfterPayNotify==success");
				//响应微信
				WcPayCommonUtil.weChatPayResponse("SUCCESS","OK", response);
			} catch (Exception e) {
				logger.error("==wcPayNotify==updatePayOrderStatusAfterPayNotify==exception",e);
				WcPayCommonUtil.weChatPayResponse("FAIL","EXCEPTION", response);
				return;
			}
			if("SUCCESS".equals(resultCode)){
				/**支付成功沉淀订购关系数据 start**/
				String orderId = (String) map.get("out_trade_no");
				orderService.paySuccessOrderDeposition(resultCode, orderId);
				/**支付成功沉淀订购关系数据 end**/
			}
			
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
		//商户订单号
		String out_trade_no = params.get("out_trade_no");
		//判断业务是否已经处理过
		if(payService.isProcessed(out_trade_no)){
			logger.info("==aliPayNotify==isProcessed==支付宝回调,业务已经处理过");
			AliPayCommonUtil.aliPayResponse("success", response);
			return;
		}
		logger.info("==aliPayNotify==isProcessed==支付宝回调,业务正常处理");
		//支付宝交易号  
		String trade_no = params.get("trade_no");
		//交易状态
		String trade_status = params.get("trade_status");
		try {
			logger.info("==aliPayNotify==updateRedpacketPayStatus==begin,trade_status:"+trade_status);
			//数据沉淀
			if("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)){
				orderService.paySuccessOrderDeposition("SUCCESS", out_trade_no);
			}else{
				orderService.paySuccessOrderDeposition("FAIL", out_trade_no);
			}
			logger.info("==aliPayNotify==updateRedpacketPayStatus==success");
			//响应支付宝
			AliPayCommonUtil.aliPayResponse("success", response);
		} catch (Exception e) {
			logger.error("==aliPayNotify==updateRedpacketPayStatus==exception",e);
			//响应支付宝
			AliPayCommonUtil.aliPayResponse("fail", response);
			return;
		}
		
	}
	
}



