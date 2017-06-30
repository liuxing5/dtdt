package com.asiainfo.dtdt.service.impl.order;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.entity.WoplatOrder;
import com.asiainfo.dtdt.interfaces.order.IWoplatOrderService;
import com.asiainfo.dtdt.service.mapper.WoplatOrderMapper;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 下午5:06:24 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
public class WoplatOrderServiceImpl implements IWoplatOrderService {

	private static final Logger logger = Logger.getLogger(WoplatOrderServiceImpl.class);
	
	@Autowired
	private WoplatOrderMapper woplatOrderMapper;
	
	public String queryWoplatOrderByParam(String productCode, String mobilephone, String state) {
		logger.info("woplatOrderServiceImpl queryWoplatOrderByParam interface param:{productCode:"+productCode+",mobilephone:"+mobilephone+",state:"+state+"}");
		WoplatOrder woplatOrder = woplatOrderMapper.queryWoplatOrderByParam(productCode, mobilephone, state);
		logger.info("woplatOrderServiceImpl queryWoplatOrderByParam interface return data:"+JSONObject.toJSONString(woplatOrder));
		return JSONObject.toJSONString(woplatOrder);
	}
}
