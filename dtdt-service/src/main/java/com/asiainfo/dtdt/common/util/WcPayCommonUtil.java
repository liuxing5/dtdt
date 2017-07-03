package com.asiainfo.dtdt.common.util;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import com.asiainfo.dtdt.common.MD5Util;


/**
* @ClassName: WcPayCommonUtil 
* @Description: (微信支付生成签名工具类) 
* @author xiangpeng 
* @date 2017年6月30日 下午2:43:26 
*
 */
@SuppressWarnings("all")
public class WcPayCommonUtil {
	
    //定义签名，微信根据参数字段的ASCII码值进行排序 加密签名,故使用SortMap进行参数排序
    public static String createSign(String characterEncoding,Map<String,String> parameters,String wechat_partner_key){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        String signTemp = sb.append("key=").append(wechat_partner_key).toString();
        
        String sign = MD5Util.MD5Encode(signTemp).toUpperCase();
        return sign;
    }
    
    //将封装好的参数转换成Xml格式类型的字符串
    public static String getRequestXml(Map<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if("sign".equalsIgnoreCase(k)){

            }
            /*else if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }*/
            else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("<"+"sign"+">"+parameters.get("sign")+"</"+"sign"+">");
        sb.append("</xml>");
        return sb.toString();
    }
    
    //将map转为xml
    public static String map2XML(Map<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
        }
        sb.append("</xml>");
        return sb.toString();
    }
    
    public static void weChatPayResponse(String return_code,String return_msg,HttpServletResponse response) throws Exception{
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
	    response.setContentType("UTF-8");
	    PrintWriter printWriter = response.getWriter();
	    Map<String,String> map = new TreeMap<String,String>();
	    map.put("return_code", return_code);
	    map.put("return_msg", return_msg);
		printWriter.write(WcPayCommonUtil.map2XML(map));
		printWriter.flush();
		printWriter.close();
    }
}
