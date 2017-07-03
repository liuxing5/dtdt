package com.asiainfo.dtdt.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * Description: http 请求
 */
@SuppressWarnings("deprecation")
public class RestClient {

	private static Log log = LogFactory.getLog(RestClient.class);

	public static String post(String uri, String param) throws Exception
	{
		return doRest(uri, "POST", param);
	}

	/**
	 * 
	 * Description: 发起HTTP请求
	 * @param uri 请求地址
	 * @param methodName 请求方式 GET、POST、PUT、DELTE（目前只实现了POST）
	 * @param param 请求参数
	 * @return
	 * @throws Exception
	 * 
	 * Date: 2016年7月6日 
	 * @author Liuys5
	 */
	public static String doRest(String uri, String methodName, String param) throws Exception
	{
		if ((methodName == null) || ("".equals(methodName)))
		{
			methodName = "POST";
		}
		String data = "";
		log.info(methodName + " rest:" + uri);

		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpPost post = new HttpPost(uri);
		post.setEntity(new StringEntity(param, Charset.forName("UTF-8")));

		try
		{
			HttpResponse response = httpClient.execute(post);
			log.info(response);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200)
			{
				log.error("Method failed: " + response.getStatusLine() + " for url " + uri);
				throw new Exception("Method failed: " + response.getStatusLine() + " for url " + uri);
			}
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = br.readLine()) != null)
			{
				stringBuffer.append(str);
			}
			data = stringBuffer.toString();
		} catch (HttpException e)
		{
			log.error("Please check your provided http address!");
			throw e;
		} catch (IOException e)
		{
			log.error(e);
			throw e;
		} catch (Exception e)
		{
			log.error(e);
			throw e;
		} finally
		{
			try
			{
				httpClient.getConnectionManager().shutdown();
			} catch (Exception ignore)
			{
			}
		}
		log.debug("rest data:" + data);
		return data;
	}

	public static String doRestChargeSys(String uri, Map<String,Object> paramMap)
			throws Exception
	{
		String timestamp = RechargeSignUtil.create_timestamp();
		String sign = RechargeSignUtil.createSign("utf-8",paramMap,timestamp);
		String param = RechargeSignUtil.mapToJsonStr(paramMap);
		String user = (String) paramMap.get("user");
		String data = "";
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(uri);
		post.setEntity(new StringEntity(param, Charset.forName("UTF-8")));
		post.setHeader("Connection", "Keep-Alive");
		post.setHeader("Content-Type","application/json");
		post.setHeader("timestamp", timestamp);
		post.setHeader("Authorization", user + ":" + sign);
		try
		{
			HttpResponse response = httpClient.execute(post);
			log.info(response);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200)
			{
				log.error("Method failed: " + response.getStatusLine() + " for url " + uri);
				throw new Exception("Method failed: " + response.getStatusLine() + " for url " + uri);
			}
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = br.readLine()) != null)
			{
				stringBuffer.append(str);
			}
			data = stringBuffer.toString();
		} catch (HttpException e)
		{
			log.error("Please check your provided http address!");
			throw e;
		} catch (IOException e)
		{
			log.error(e);
			throw e;
		} catch (Exception e)
		{
			log.error(e);
			throw e;
		} finally
		{
			try
			{
				httpClient.getConnectionManager().shutdown();
			} catch (Exception ignore)
			{
			}
		}
		log.debug("rest data:" + data);
		return data;
	}
	
}
