package com.asiainfo.dtdt.controller;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Description: controller公共操作
 * Date: 2017年7月5日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
public class BaseController {
	
	protected JSONObject headers;

	public void getHeaderCommonData(HttpServletRequest request)
	{
		headers = new JSONObject();
		headers.put("partnerCode",
				request.getHeader("partnerCode"));
		headers.put("appkey", request.getHeader("appkey"));
		headers.put("timestamp",
				request.getHeader("timestamp"));
		headers.put("appSignature",
				request.getHeader("appSignature"));
	}
}
