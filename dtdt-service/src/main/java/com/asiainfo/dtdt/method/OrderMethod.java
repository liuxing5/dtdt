package com.asiainfo.dtdt.method;


import lombok.extern.log4j.Log4j2;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.DateUtil;
import com.asiainfo.dtdt.common.RestClient;
import com.asiainfo.dtdt.common.UuidUtil;
import com.asiainfo.dtdt.common.request.HttpClientUtil;
import com.asiainfo.dtdt.common.util.MD5Util;
import com.asiainfo.dtdt.config.woplat.WoplatConfig;


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
	
	public static WoplatConfig woplatConfig = new WoplatConfig();
	
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
	public static String order(String msisdn,String productCode,String subscriptionTime,String orderChannel){
		log.info("**********请求沃家总管进行定向流量订购开始***********");
		JSONObject jsonObject = new JSONObject();
		String result = null;
		try {
			jsonObject.put("seq", UuidUtil.generateUUID());
//			jsonObject.put("appId", woplatConfig.getWoAppId());
			jsonObject.put("appId", Constant.APPID);
			jsonObject.put("operType", 1);
			jsonObject.put("msisdn", msisdn);
			jsonObject.put("productId", productCode);
			jsonObject.put("subscriptionTime", subscriptionTime);
			jsonObject.put("orderMethod",orderChannel);
			String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
			jsonObject.put("timeStamp", timeStamp);
//			String signStr = woplatConfig.getWoAppId()+msisdn+timeStamp+woplatConfig.getWoAppKey();
			String signStr = Constant.APPID+msisdn+timeStamp+Constant.APPKEY;
			jsonObject.put("appSignature", MD5Util.MD5Encode(signStr));
			log.info("post wojia order param:"+jsonObject.toString());
//			result = RestClient.doRest(woplatConfig.getOrderUrl(), "POST", jsonObject.toString());
			result = RestClient.doRest(Constant.ORDER_URL, "POST", jsonObject.toString());
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
	public static String closeOrder(String msisdn,String productId,String orderId,String subscriptionTime,String orderMethod){
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
	public static String queryOrder(String msisdn,String orderId){
		log.info("**********请求沃家总管进行定向流量查询订购信息开始***********");
		JSONObject jsonObject = new JSONObject();
		String result = null;
		try {
			jsonObject.put("seq", UuidUtil.generateUUID());
			jsonObject.put("appId", woplatConfig.getWoAppId());
			jsonObject.put("msisdn", msisdn);
			jsonObject.put("orderId", orderId);
			String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
			jsonObject.put("timeStamp", timeStamp);
			String signStr = woplatConfig.getWoAppId()+msisdn+timeStamp+woplatConfig.getWoAppKey();
			jsonObject.put("appSignature", MD5Util.MD5Encode(signStr));
			log.info("wojia post queryOrder param:"+jsonObject.toString());
			result = HttpClientUtil.httpPost(woplatConfig.getQueryOrder(), jsonObject);
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
		System.out.println(order("18516222334", "cc0ad6fb167742e185d85f511e26c80d", "201706271303010201", "1"));
//		System.out.println(closeOrder( "18516222334", "1000", "3453445467665434567", "201706271303010201", "1"));
	}
}
