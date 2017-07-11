package com.asiainfo.dtdt.task.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.asiainfo.dtdt.task.entity.HisOrder;
import com.asiainfo.dtdt.task.entity.HisOrderRecord;
import com.asiainfo.dtdt.task.entity.OrderRecord;
import com.asiainfo.dtdt.task.service.IOrderRecordProccessService;
import com.asiainfo.dtdt.task.service.mapper.HisOrderMapper;
import com.asiainfo.dtdt.task.service.mapper.HisOrderRecordMapper;
import com.asiainfo.dtdt.task.service.mapper.OrderRecordMapper;

import lombok.extern.log4j.Log4j2;

/**
* @ClassName: OrderRecordProccessImpl 
* @Description: 月底解除产品关系：
	* 					1.查询订购关系表中退订状态为20（退订中）的；且为本月月底失效的订购信息；
	* 					2.修改订购关系表状态为22--服务到期， 移动到历史表；
	* 					3.修改订单历史表状态为22--服务到期；
* @author liuxing5
* @date 2017年7月10日 下午9:14:30 
 */
@Service
@Log4j2
public class OrderRecordProccessImpl implements IOrderRecordProccessService{

	@Resource
	private OrderRecordMapper orderRecordMapper;
	
	@Resource
	private HisOrderRecordMapper hisOrderRecordMapper;
	
	@Resource
	private HisOrderMapper hisOrderMapper;
	
	@Override
	public void closeOrderTask()
	{
		try
		{
			log.info("OrderRecordProccessImpl closeOrderTask() start");
			
			Date date = new Date();
			
			//查询退订回调成功，且失效时间为本月月底的数据
			List<OrderRecord> list = orderRecordMapper.getCloseOrderList();
			
			if(null != list)
			{
				log.info("OrderRecordProccessImpl closeOrderTask() list.size=" + list.size());
				for (int i = 0; i < list.size(); i++) {
					OrderRecord orderRecord = list.get(i);
					log.info("OrderRecordProccessImpl closeOrderTask() orderRecord=" + orderRecord);
					
					//处理订购表：更状态-22；移动表
					orderRecord.setState("22");
					orderRecord.setUpdateTime(date);
					orderRecord.setRemark("服务到期-定时任务处理状态为22，且移动到历史表");
					orderRecordMapper.updateOrderRecord(orderRecord);
					insertHisOrderRecord(orderRecord);
					orderRecordMapper.deleteOrderRecord(orderRecord.getOrderId());
					
					//处理历史订单表：更状态
					HisOrder hisOrder = hisOrderMapper.selectByPrimaryKey(orderRecord.getOrderId());
					hisOrder.setState("22");
					hisOrder.setUpdateTime(date);
					hisOrder.setRemark("服务到期-定时任务处理状态为22");
					hisOrderMapper.updateByPrimaryKeySelective(hisOrder);
				}
			}
		}
		catch(Exception e)
		{
			log.error("{}", "定时任务异常", e);
		}
		
		log.info("OrderRecordProccessImpl closeOrderTask() end");
	}
	
	/**
	* @Title: insertHisOrderRecord 
	* @Description: 根据订单关系表入库订单关系历史表
	* @param orderRecord void
	* @throws
	 */
	private void insertHisOrderRecord(OrderRecord orderRecord) {
		log.info("OrderServiceImpl insertHisOrderRecord() orderRecord:" + orderRecord);
		
		HisOrderRecord hisOrderRecord = new HisOrderRecord();
		hisOrderRecord.setOrderId(orderRecord.getOrderId());
		hisOrderRecord.setParentOrderId(orderRecord.getParentOrderId());
		hisOrderRecord.setPartnerCode(orderRecord.getPartnerCode());
		hisOrderRecord.setAppKey(orderRecord.getAppKey());
		hisOrderRecord.setPartnerOrderId(orderRecord.getPartnerOrderId());
		hisOrderRecord.setCycleType(orderRecord.getCycleType());
		hisOrderRecord.setProductCode(orderRecord.getProductCode());
		hisOrderRecord.setState(orderRecord.getState());
		hisOrderRecord.setMobilephone(orderRecord.getMobilephone());
		hisOrderRecord.setOrderChannel(orderRecord.getOrderChannel());
		hisOrderRecord.setPrice(orderRecord.getPrice());
		hisOrderRecord.setCount(orderRecord.getCount());
		hisOrderRecord.setMoney(orderRecord.getMoney());
		hisOrderRecord.setIsNeedCharge(orderRecord.getIsNeedCharge());
		hisOrderRecord.setOperSource(orderRecord.getOperSource());
		hisOrderRecord.setAllowAutoPay(orderRecord.getAllowAutoPay());
		hisOrderRecord.setWoOrder(orderRecord.getWoOrder());
		hisOrderRecord.setRefundValidTime(orderRecord.getRefundValidTime());;
		hisOrderRecord.setRefundTime(orderRecord.getRefundTime());
		hisOrderRecord.setValidTime(orderRecord.getValidTime());
		hisOrderRecord.setInvalidTime(orderRecord.getInvalidTime());
		hisOrderRecord.setCreateTime(orderRecord.getCreateTime());
		hisOrderRecord.setUpdateTime(orderRecord.getUpdateTime());
		hisOrderRecord.setRedirectUrl(orderRecord.getRedirectUrl());
		hisOrderRecord.setWoOrderId(orderRecord.getWoOrderId());
		hisOrderRecord.setRemark(orderRecord.getRemark());
		try {
			hisOrderRecordMapper.insertSelective(hisOrderRecord);
		} catch (Exception e) {
			log.info("OrderServiceImpl insertHisOrderRecord() Exception e=" + e);
			e.printStackTrace();
		}
	}
	
}
