package com.asiainfo.dtdt.method;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.RestClient;
import com.asiainfo.dtdt.common.request.HttpClientUtil;
import com.asiainfo.dtdt.common.util.DateUtil;
import com.asiainfo.dtdt.common.util.MD5Util;
import com.asiainfo.dtdt.common.util.UuidUtil;
import com.asiainfo.dtdt.config.woplat.WoplatConfig;

import lombok.extern.log4j.Log4j2;


/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月28日 下午2:07:16 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Log4j2
public class OrderMethod {
	
	@Resource
	public static WoplatConfig woplatConfig;
	
	public static final String APPID = "b3ab31fd-8074-46da-9086-f3fb6184b334";
	public static final String  APPKEY = "d4584789-4423-4884-80cd-5447b6aa19df";
	
	/**沃家总管订购退订接口**/
	public static final String ORDER_URL  			= "http://120.52.120.106:9008/order";
	/**沃家总管查询订购信息接口**/
	public static final String QUERYORDER_URL  		= "http://120.52.120.106:9008/queryorder";
	
	/**
	 * @throws Exception 
	* @Title: OrderMethod 
	* @Description: (订购定向流量方法) 
	* @param operType 受理类型1-订购2-退订
	* @param msisdn 订购号码（联通手机号码）
	* @param productId	订购产品ID
	* @param subscriptionTime 订购时间
	* @param orderChannel 订购渠道1：APP 2：WEB 3：文件接口 4：其他
	* @return    订购ID    
	* @throws
	 */
	public static  String order(String msisdn,String productCode,String subscriptionTime,String orderChannel){
		log.info("**********请求沃家总管进行定向流量订购开始***********");
		JSONObject jsonObject = new JSONObject();
		String result = null;
		try {
			jsonObject.put("seq", UuidUtil.generateUUID());
//			jsonObject.put("appId", woplatConfig.getWoAppId());
			jsonObject.put("appId", APPID);
			jsonObject.put("operType", 1);
			jsonObject.put("msisdn", msisdn);
			jsonObject.put("productId", productCode);
			jsonObject.put("subscriptionTime", subscriptionTime);
			jsonObject.put("orderMethod",orderChannel);
			String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
			jsonObject.put("timeStamp", timeStamp);
//			String signStr = woplatConfig.getWoAppId()+msisdn+timeStamp+woplatConfig.getWoAppKey();
			String signStr = APPID+msisdn+timeStamp+APPKEY;
			jsonObject.put("appSignature", MD5Util.MD5Encode(signStr));
			log.info("post wojia order param:"+jsonObject.toString());
//			result = RestClient.doRest(woplatConfig.getOrderUrl(), "POST", jsonObject.toString());
			result = RestClient.doRest(ORDER_URL, "POST", jsonObject.toString());
			log.info("wojia order return result:"+result);
		} catch (Exception e) {
			log.error("post wojia order error:"+e.getMessage(), e);
			return null;
		}
		log.info("**********请求我家总管进行定向流量订购结束***********");
		return result;
	}
	
	/**
	 * @throws Exception 
	* @Title: OrderMethod 
	* @Description: (退订定向流量方法) 
	* @param operType 受理类型1-订购2-退订
	* @param msisdn 订购号码（联通手机号码）
	* @param productId	订购产品ID
	* @param orderId 订购ID
	* @param subscriptionTime 订购时间
	* @param orderMethod 订购渠道1：APP 2：WEB 3：文件接口 4：其他
	* @return        
	* @throws
	 */
	public  String closeOrder(String msisdn,String productId,String orderId,String subscriptionTime,String orderMethod){
		log.info("**********请求沃家总管进行定向流量退订开始***********");
		JSONObject jsonObject = new JSONObject();
		String result = null;
		try {
			jsonObject.put("seq", UuidUtil.generateUUID());
			jsonObject.put("appId", woplatConfig.getWoAppId());
			jsonObject.put("operType", "2");
			jsonObject.put("msisdn", msisdn);
			jsonObject.put("productId", productId);
			jsonObject.put("orderId", orderId);
			jsonObject.put("subscriptionTime", subscriptionTime);
			jsonObject.put("orderMethod",orderMethod);
			String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
			jsonObject.put("timeStamp", timeStamp);
			String signStr = woplatConfig.getWoAppId()+msisdn+timeStamp+woplatConfig.getWoAppKey();
			jsonObject.put("appSignature", MD5Util.MD5Encode(signStr));
			log.info("post wojia closeOrder param:"+jsonObject.toString());
//			result = HttpClientUtil.httpPost(woplatConfig.getOrderUrl(), jsonObject);
			result = RestClient.doRest(woplatConfig.getOrderUrl(), "POST", jsonObject.toString());
			log.info("wojia closeOrder return result:"+result);
		} catch (Exception e) {
			log.error("post wojia closeOrder error:"+e.getMessage(), e);
			return null;
		}
		log.info("**********请求沃家总管进行定向流量退订结束***********");
		return result;
	}
	
	/**
	 * @throws Exception 
	* @Title: OrderMethod 
	* @Description: (查询定向流量订购信息) 
	* @param appId 应用ID
	* @param operType 受理类型1-订购2-退订
	* @param msisdn 订购号码（联通手机号码）
	* @param productId	订购产品ID
	* @param orderId 订购ID
	* @param subscriptionTime 订购时间
	* @param orderMethod 订购渠道1：APP 2：WEB 3：文件接口 4：其他
	* @return        
	* @throws
	 */
	public static  String queryOrder(String msisdn,String orderId){
		log.info("**********请求沃家总管进行定向流量查询订购信息开始***********");
		JSONObject jsonObject = new JSONObject();
		String result = null;
		try {
			jsonObject.put("seq", UuidUtil.generateUUID());
			jsonObject.put("appId", APPID);
			jsonObject.put("msisdn", msisdn);
			jsonObject.put("orderId", orderId);
			String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
			jsonObject.put("timeStamp", timeStamp);
			String signStr = APPID+msisdn+timeStamp+APPKEY;
			jsonObject.put("appSignature", MD5Util.MD5Encode(signStr));
			log.info("wojia post queryOrder param:"+jsonObject.toString());
			result = RestClient.doRest(QUERYORDER_URL, "POST", jsonObject.toString());
			log.info("wojia queryOrder return result:"+result);
		} catch (Exception e) {
			log.error("post wojia queryOrder error:"+e.getMessage(), e);
			return null;
		}
		log.info("**********请求沃家总管进行定向流量查询订购信息结束***********");
		return result;
	}
	
	public static void main(String[] args) {
//		System.out.println(DateUtil.getSysdateYYYYMMDDHHMMSS());
//		System.out.println(order("18516222334", "fd3cd79e20c14728-984f32dbfa56713c", DateUtil.getSysdateYYYYMMDDHHMMSS(), "1"));
//		System.out.println(closeOrder( "18516222334", "1000", "3453445467665434567", "201706271303010201", "1"));
		System.out.println(queryOrder("18516222334", "231846ff-a973-4705-ad01-026b9c6ebbf7"));
	}
}
