package com.asiainfo.dtdt.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.paas.cache.ICache;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.interfaces.ITokenService;

@Service("tokenServiceImpl")
public class TokenServiceImpl implements ITokenService {
	
	private static final Logger logger = Logger.getLogger(TokenServiceImpl.class);
	
	@Autowired
	private ICache cache;
	
	public String getToken(String partnerCode, String appKey) {
		logger.info("TokenServiceImpl getToken() partnerCode=" + partnerCode + " appKey=" + appKey);
		
		//参数校验
		if (StringUtils.isEmpty(partnerCode)) {
			ReturnUtil.returnJsonError("00101", "partnerCode is null", null);
		}
		if (StringUtils.isEmpty(appKey)) {
			ReturnUtil.returnJsonError("00103", "appKey is null", null);
		}
		
		//获取token：T_partnerCode值_appkey值
		String token = null;
		try {
			Object obj = cache.getItem("T_" + partnerCode + appKey);
			if (obj != null){
				token = (String) obj;
			} else {
				//TODO 生成规则：
				token = new Long(System.currentTimeMillis()).toString();
				cache.addItem("T_" + partnerCode + appKey, token, 24*60*60);
			}
		} catch (Exception e) {
			logger.error("Exception e=" + e);
			return ReturnUtil.returnJsonInfo("99999", "error", e.toString());
		}
		return ReturnUtil.returnJsonInfo("00000", "success", token);
	}

}
