package com.asiainfo.dtdt.common;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
* @ClassName: ReturnUtil 
 */
public class ReturnUtil {
	
	public static JsonConfig jsonConfig = new JsonConfig();
	
	static void initDate(){
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	* @Title: returnJsonError 
	* @Description: returnJsonError 统一返回给APP端的数据：适用于返回错误提示，data为字符串的说明性文字
	* @param code
	* @param msg
	* @param data
	* @return String
	* @throws
	 */
	public static String returnJsonError(Object code, String msg, String data)
	{
		JSONObject json = new JSONObject();
		if(Constant.SUCCESS_CODE.equals(code)){
			json.put("code", code);
		}else{
			json.put("code", code+"");
		}
		json.put("msg", msg);
		if (null == data)
		{
			json.put("data", "");
		}else{
			json.put("data", data);
		}
		return json.toString();
	}
	
	/**
	 * @Title: returnJsonInfo 
	 * @Description: returnJsonError 统一返回给APP端的数据：适用于返回错误提示，data为字符串的说明性文字
	 * @param code
	 * @param msg
	 * @param data
	 * @return String
	 * @throws
	 */
	public static String returnJsonInfo(Object code, String msg, String data)
	{	
		JSONObject json = new JSONObject();
		if(Constant.SUCCESS_CODE.equals(code)){
			json.put("code", code);
		}else{
			json.put("code", code+"");
		}
		json.put("msg", msg);
		if (null == data)
		{
			json.put("data", "");
		}else{
			json.put("data", data);
		}
		return json.toString();
	}
	
	/**
	* @Title: returnJsonObj 
	* @Description: returnJsonObj 统一返回给APP端的数据：适用于返回错误提示，data为对象
	* @param code
	* @param msg
	* @param obj
	* @return String
	* @throws
	 */
	@SuppressWarnings("static-access")
	public static String returnJsonObj(Object code, String msg, Object obj)
	{
		initDate();
		JSONObject json = new JSONObject();
		if(Constant.SUCCESS_CODE.equals(code)){
			json.put("code", code);
		}else{
			json.put("code", code+"");
		}
		json.put("code", code);
		json.put("msg", msg);
		if (null == obj)
		{
			json.put("data", "");
		}else{
			json.put("data", json.fromObject(obj, jsonConfig));
		}
		return json.toString();
	}
	
	
	/**
	* @Title: returnJson 
	* @Description: returnJson 统一返回给APP端的数据：适用于返回错误提示，data为list
	* @param code
	* @param msg
	* @return String
	* @throws
	 */
	@SuppressWarnings("static-access")
	public static String returnJsonList(Integer code, String msg, List<?> list)
	{
		initDate();
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		JSONArray jsonArray = new JSONArray();
		if (null == list)
		{
			json.put("data", "");
		}else{
			json.put("data", jsonArray.fromObject(list, jsonConfig));
		}
		
		return json.toString();
	}
    
}
