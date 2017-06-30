package com.asiainfo.dtdt.service.impl.order;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.entity.OrderRecord;
import com.asiainfo.dtdt.interfaces.order.IOrderRecordService;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 下午5:09:44 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
public class OrderRecordServiceImpl implements IOrderRecordService {

	private static final Logger logger = Logger.getLogger(OrderRecordServiceImpl.class);
	
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	
	/**
	 * (非 Javadoc) 
	* <p>Title: queryOrderRecordByParam</p> 
	* <p>Description: 查询订购关系信息</p> 
	* @param productCode
	* @param mobilephone
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IOrderRecordService#queryOrderRecordByParam(java.lang.String, java.lang.String)
	 */
	public String queryOrderRecordByParam(String productCode, String mobilephone) {
		logger.info("orderRecordServiceImpl queryOrderRecordByParam interface param:{productCode:"+productCode+",mobilephone:"+mobilephone+"}");
		OrderRecord orderRecord = orderRecordMapper.queryOrderRecordByParam(productCode, mobilephone);
		logger.info("orderRecordServiceImpl queryOrderRecordByParam interface return data:"+JSONObject.toJSONString(orderRecord));
		return JSONObject.toJSONString(orderRecord);
	}

}
