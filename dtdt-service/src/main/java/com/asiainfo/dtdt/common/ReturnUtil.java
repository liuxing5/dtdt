package com.asiainfo.dtdt.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
* @ClassName: ReturnUtil 
 */
public class ReturnUtil {
	
	//JSON.toJSONStringWithDateFormat(new Date(), "yyyy-MM-dd HH:mm:ss")
	
	private static SerializeConfig mapping = new SerializeConfig();
	
	private static String dateFormat;  
	
	//返回参数data类型 时间统一格式化为："yyyy-MM-dd HH:mm:ss"
	static {  
	    dateFormat = "yyyy-MM-dd HH:mm:ss";  
	    mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
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
	@SuppressWarnings("static-access")
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
			json.put("data", null);
		}else{
			json.put("data", json.parse(json.toJSONString(data, mapping, SerializerFeature.DisableCircularReferenceDetect)));
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
	@SuppressWarnings("static-access")
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
			json.put("data", null);
		}else{
			json.put("data", json.parse(json.toJSONString(data, mapping, SerializerFeature.DisableCircularReferenceDetect)));
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
	public static String returnJsonList(Object code, String msg, List<?> list)
	{
		JSONObject json = new JSONObject();
		json.put("code", code);
		json.put("msg", msg);
		if (null == list)
		{
			json.put("data", null);
		}else{
			//返回list：禁止fastjson循环引用:SerializerFeature.DisableCircularReferenceDetect
			json.put("data", json.parse(json.toJSONString(list, mapping, SerializerFeature.DisableCircularReferenceDetect)));
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
			json.put("data", null);
		}else{
			json.put("data", json.parse(json.toJSONString(obj, mapping, SerializerFeature.DisableCircularReferenceDetect)));
		}
		return json.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		String code = "0000";
		String msg = "ok";
		List list = new ArrayList<>();
		
		//这里两个list不是循环，就不会有引用
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("cycleType", "0");
		jsonObject1.put("productCode", "100105");
		jsonObject1.put("validTime", "2017-07-05 14:45:42");
		jsonObject1.put("invalidTime", "2017-07-05 14:45:42");
		jsonObject1.put("type", "1");
		
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("cycleType", "0");
		jsonObject2.put("productCode", "100105");
		jsonObject2.put("validTime", "2017-07-05 14:45:42");
		jsonObject2.put("invalidTime", "2017-07-05 14:45:42");
		jsonObject2.put("type", "1");
		
		list.add(jsonObject1);
		list.add(jsonObject2);
		System.out.println(returnJsonList(code, msg, list));
	}
}
