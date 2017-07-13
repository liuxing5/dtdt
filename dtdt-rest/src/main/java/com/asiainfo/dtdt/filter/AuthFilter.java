package com.asiainfo.dtdt.filter;

import io.undertow.servlet.util.IteratorEnumeration;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.awim.bean.SpringContextHolder;
import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.entity.ResponseCode;
import com.asiainfo.dtdt.entity.ResponseData;
import com.asiainfo.dtdt.interfaces.IAuthoService;


/**
 * 
 * Description: 统一认证过滤
 * Date: 2017年7月3日 
 * Copyright (c) 2017 AI
 * 
 * @author Liuyansen
 */
@Log4j2
@Configuration
public class AuthFilter implements Filter{


	private static final String[] IGNORE_URI = {"/test/getSign" ,"/notice"};
	
	private IAuthoService authoService;
	
	private RedisAssistant redis;
	/**
	 * Description:
	 * 
	 * @param filterConfig
	 * @throws ServletException
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	     authoService = SpringContextHolder.getBean(IAuthoService.class);
	     redis = SpringContextHolder.getBean(RedisAssistant.class);
	}
	/**
	 * Description:
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@SuppressWarnings("unused")
	private String charset = "UTF-8"; 
	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		log.info("AuthFilter doFilter()");
		HttpServletRequest hrequest = (HttpServletRequest) request;
		String url = hrequest.getServletPath();
		String requestType = hrequest.getMethod();
		JSONObject requestJson = new JSONObject();

		JSONObject result = new JSONObject();
		PrintWriter out = null;
		if (!isNotFilter(url))
		{
			HashMap<String, String[]> m = new HashMap(
					hrequest.getParameterMap());
			try
			{
				MyRequestWrapper myRequestWrapper = new MyRequestWrapper(
						(HttpServletRequest) request, m);
				if ("POST".equalsIgnoreCase(requestType))
				{
					requestJson = myRequestWrapper.getBody();
				} else
				{
					IteratorEnumeration ite = (IteratorEnumeration) hrequest.getParameterNames();
					while(ite.hasMoreElements())
					{
						String key = ite.nextElement().toString();
						String value = hrequest.getParameter(key);
						requestJson.put(key, value);
					}
					requestJson.put("partnerCode",
							hrequest.getHeader("partnerCode"));
					requestJson.put("appkey", hrequest.getHeader("appkey"));
					requestJson.put("timestamp",
							hrequest.getHeader("timestamp"));
					requestJson.put("appSignature",
							hrequest.getHeader("appSignature"));
				}

				JSONObject checkResult = checkCommonParam(requestJson);
				if (!checkResult.isEmpty())
				{
					log.info("request url:" + url + " ; param check failed:" + checkResult.toJSONString());
					out = response.getWriter();
					response.setCharacterEncoding("UTF-8");
					out.append(checkResult.toString());
					return;
				}

				log.info("request url:" + url + " ; qurestparam:" + requestJson);
				chain.doFilter(myRequestWrapper, response);
			} catch (Exception e)
			{//返回参数解密错误
				log.error("系统异常", e);
				result.put("code", 99999);
				result.put("msg", "系统异常");
				out = response.getWriter();
				out.append(result.toString());
			} finally
			{
				if (out != null)
				{
					out.close();
				}
			}
		} else
		{
			chain.doFilter(hrequest, response);
		}

	}
	
	@SuppressWarnings("rawtypes")
	private JSONObject checkCommonParam(JSONObject requestJson)
	{
		
		JSONObject checkResult = new JSONObject();
		try
		{
			if(null == requestJson || requestJson.isEmpty())
			{
				checkResult.put("code", "10000");
				checkResult.put("msg", "参数为空，请检查参数！");
				return checkResult;
			}
			
			if ((!requestJson.containsKey("partnerCode") || StringUtils
					.isEmpty(requestJson.getString("partnerCode")))
					|| (!requestJson.containsKey("appkey") || StringUtils
							.isEmpty(requestJson.getString("appkey")))
					|| (!requestJson.containsKey("timestamp") || StringUtils
							.isEmpty(requestJson.getString("timestamp")))
					|| (!requestJson.containsKey("appSignature") || StringUtils
							.isEmpty(requestJson.getString("appSignature"))))
			{
				checkResult.put("code", "10000");
				checkResult.put("msg",
						"partnerCode,appkey,timestamp,appSignature必传参数，请检查参数！");
				return checkResult;
			}
			
			// 校验timestamp(配置已秒为单位)
			String validTimeStr = redis.getStringValue(RedisKey.SERVICE_VALID_TIME);
			if(StringUtils.isEmpty(validTimeStr))
			{
				validTimeStr = "60";
				redis.setForever(RedisKey.SERVICE_VALID_TIME, validTimeStr);
			}
			long timestamp = Long.valueOf(requestJson.getString("timestamp"));
			long validTime = Long.valueOf(validTimeStr) * 1000l;
			long nowtimestamp = new Date().getTime();
			if(timestamp > nowtimestamp || (nowtimestamp - timestamp) > validTime)
			{
				checkResult.put("code", "30003");
				checkResult.put("msg", "请求链接失效！");
				return checkResult;
			}
			
			// 校验数据库是否存在，并且配对以及签名
			ResponseData res = authoService.validPartnerAndAPP(requestJson);
			if(ResponseCode.COMMON_SUCCESS_CODE.equals(res.getCode()))
			{
				return checkResult;
			}
			else
			{
				return (JSONObject) JSONObject.toJSON(res);
			}
		}
		catch (Exception e)
		{
			log.error("参数校验错误！", e);
			checkResult.put("code", "99999");
			checkResult.put("msg", "系统错误！");
		}
		
		return checkResult;
	}
	
	private boolean isNotFilter(String url)
	{
		if (!StringUtils.isEmpty(url))
		{
			for (String ignoreUri : IGNORE_URI)
			{
				if (url.contains(ignoreUri) || url.contains("/pay"))
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String[] ag)
	{
		System.out.println(new Date().getTime());
		
		System.out.println(new Date().getTime());
	}

}
