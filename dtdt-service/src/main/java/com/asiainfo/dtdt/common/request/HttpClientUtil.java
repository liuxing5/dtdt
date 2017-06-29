package com.asiainfo.dtdt.common.request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@SuppressWarnings({ "deprecation", "resource" })
	public static String doHttpPost(String url, String jsonParam) throws Exception
	{
		logger.info("doHttpPost url [" + url + "] start");
		logger.info("doHttpPost param :" + jsonParam);
		String result = "";
		HttpClient httpClient = new DefaultHttpClient();
//		请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
//		读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//		httpPost.setHeader("Accept", "application/json");
		StringEntity entity = new StringEntity(jsonParam, "utf-8");//解决中文乱码问题
//		entity.setContentEncoding("UTF-8");
//		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
		if (response != null)
		{
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null)
			{
				result = EntityUtils.toString(resEntity, "UTF-8");
			}
		}
		logger.info("doHttpPost result :" + result);
		return result;
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public static String doHttpGet(String url)
			throws Exception
	{
		logger.info("doHttpGet url [" + url + "] start");
		HttpClient httpClient = new DefaultHttpClient();
//		请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
//		读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		String result = "";
		HttpGet hettpGet = new HttpGet(url);
		hettpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
		hettpGet.setHeader("Accept", "application/json");
		HttpResponse response = httpClient.execute(hettpGet);
		if (response != null)
		{
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null)
			{
				result = EntityUtils.toString(resEntity, "UTF-8");
				result = result.replace("\\", "");
			}
		}
		logger.info("doHttpGet url [" + url + "] end");
		return result;
	}
}
