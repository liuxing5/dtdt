package com.asiainfo.dtdt.service.impl.order;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.IsMobileNo;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.entity.HisOrder;
import com.asiainfo.dtdt.entity.HisOrderRecord;
import com.asiainfo.dtdt.entity.Order;
import com.asiainfo.dtdt.entity.OrderRecord;
import com.asiainfo.dtdt.interfaces.order.IQueryOrderService;
import com.asiainfo.dtdt.service.mapper.HisOrderMapper;
import com.asiainfo.dtdt.service.mapper.HisOrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class QueryOrderServiceImpl implements IQueryOrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	
	@Autowired
	private HisOrderRecordMapper hisOrderRecordMapper;
	
	@Autowired
	private HisOrderMapper hisOrderMapper;
	
	
	/**
	* @Title: queryOrderRecord 
	* @Description: 查询订购信息服务
	* @param phone
	* @return String
	* @throws
	 */
	public String queryOrderRecord(String phone, String appkey) {
		
		log.info("OrderServiceImpl queryOrderRecord() phone=" + phone + " appkey=" + appkey);
		
		if (StringUtils.isEmpty(phone)) {
			return ReturnUtil.returnJsonList(Constant.PARAM_NULL_CODE, Constant.PARAM_NULL_MSG, null);
		}
		
		if (!IsMobileNo.isMobile(phone)) {
			return ReturnUtil.returnJsonList(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
		}
		
		try {
			List<OrderRecord> list = orderRecordMapper.queryOrderRecord(phone, appkey);
			log.info("OrderServiceImpl queryOrderState() list=" + list);
			return ReturnUtil.returnJsonList(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, list);
		} catch (Exception e) {
			log.error("OrderServiceImpl queryOrderRecord() Exception e=" + e);
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
	}
	
	/**
	* @Title: queryOrderState 
	* @Description: 查询订单状态服务
	* @param orderId
	* @return String
	* @throws
	 */
	public String queryOrderState(String orderId) {
		log.info("OrderServiceImpl queryOrderState() orderId=" + orderId);
		
		if (StringUtils.isEmpty(orderId)) {
			return ReturnUtil.returnJsonList(Constant.PARAM_NULL_CODE, Constant.PARAM_NULL_MSG, null);
		}
		
		try {
			//先后：order表，order_record表，his_order_record表，his_order表
			
			String state = null;
			Order order = orderMapper.selectByPrimaryKey(orderId);
			if (null == order) {
				OrderRecord orderRecord = orderRecordMapper.selectByPrimaryKey(orderId);
				if (null == orderRecord) {
					HisOrderRecord hisOrderRecord = hisOrderRecordMapper.selectByPrimaryKey(orderId);
					if (null == hisOrderRecord) {
						HisOrder hisOrder = hisOrderMapper.selectByPrimaryKey(orderId);
						if (null == hisOrder) {
							return ReturnUtil.returnJsonInfo(Constant.NO_ORDER_CODE, Constant.NO_ORDER_MSG, null);
						}else {
							state = hisOrder.getState();
						}
					} else {
						state = hisOrderRecord.getState();
					}
				} else {
					state = orderRecord.getState();
				}
			} else {
				state = order.getState();
			} 
			
			log.info("OrderServiceImpl queryOrderState() state=" + state);
			
			JSONObject json = new JSONObject();
			json.put("state", state);
			
			switch (Integer.valueOf(state)) {
			case 1:json.put("stateMsg", "未付款");break;
			case 2:json.put("stateMsg", "付款中");break;
			case 3:json.put("stateMsg", "付款失败");break;
			case 4:json.put("stateMsg", "付款成功，待订购");break;
			
			case 5:case 6:case 7:case 8:json.put("stateMsg", "订购失败");break;
			
			case 9:json.put("stateMsg", "订购受理中");break;
			
			case 10:case 11:case 12:case 13:case 14:case 15:case 16:case 17:case 18:json.put("stateMsg", "订购成功");break;
			
			case 19:json.put("stateMsg", "退订成功");break;
			case 20:json.put("stateMsg", "退订中");break;
			case 21:json.put("stateMsg", "订购作废");break;
			case 22:json.put("stateMsg", "服务到期");break;
			default:json.put("stateMsg", "此订单状态异常");break;
			}
			return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, json);
		} catch (Exception e) {
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
		
	}

}
