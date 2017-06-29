package com.asiainfo.dtdt.intercepter;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ai.paas.cache.ICache;
import com.alibaba.fastjson.JSONObject;

/**
* @Description: 拦截器：校验token是否存在；合作方验证；应用验证；
* @author liuxing5
*
 */
public class Interceptor implements HandlerInterceptor {

	private static final Log logger = LogFactory.getLog(Interceptor.class);
	private static final String[] IGNORE_URI = {"/token/getToken"};//过滤第一次获取token

	@Autowired
	private ICache cache;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		logger.info("Interceptor preHandle()");
		Writer out = null;
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			
			String tokenGet = request.getParameter("token");
			String partnerCode = request.getParameter("partnerCode");//合作方
			String appKey = request.getParameter("appKey");//应用
			
			logger.info("Interceptor preHandle() tokenGet=" + tokenGet);
			String url = request.getServletPath();
			
			//第一次获取token过滤掉
			if (needFilter(url)) {
				String tokenCache = null;
				JSONObject result = new JSONObject();
				if (StringUtils.isNotEmpty(tokenGet)) {
					tokenCache = (String) cache.getItem("T_" + partnerCode + appKey);
					if (tokenGet.equals(tokenCache))
					{
						result.put("code", "10011");
						result.put("msg", "合作方ID/应用ID验证不通过");
						out = response.getWriter();
						out.write(result.toString());
						return false;
					}
					if (null != tokenCache) {
						// 刷新token存活时间
						cache.addItem("T_" + partnerCode + appKey, tokenCache, 24*60*60);
					}
				} else {
					result.put("code", "10012");
					result.put("msg", "token失效");
					out = response.getWriter();
					out.write(result.toString());
					return false;
				}
			}
			logger.info("return true");
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * Description: 判断是否需要过滤
	 * @param url
	 * @return
	 */
	private boolean needFilter(String url) {
		if (StringUtils.isNotEmpty(url)) {
			for (String ignoreUri : IGNORE_URI) {
				if (url.contains(ignoreUri)) {
					return false;
				}
			}
		}
		return true;
	}
}
