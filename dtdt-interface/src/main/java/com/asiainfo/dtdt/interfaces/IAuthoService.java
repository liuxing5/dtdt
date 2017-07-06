package com.asiainfo.dtdt.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.entity.ResponseData;


public interface IAuthoService {

	public String getSMSCode(String phoneNum);
	
	/**
	 * 
	 * Description: 校验合作方是否存在，app是否存在，二者是否配对，签名是否正确
	 * @param partnerCode
	 * @param appkey
	 * @return
	 * 
	 * Date: 2017年7月4日 
	 * @author Liuys5
	 */
	public ResponseData<?> validPartnerAndAPP(JSONObject reqjson);
}
