package com.asiainfo.dtdt.service.impl.order;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
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

@Service
public class QueryOrderServiceImpl implements IQueryOrderService {
	
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
	
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
	public String queryOrderRecord(String phone) {
		
		logger.info("OrderServiceImpl queryOrderRecord() phone=" + phone);
		
		try {
			List<OrderRecord> list = orderRecordMapper.queryOrderRecord(phone);
			return ReturnUtil.returnJsonList(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, list);
		} catch (Exception e) {
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
		logger.info("OrderServiceImpl queryOrderState() orderId=" + orderId);
		
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
							return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, "no order data", null);
						}
						state = hisOrder.getState();
					}
					state = hisOrderRecord.getState();
				}
				state = orderRecord.getState();
			}
			state = order.getState();
			
			logger.info("OrderServiceImpl queryOrder() state=" + state);
			
			JSONObject json = new JSONObject();
			json.put("state", state);
			
			switch (Integer.valueOf(state)) {
			case 1:json.put("stateMsg", "未付款");break;
			case 2:json.put("stateMsg", "付款中");break;
			case 3:json.put("stateMsg", "付款失败");break;
			case 4:json.put("stateMsg", "付款成功，待订购");break;
			
			case 5:case 6:case 7:case 8:json.put("stateMsg", "订购失败");break;
			
			case 9:json.put("stateMsg", "订购受理中");break;
			
			case 10:case 11:case 12:case 13:case 14:case 15:case 16:case 17:case 18:json.put("state", "订购成功");break;
			
			case 19:json.put("stateMsg", "退订成功");break;
			case 20:json.put("stateMsg", "退订中");break;
			case 21:json.put("stateMsg", "订购作废");break;
			case 22:json.put("stateMsg", "服务到期");break;
			default:break;
			}
			return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, json);
		} catch (Exception e) {
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
		
	}

}
