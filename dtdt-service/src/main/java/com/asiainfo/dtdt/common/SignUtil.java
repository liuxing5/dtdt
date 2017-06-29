package com.asiainfo.dtdt.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		sb.append("key=" + "appKey");//appKey
		//3.MD5加密，大写
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
    
	public static void main(String[] args)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pertnerCode", 123);
		map.put("appkey", 12345);
		map.put("appsecret", 33333);
		System.out.println(createSign(map, "utf-8"));
	}
}
