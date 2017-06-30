package com.asiainfo.dtdt.common.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;


/** 
* @author 作者 : xiangpeng
* @date 创建时间：2016年12月13日 下午5:15:14 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/** 
     * httpPost 
     * @param url  路径 
     * @param jsonParam 参数 
     * @return 
     */  
	
    public static String httpPost(String url,JSONObject jsonParam){ 

        return httpPost(url, jsonParam,false);  
    }
    
    
    @SuppressWarnings("deprecation")
	public static String httpPost(String urlPath,JSONObject jsonParam, boolean noNeedResponse) {
    	//post请求返回结果 httpPost(url, jsonParam,false)
    	String jsonResult = null;
    	String param = jsonParam.toString();
    	try {
    		logger.info("jsonParam:"+param);
    		URL url=new URL(urlPath);
    	    HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
    	    httpConn.setConnectTimeout(60000);
    	    httpConn.setReadTimeout(60000);
    	    //设置参数
    	    httpConn.setDoOutput(true);   //需要输出
    	    httpConn.setDoInput(true);   //需要输入
    	    httpConn.setUseCaches(false);  //不允许缓存
    	    httpConn.setRequestMethod("POST");   //设置POST方式连接
    	    //设置请求属性
    	    httpConn.setRequestProperty("Content-Type", "application/json");
//    	    httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
    	    httpConn.setRequestProperty("Charset", "UTF-8");
    	    //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
    	   // httpConn.connect();
    	    //建立输入流，向指向的URL传入参数
    	    DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream());
    	    dos.writeBytes(URLEncoder.encode(param));
    	    dos.flush();
    	    dos.close();
    	    //获得响应状态
    	    int resultCode=httpConn.getResponseCode();
    	    if(HttpURLConnection.HTTP_OK==resultCode){
    	      StringBuffer sb=new StringBuffer();
    	      String readLine=new String();
    	      BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
    	      while((readLine=responseReader.readLine())!=null){
    	        sb.append(readLine).append("\n");
    	      }
    	      responseReader.close();
    	      jsonResult = sb.toString();
    	    } 
		} catch (SocketTimeoutException e) {
			logger.error("post 请求超时："+urlPath+e.getMessage());
			logger.error("post 请求超时："+e);
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("code", Constant.ERROR_CODE);
			json.put("msg", "请求超时"+Constant.ERROR_MSG);
			return json.toString();
		}catch (IllegalArgumentException e) {
			logger.error("post 请求参数异常："+urlPath+e.getMessage());
			logger.error("post 请求参数异常："+e);
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("code", Constant.ERROR_CODE);
			json.put("msg", "请求参数"+Constant.ERROR_MSG);
			return json.toString();
		} catch (Exception e) {
			logger.error("post 请求接口异常："+urlPath+e.getMessage());
			logger.error("post 请求接口异常："+e);
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("code", Constant.ERROR_CODE);
			json.put("msg", "接口通讯"+Constant.ERROR_MSG);
			return json.toString();
		}
    	
    	return jsonResult;
    }
}
