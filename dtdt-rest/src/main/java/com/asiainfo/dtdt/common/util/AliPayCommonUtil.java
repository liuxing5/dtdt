package com.asiainfo.dtdt.common.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
* @ClassName: AliPayCommonUtil 
* @Description: (支付宝支付工具类) 
* @author xiangpeng 
* @date 2017年6月30日 下午2:42:29 
*
 */
public class AliPayCommonUtil {
	public static void aliPayResponse(String return_msg,HttpServletResponse response) throws Exception{
    	response.setHeader("Content-type", "text/html;charset=UTF-8");
	    response.setContentType("UTF-8");
	    PrintWriter printWriter = response.getWriter();
		printWriter.write(return_msg);
		printWriter.flush();
		printWriter.close();
    }
}
