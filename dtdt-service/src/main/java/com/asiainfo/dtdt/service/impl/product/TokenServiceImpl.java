package com.asiainfo.dtdt.service.impl.product;
/*package com.asiainfo.dtdt.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.paas.cache.ICache;
import com.asiainfo.dtdt.common.Constant;
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
			ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerCode" + Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isEmpty(appKey)) {
			ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "appKey" + Constant.PARAM_NULL_MSG, null);
		}
		
		//获取token
		String token = null;
		try {
			Object obj = cache.getItem("T_" + partnerCode + appKey);//格式：T_partnerCode_appKey
			if (obj != null){
				token = (String) obj;
			} else {
				//TODO 生成规则：
				token = new Long(System.currentTimeMillis()).toString();
				cache.addItem("T_" + partnerCode + appKey, token, 24*60*60);
			}
		} catch (Exception e) {
			logger.error("Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, e.toString());
		}
		logger.info("token=" + token);
		return ReturnUtil.returnJsonInfo(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, token);
	}

}
*/