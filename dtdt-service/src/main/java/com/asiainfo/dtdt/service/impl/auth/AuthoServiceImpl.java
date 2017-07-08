package com.asiainfo.dtdt.service.impl.auth;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.util.SignUtil;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.Partner;
import com.asiainfo.dtdt.entity.ResponseCode;
import com.asiainfo.dtdt.entity.ResponseData;
import com.asiainfo.dtdt.interfaces.IAuthoService;
import com.asiainfo.dtdt.service.mapper.AppMapper;
import com.asiainfo.dtdt.service.mapper.PartnerMapper;

@Service
@Log4j
public class AuthoServiceImpl implements IAuthoService
{

	
	@Autowired
	private AppMapper appMapper;
	@Autowired
	private PartnerMapper partnerMapper;
	
	public String getSMSCode(String phoneNum)
	{
		return "hello elf!";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseData<?> validPartnerAndAPP(JSONObject reqjson)
	{
		try
		{
			String partnerCode = reqjson.getString("partnerCode");
			Partner partner = partnerMapper.getByPartnerCode(partnerCode);
			if (null == partner
					|| StringUtils.isEmpty(partner.getPartnerCode()))
			{
				return new ResponseData("20000", "partnerCode或appkey非法！");
			}
			String appkey = reqjson.getString("appkey");
			App appinfo = appMapper.queryAppInfo(appkey);
			if (null == appinfo || StringUtils.isEmpty(appinfo.getAppKey()))
			{
				return new ResponseData("20000", "partnerCode或appkey非法！");
			}

			if (!appinfo.getPartnerId().equals(partner.getPartnerId()))
			{ 
				return new ResponseData("20000", "partnerCode或appkey非法！");
			}

			// 验证签名
			JSONObject signJson = (JSONObject) reqjson.clone();
			signJson.put("secret", appinfo.getSecret());
			String singstr = SignUtil.createSign(signJson, "UTF-8");
			if (!StringUtils.pathEquals(singstr,
					reqjson.getString("appSignature")))
			{
				return new ResponseData(Constant.PARAM_WRONG_SIGN, "签名错误！");
			}
		} catch (Exception e)
		{
			log.error("参数校验错误！", e);
			return new ResponseData(ResponseCode.COMMON_ERROR_CODE, "系统错误");
		}
		return new ResponseData(ResponseCode.COMMON_SUCCESS_CODE, "成功");
	}
	
	

}
