package com.asiainfo.dtdt.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
* @ClassName: RechargeSignUtil 
* @Description: (充值接口签名工具) 
* @author xiangpeng 
* @date 2017年7月3日 下午5:25:12 
*
 */
public class RechargeSignUtil {
	
	private final static Logger log = Logger.getLogger(RechargeSignUtil.class);

	/** 
     * 签名算法sign 
     * @param characterEncoding 
     * @param parameters 
     * @return 
     */  
	public static String createSign(String characterEncoding, Map<String, Object> parameters,String timestamp)
	{
		String a = "POST_DATA=" + mapToJsonStr(parameters) + "&timestamp=" + timestamp + Constant.CHARGEENCRYFACTOR;
		String sign = MD5Util.MD5Encode(a, null).toUpperCase();
		log.info("sign=" + sign);
		return sign;
	}
    
	/**
	 * 
	 * Description: map 转json
	 * @param parameters
	 * @return
	 * 
	 * Date: 2017年3月3日 
	 * @author Fengxg
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static String mapToJsonStr(Map<String, Object> parameters){
    	StringBuffer sb = new StringBuffer();
		Collection<String> keyset= parameters.keySet(); 
		List list=new ArrayList<String>(keyset);
		Collections.sort(list);
		for(int i=0;i<list.size();i++){
			Object v = parameters.get(list.get(i));
			Object k = list.get(i);
			if (null != v && !"".equals(v))
			{
				sb.append("\"" + k + "\":\"" + v + "\",");
			}
		}
		String data = sb.toString();
		return "{" + data.substring(0,data.length()-1) + "}"; 
    }
    
    /**
	 * 获取编码字符集
	 * @param request
	 * @param response
	 * @return String
	 */
	public static String getCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {
		
		if(null == request || null == response) {
			return "gbk";
		}
		
		String enc = request.getCharacterEncoding();
		if(null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}
		
		if(null == enc || "".equals(enc)) {
			enc = "gbk";
		}
		
		return enc;
	}

	public static String create_nonce_str()
	{
		return UuidUtil.generateUUID();
	}

	public static String create_timestamp()
	{
		return Long.toString(System.currentTimeMillis());
	}
	
	//备用
	public static String getNonceStr()
	{
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "GBK");
	}
	
	public static void main(String[] args)
	{
		System.out.println(create_timestamp());
	}
}
