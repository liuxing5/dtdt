package com.asiainfo.dtdt.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
* @ClassName: SignUtil
* @Description: 签名生成工具类
*
 */
public class SignUtil {
	
	/** 
     * 签名算法sign 
     * 	1.将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
			特别注意以下重要规则：
			◆ 参数名ASCII码从小到大排序（字典序）；
			◆ 如果参数的值为空不参与签名；
			◆ 参数名区分大小写；
			◆ 验证签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。
		2.在stringA最后拼接上key得到新字符串，并对新字符串进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign。
     * @param characterEncoding 
     * @param parameters 
     * @return 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })  
	public static String createSign(Map<String, Object> parameters, String characterEncoding)
	{
		StringBuffer sb = new StringBuffer();
		Collection<String> keyset = parameters.keySet(); 
		List list = new ArrayList<String>(keyset);
		
		//1.排序	（ 参数名ASCII码从小到大排序（字典序））
		Collections.sort(list);
		//2.拼接 eg：key=value&key=value
		for(int i=0;i<list.size();i++){
			Object v = parameters.get(list.get(i));
			Object k = list.get(i);
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))//（如果参数的值为空不参与签名）
			{
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + "appSecret");//appSecret:本地的
		//3.MD5加密，大写
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    public static String createSign(JSONObject parameters, String characterEncoding)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("appkey").append(parameters.getString("appkey"));
		parameters.remove("appkey");
		String secret = parameters.getString("secret");
		parameters.remove("secret");

		Collection<String> keyset = parameters.keySet();
		List list = new ArrayList<String>(keyset);
		//1.排序	（ 参数名ASCII码从小到大排序（字典序））
		Collections.sort(list);
		//2.拼接 eg：key=value&key=value
		for (int i = 0; i < list.size(); i++)
		{
			Object k = list.get(i);
			Object v = parameters.get(k);
			
			if (null != v && !"".equals(v) && !"appSignature".equals(k)
					&& !"key".equals(k))//（如果参数的值为空不参与签名）
			{
				try
				{
					JSONArray array = parameters.getJSONArray(k.toString());
					sb.append(k.toString());
					for(int j=0; j<array.size(); j++)
					{
						sb.append(array.get(j).toString());
					}
					continue;
				}
				catch(Exception e)
				{
					//
				}
				
				sb.append(k.toString() + v.toString());
			}
		}
		sb.append(secret);
		//3.MD5加密，大写
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
				.toUpperCase();
		return sign;
	}
    
	public static void main(String[] args)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("partnerCode", 123);
		map.put("appkey", 12345);
		map.put("appsecret", 33333);
		System.out.println(createSign(map, "utf-8"));
		
		JSONObject parameters = new JSONObject();
		
		/*parameters.put("partnerCode", "1234543245");
		parameters.put("appkey", "fwerh4356ytrt54");
		parameters.put("timestamp", "1499235630342");
		parameters.put("secret", "ewer5retyt");
		//parameters.put("phone", "18710728340");
		parameters.put("phone", "18516222335");
		System.out.println(createSign(parameters, "utf-8"));*/
		
		/**
		 * {
    "phone": "18610728340",
    "productCode": "100101",
    "partnerOrderId": "3456432134567543456750",
    "orderMethod": "1",
    "partnerCode": "1234543245",
    "appkey": "fwerh4356ytrt54",
    "timestamp": "1499443544141",
    "appSignature": "40BB9846E00D1D1C69B851264315BF8E"
264315BF8E"
}
		 */
		/*parameters.put("phone", "18610728340");
		parameters.put("productCode", "100101");
		parameters.put("partnerCode", "1234543245");
		parameters.put("partnerOrderId", "3456432134567543456750");
		parameters.put("orderMethod", "1");
		parameters.put("appkey", "fwerh4356ytrt54");
		parameters.put("timestamp", "1499443544141");
		parameters.put("secret", "ewer5retyt");*/
		
		
		/**
		 * {
    "partnerCode": "1234543245",
    "appkey": "fwerh4356ytrt54",
    "timestamp": "1499236715439",
    "appSignature": "350FE98995A09E5EE05E3E1B493E6488",
    "phones": [
        "18610728340",
        "18610728340",
        "18610728340",
        "18610728340"
    ],
    "productCode": "100101",
    "orderMethod": "1",
    "partnerOrderId": "345678907777777"
}

		 */
		parameters.put("productCode", "100101");
		parameters.put("partnerCode", "1234543245");
		parameters.put("partnerOrderId", "345678907777777");
		parameters.put("orderMethod", "1");
		parameters.put("appkey", "fwerh4356ytrt54");
		parameters.put("timestamp", "1499236715439");
		parameters.put("secret", "ewer5retyt");
		parameters.put("phones", "[\"18610728340\",\"]");
		
		
		System.out.println(createSign(parameters, "utf-8"));
		System.out.println(new Date().getTime());
		
	}
}
