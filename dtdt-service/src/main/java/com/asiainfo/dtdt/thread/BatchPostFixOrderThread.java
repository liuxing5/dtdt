package com.asiainfo.dtdt.thread;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.util.SpringContextUtil;
import com.asiainfo.dtdt.entity.BatchOrder;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
@Log4j2
public class  BatchPostFixOrderThread implements Runnable {
	private IOrderService orderService;
	
	private BatchOrder batchOrder;
	public BatchPostFixOrderThread(BatchOrder batchOrder) {
		this.batchOrder = batchOrder;
		ApplicationContext context = SpringContextUtil.getApplicationContext();  
		orderService = (IOrderService) context.getBean("orderServiceImpl");
	}

	@Override
	public void run() {
		splitBatchOrder();
	}
	
	private void splitBatchOrder(){
		log.info("batchOrder thread {} start",batchOrder.toString());
		if(StringUtils.isNotBlank(batchOrder.getMobilephones())){
			JSONArray ja = JSONObject.parseArray(batchOrder.getMobilephones());
			int size = ja.size();
			if(size > 0 ){
				String phoneNum = null; 
				JSONObject json = new JSONObject();
//				json.put("seq", "");
				json.put("partnerCode", batchOrder.getPartnerCode());
				json.put("appkey", batchOrder.getAppKey());
				json.put("productCode", batchOrder.getProductCode());
				json.put("orderMethod", "1");
				json.put("partnerOrderId", batchOrder.getPartnerOrderId());
				json.put("isBatch", true);
				for(int i = 0;i < size; i++){
					phoneNum = (String) ja.get(i);
					log.debug("for batchOrder {}|{}|",batchOrder.getBatchId(),phoneNum);
					if(StringUtils.isNotBlank(phoneNum)){
						json.put("phone", phoneNum);
						String result = orderService.postfixOrder(json.toJSONString());
						log.info("batchOrderId|{}|{}|",batchOrder.getBatchId(),result);
					}
					phoneNum = null;
				}
			}
		}
	}

}
