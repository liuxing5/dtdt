package com.asiainfo.dtdt.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年7月7日 下午2:41:38 
* @Description: (检验参数) 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public class CheckParam {
	
	public static boolean checkParamIsNull(String param){
		if(StringUtils.isBlank(param) || "".equals(param) || param == null ){
			return true;
		}
		return false;
	}
	
	public static boolean checkParamLength(String param,int maxLength){
		if(param.length() > maxLength){
			return true;
		}
		return false;
	}
	public static String checkParam(String paramsLength,String json){
		StringBuffer sbBuffer  = new StringBuffer();
		String errMsg = null;
		Map<String, Integer> map = JSONObject.parseObject(paramsLength, HashMap.class);
		try {
			JSONObject jsonObject = JSONObject.parseObject(json);
			for (String key : jsonObject.keySet()) {
				if(map.containsKey(key)){
					if(map.get(key) < jsonObject.getString(key).length()){
						sbBuffer.append(key+" ");
						errMsg = "不能超过指定长度！";
					}
				}
			}
		} catch (Exception e) {
			return "非Json格式的参数字符串";
		}
		return sbBuffer.toString()+errMsg;
	}
	
	public static void main(String[] args) {
		String par = "{\"seq\":36,\"orderId\":36,\"partnerCode\":8}";
		String json = "{\"seq\":\"122334344541111\",\"orderId\":\"201719203u3434495111111\"}";
		System.out.println(checkParamIsNull((checkParam(par,json))));
	}
	
	
	
}
