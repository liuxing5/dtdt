package com.asiainfo.dtdt.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.paas.cache.ICache;
import com.asiainfo.dtdt.common.DateUtil;
import com.asiainfo.dtdt.common.IsMobileNo;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.context.Config;
import com.asiainfo.dtdt.interfaces.ICodeService;
import com.huawei.insa2.util.SGIPSendMSGUtil;

@Service("codeServiceImpl")
public class CodeServiceImpl implements ICodeService {
	
	private static final Logger logger = Logger.getLogger(CodeServiceImpl.class);
	
	@Autowired
	private ICache cache;
	
	public String getCode(String phone) {
		logger.info("CodeServiceImpl getCode() phone=" + phone);
		
		//参数校验
		if (StringUtils.isEmpty(phone)) {
			ReturnUtil.returnJsonError("00105", "phone is null", null);
		}
		if (!IsMobileNo.isMobile(phone)) {
			return ReturnUtil.returnJsonInfo("00105", "phone is wrong", null);
		}
		
		try {
			Object obj = cache.getItem(phone + "_phone_code_count");
			int phoneCodeCountInt = 0;
			if (null != obj) {
				phoneCodeCountInt = (Integer) obj;
//				if (phoneCodeCountInt >= Integer.valueOf(Config.SMSCOUNTMAX))
//				{
//					return ReturnUtil.returnJsonError("00106", "code count is over", null);//次数超限，不发短信
//				}
			}
			//生成随机6位验证码
			int code = (int) (Math.random() * 9000 + 100000);
			logger.info("开始给 " + phone + "发送短信验证码：" + code);

			//发短信
			try {
				String msgContent = Config.SMSMESAGECODE.replace(Config.PHONENUMMODE, String.valueOf(code));
				SGIPSendMSGUtil.CONF_PATH = Config.path + "/properties/conf.properties";
				SGIPSendMSGUtil.sendMsg(phone, msgContent);
			} catch (Exception e) {
				logger.error("发送短信错误！", e);
				return ReturnUtil.returnJsonError("00106", "send message wrong", null);
			}
			//设置_code时效 300s；设置发短信次数一天内5次
			cache.addItem(phone + "_phone_code", String.valueOf(code), Integer.valueOf(Config.SMSTIMER));
			cache.addItem(phone + "_phone_code_count", ++phoneCodeCountInt, 
							DateUtil.getSecondsNowToNDateAfter(new Date(), Integer.valueOf(Config.SMSCOUNTTIMER)));
			return ReturnUtil.returnJsonInfo("00000", "ok", String.valueOf(code));
		} catch (Exception e)
		{
			logger.error("获取验证码错误！", e);
			return ReturnUtil.returnJsonError("00107", "getCode wrong", null);
		}
	}

}
