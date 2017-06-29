package com.asiainfo.dtdt.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月28日 下午9:15:51 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Controller
@RequestMapping(value="/notice",method=RequestMethod.POST)
public class NoticeController {
	
	private Logger logger = Logger.getLogger(NoticeController.class);
	
	@RequestMapping("/all")
	@ResponseBody
	public String notice(HttpServletRequest request, HttpServletResponse response) throws IOException{
		InputStream ins = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins, "ISO-8859-1"));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
        	sb.append(line);
        }
        String notifyJson = sb.toString(); //接收到通知信息。
        logger.info("notice data:"+notifyJson);
        JSONObject jsonObject =new JSONObject(notifyJson);
		String seq = jsonObject.get("seq").toString();
		String appId = jsonObject.get("msisdn").toString();
		String orderId = jsonObject.get("orderId").toString();
		String productId = jsonObject.get("productId").toString();
		String subscriptionTime = jsonObject.get("subscriptionTime").toString();
		String feeStartDate = jsonObject.get("feeStartDate").toString();
		String validExpireDate = jsonObject.get("validExpireDate").toString();
		String orderDesc = jsonObject.get("orderDesc").toString();
		String productAttrValues = jsonObject.get("productAttrValues").toString();
		JSONArray jsonArray = new JSONArray(productAttrValues);
		Map<String,Object> mapList  = new HashMap();
		List list  = new ArrayList();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			mapList.put("attrTypeId",json.getString("attrTypeId"));//产品编码
			mapList.put("attrValue",json.getString("attrValue"));//产品属性值
			mapList.put("attrDesc",json.getString("attrDesc"));//产品属性描述
			list.add(mapList);
		}
		/**处理业务开始*/
		
		
		/**处理业务结束*/
		JSONObject returnJson = new JSONObject();
		returnJson.append("ecode", "0");
		returnJson.append("emsg", "成功");
		returnJson.append("seq", seq);
		return returnJson.toString();
	}
}
