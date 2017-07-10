package com.asiainfo.dtdt.service.impl.order;

import java.util.ArrayList;
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
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.interfaces.order.IQueryOrderService;
import com.asiainfo.dtdt.service.mapper.HisOrderMapper;
import com.asiainfo.dtdt.service.mapper.HisOrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.ProductMapper;

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
	
	@Autowired
	private ProductMapper productMapper;
	
	/**
	* @Title: queryOrderRecord 
	* @Description: 查询订购信息服务
	* @param phone
	* @return String
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String queryOrderRecord(String phone, String appkey, String partnerCode) {
		
		log.info("OrderServiceImpl queryOrderRecord() phone=" + phone + " appkey=" + appkey);
		
		if (StringUtils.isEmpty(phone)) {
			return ReturnUtil.returnJsonList(Constant.PARAM_NULL_CODE, Constant.PARAM_NULL_MSG, null);
		}
		
		if (phone.length() != 11) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_LENGTH_CODE, "phone" + Constant.PARAM_LENGTH_MSG, null);
		}
		
		if (!IsMobileNo.isMobile(phone)) {
			return ReturnUtil.returnJsonList(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
		}
		
		try {
			//需求：只返回订购成功的，只查询orderRecord表
			List<OrderRecord> orderRecordList = orderRecordMapper.queryOrderRecord(phone, partnerCode, appkey);
			log.info("OrderServiceImpl queryOrderState() orderRecordList=" + orderRecordList);
			if (orderRecordList.size() == 0) {
				return ReturnUtil.returnJsonInfo(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
			}
//			data.put("successOrders", orderRecordList);
			
			//订购失败信息在历史订购关系表
//			List<HisOrderRecord> hisOrderRecordlist = hisOrderRecordMapper.queryOrderRecord(phone, appkey);
//			log.info("OrderServiceImpl queryOrderState() hisOrderRecordlist=" + hisOrderRecordlist);
//			data.put("failOrders", hisOrderRecordlist);
			
			//拼返回参数
			JSONObject data = new JSONObject();
			List returnList = new ArrayList<>();
			
			for (int i = 0; i < orderRecordList.size(); i++) {
				OrderRecord orderRecord = orderRecordList.get(i);
				Product product = productMapper.queryProduct(orderRecord.getProductCode());
				
				data.put("productCode", orderRecord.getProductCode());
				data.put("cycleType", orderRecord.getCycleType());
				data.put("type", product.getType());
				data.put("validTime", orderRecord.getValidTime());
				data.put("invalidTime", orderRecord.getInvalidTime());
				returnList.add(data);
			}
			
			return ReturnUtil.returnJsonList(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, returnList);
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
	public String queryOrderState(String orderId, String appkey, String partnerCode) {
		log.info("OrderServiceImpl queryOrderState() orderId=" + orderId);
		
		if (StringUtils.isEmpty(orderId)) {
			return ReturnUtil.returnJsonList(Constant.PARAM_NULL_CODE, Constant.PARAM_NULL_MSG, null);
		}
		
		if (orderId.length() != 32) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_LENGTH_CODE, "orderId" + Constant.PARAM_LENGTH_MSG, null);
		}
		
		try {
			//先后：order表，order_record表，his_order_record表，his_order表
			
			String state = null;
			Order order = orderMapper.queryOrderState(orderId, partnerCode, appkey);
			if (null == order) {
				OrderRecord orderRecord = orderRecordMapper.queryOrderState(orderId, partnerCode, appkey);
				if (null == orderRecord) {
					HisOrderRecord hisOrderRecord = hisOrderRecordMapper.queryOrderState(orderId, partnerCode, appkey);
					if (null == hisOrderRecord) {
						HisOrder hisOrder = hisOrderMapper.queryOrderState(orderId, partnerCode, appkey);
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
			
			/**
			 *  wojia 返回 state
			* 	状态1：未付款，此时邮箱侧合作方查询该笔订购状态为：未付款；
			*	状态2：付款中，此时邮箱侧合作方查询该笔订购状态为：付款中；
			*	状态3：付款失败，此时邮箱侧合作方查询该笔订购状态为：付款失败；
			*	状态4：付款成功&邮箱侧发起订购中，此时邮箱侧合作方查询该笔订购状态为：付款成功，待订购；
			*	状态5：邮箱侧订购失败&未原路原付款金额退款，此时邮箱侧合作方查询该笔订购状态为：订购失败，待原路退款；
			*	状态6：邮箱侧订购失败&原路原付款金额退款中，此时邮箱侧合作方查询该笔订购状态为：订购失败，原路退款中；
			*	状态7：邮箱侧订购失败&原路原付款金额退款成功，此时邮箱侧合作方查询该笔订购状态为：订购失败，原路退款成功；
			*	状态8：邮箱侧订购失败&原路原付款金额退款失败，此时邮箱侧合作方查询该笔订购状态为：订购失败，原路退款失败，人工处理中；
			*	状态9：邮箱侧订购中，此时邮箱侧合作方查询该笔订购状态为：订购受理中；
			*	状态10：邮箱侧订购成功&沃家总管侧存在有效订购关系&待返充话费，此时邮箱侧合作方查询该笔订购状态为：订购成功；
			*	状态11：邮箱侧订购成功&沃家总管侧存在有效订购关系&返充话费成功，此时邮箱侧合作方查询该笔订购状态为：订购成功；
			*	状态12：邮箱侧订购成功&沃家总管侧存在有效订购关系&返充话费失败，此时邮箱侧合作方查询该笔订购状态为：订购成功；
			*	状态13：邮箱侧订购成功&沃家总管侧存在有效订购关系&无需返充话费，此时邮箱侧合作方查询该笔订购状态为：订购成功；
			*	状态14：邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求，此时邮箱侧合作方查询该笔订购状态为：订购成功；
			*	状态15：邮箱侧订购失败&沃家总管侧不存在有效订购关系&未原路非付款金额退款，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，待原路部分退款；
			*	状态16：邮箱侧订购失败&沃家总管侧不存在有效订购关系&原路非付款金额退款中，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，原路部分退款中；
			*	状态17：邮箱侧订购失败&沃家总管侧不存在有效订购关系&原路非付款金额退款失败，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，原路部分退款失败，人工处理中；
			*	状态18：邮箱侧订购失败&沃家总管侧不存在有效订购关系&原路非付款金额退款成功，此时邮箱侧合作方查询该笔订购状态为：订购成功，定向流量服务中断，原路部分退款成功；
			*	状态19：邮箱侧退订成功，此时邮箱侧合作方查询该笔订购状态为：退订成功；
			*	状态20：邮箱侧退订中，此时邮箱侧合作方查询该笔订购状态为：退订中；
			*	状态21：邮箱侧已作废（订购状态在X小时内一直为“未支付”，X小时后将该订单状态设置为“已作废”），此时邮箱侧合作方查询该笔订购状态为：订购作废；
			*	状态22：服务到期（半年包、年包产品自然达到有效期截止日），此时邮箱侧合作方查询该笔订购状态为：服务到期；
			*
			*	我方返回给合作方state（无 1,2,3,21状态）
			*	      状态1：待订购；--4
			*	      状态2：订购中；--9
			*	      状态3：订购成功--10,11,12,13,14
			*	      状态4：订购失败；--5,6,7,8,15,16,17,18
			*	      状态5：退订中；--20
			*	      状态6：退订成功；--19
			*	      状态7：服务到期；--22
			 */
//			case 1:json.put("stateMsg", "未付款");break;
//			case 2:json.put("stateMsg", "付款中");break;
//			case 3:json.put("stateMsg", "付款失败");break;
//			case 4:json.put("stateMsg", "付款成功，待订购");break;
//			
//			case 5:case 6:case 7:case 8:json.put("stateMsg", "订购失败");break;
//			
//			case 9:json.put("stateMsg", "订购受理中");break;
//			
//			case 10:case 11:case 12:case 13:case 14:case 15:case 16:case 17:case 18:json.put("stateMsg", "订购成功");break;
//			
//			case 19:json.put("stateMsg", "退订成功");break;
//			case 20:json.put("stateMsg", "退订中");break;
//			case 21:json.put("stateMsg", "订购作废");break;
//			case 22:json.put("stateMsg", "服务到期");break;
			
			//需求：付款取消则状态从4开始，无21状态
//			case 1:json.put("stateMsg", "未付款");break;
//			case 2:json.put("stateMsg", "付款中");break;
//			case 3:json.put("stateMsg", "付款失败");break;
			case 4:json.put("state", "1");json.put("stateMsg", "待订购");break;
			case 5:case 6:case 7:case 8:case 15:case 16:case 17:case 18:json.put("state", "4");json.put("stateMsg", "订购失败");break;
			case 9:json.put("state", "2");json.put("stateMsg", "订购中");break;
			case 10:case 11:case 12:case 13:case 14:json.put("state", "3");json.put("stateMsg", "订购成功");break;
			
			case 19:json.put("state", "6");json.put("stateMsg", "退订成功");break;
			case 20:json.put("state", "5");json.put("stateMsg", "退订中");break;
			//case 21:json.put("stateMsg", "订购作废");break;
			case 22:json.put("state", "7");json.put("stateMsg", "服务到期");break;
			default:json.put("stateMsg", "此订单状态异常");break;
			}
			return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, json);
		} catch (Exception e) {
			return ReturnUtil.returnJsonList(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
	}

}
