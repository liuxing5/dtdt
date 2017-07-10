package com.asiainfo.dtdt.thread;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.asiainfo.dtdt.common.util.SpringContextUtil;
import com.asiainfo.dtdt.interfaces.order.INoticeService;

import lombok.extern.log4j.Log4j2;
@Log4j2
public class  NoticePartnerOrderThread implements Runnable {
	private INoticeService noticeService;
	
	private String woOrderId ;
	
	private String orderState;
	
	public NoticePartnerOrderThread(String woOrderId,String orderState) {
		this.woOrderId = woOrderId;
		this.orderState = orderState;
		ApplicationContext context = SpringContextUtil.getApplicationContext();  
		noticeService = (INoticeService) context.getBean("noticeServiceImpl");
	}

	@Override
	public void run() {
		NoticePartnerOrder();
	}
	
	private void NoticePartnerOrder(){
		log.info("noticeOrder thread {} start woOrderId=",woOrderId);
		if(StringUtils.isNotBlank(woOrderId)){
			noticeService.optNoticeOrder(orderState, woOrderId);
		}
		log.info("noticeOrder thread {} end woOrderId=",woOrderId);
	}

}
