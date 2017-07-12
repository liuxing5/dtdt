package com.asiainfo.dtdt.task.operation;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.asiainfo.dtdt.task.service.IOrderRecordProccessService;

/**
* @ClassName: OrderRecordProccessTask 
* @Description: 月底解除产品关系：移动到历史表
* @author liuxing5
* @date 2017年7月10日 下午9:10:28 
 */
@Component
public class OrderRecordProccessTask {
	
	@Resource
	IOrderRecordProccessService orderRecordProccessService;
	
	@PostConstruct
    public void init() {
		orderRecordProccessService.closeOrderTask();
    }

	//每月月底/次月月初  执行
	@Scheduled(cron = "0 0 0 1 * ?")
    public void refreshTask() {
		orderRecordProccessService.closeOrderTask();
    }
}
