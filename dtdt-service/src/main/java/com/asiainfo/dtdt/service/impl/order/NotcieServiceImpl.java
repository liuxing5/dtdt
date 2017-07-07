package com.asiainfo.dtdt.service.impl.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.RestClient;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.Order;
import com.asiainfo.dtdt.entity.OrderRecord;
import com.asiainfo.dtdt.interfaces.order.IChargeService;
import com.asiainfo.dtdt.interfaces.order.INoticeService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.service.mapper.AppMapper;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;

import lombok.extern.log4j.Log4j2;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年7月3日 上午11:31:25 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Log4j2
@Service
public class NotcieServiceImpl implements INoticeService {

	@Resource
	private IOrderService orderService;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	
	@Autowired 
	private IChargeService chargeService;
	
	@Autowired
	private IPayOrderService payOrderService;
	
	@Autowired
	private INoticeService noticeService;
	
	@Autowired
	private AppMapper appMapper;
	
	/**
	 * (非 Javadoc) 
	* <p>Title: optNoticeOrder</p> 
	* <p>Description: 沃家总管回调通知</p> 
	* @param notifyJson
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.INoticeService#optNoticeOrder(java.lang.String)
	 */
	public String optNoticeOrder(String notifyJson){
		//返回的json申明
		JSONObject returnJson = new JSONObject();
		try {
			JSONObject jsonObject =JSONObject.parseObject(notifyJson);
//			String seq = jsonObject.get("seq").toString();
//			String appId = jsonObject.get("msisdn").toString();
			String orderId = jsonObject.get("orderId").toString();//沃家总管返回的orderId
//			String productId = jsonObject.get("productId").toString();
//			String subscriptionTime = jsonObject.get("subscriptionTime").toString();
//			String feeStartDate = jsonObject.get("feeStartDate").toString();
//			String validExpireDate = jsonObject.get("validExpireDate").toString();
//			String orderDesc = jsonObject.get("orderDesc").toString();
			String orderState = jsonObject.get("orderState").toString();//订单状态： 2，订购成功 5，退订成功（可再订购）6，订购失败7，退订失败。
//			String productAttrValues = jsonObject.get("productAttrValues").toString();
//			JSONArray jsonArray = JSONArray.parseArray(productAttrValues);
//			Map<String,Object> mapList  = new HashMap();
//			List list  = new ArrayList();
//			for (int i = 0; i < jsonArray.size(); i++) {
//				JSONObject json = jsonArray.getJSONObject(i);
//				mapList.put("attrTypeId",json.getString("attrTypeId"));//产品编码
//				mapList.put("attrValue",json.getString("attrValue"));//产品属性值
//				mapList.put("attrDesc",json.getString("attrDesc"));//产品属性描述
//				list.add(mapList);
//			}
			/**处理业务开始*/
			optNoticeOrder(orderState, orderId);
			/**处理业务结束*/
			returnJson.put("ecode", "0");
			returnJson.put("emsg", "成功");
//			returnJson.put("seq", seq);
		} catch (JSONException jsone) {
			log.error("wojia notice check param json is error:"+jsone.getMessage(),jsone);
		}
		return returnJson.toString();
	}
	
	/**
	* @Title: NotcieServiceImpl 
	* @Description: (沃家总管回调处理业务) 
	* @param resultCode
	* @param orderId        
	* @throws
	 */
	public void optNoticeOrder(String resultCode, String orderId) {
		
		log.info("begin NoticeService optNoticeOrder param:{resultCode="+resultCode+",orderId="+orderId+"}");
		try {
			Order order = orderMapper.queryOrderByWoOrderId(orderId);
			if(resultCode.equals("2")){//订购成功
				log.info("NoticeService optNoticeOrder wojia return resultCode 2-订购成功");
				//沃家总管异步回调返回订购成功，我们需要生成订购关系，并且反冲话费
				/**调用反冲话费 start**/
				/*if(Constant.IS_NEED_CHARGE_0 == order.getIsNeedCharge()){
					chargeService.backChargeBill(orderId,Integer.parseInt(String.valueOf(order.getMoney())) , order.getMobilephone());
				}*/
				/**调用反冲话费 end**/
				orderService.updateOrder(order.getOrderId(), null, "11", Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
				String pcStr = order.getProductCode().substring(2, 4);//当前订购流量包
				byte cycleType = 0;
				if(Constant.CYCLE_TYPE_01.equals(pcStr)){//包月
					//判断wojia总管订购流量包为包月并且当前也为包月流量包
					cycleType = 0;
				}else if(Constant.CYCLE_TYPE_02.equals(pcStr)){//包半年
					//判断wojia总管订购流量包为包半年并且当前也为包半年流量包
					cycleType = 1;
				}else if(Constant.CYCLE_TYPE_03.equals(pcStr)){//包年
					//判断wojia总管订购流量包为包年并且当前也为包年流量包
					cycleType = 2;
				}
				orderService.insertFromOrderRecordById(order.getOrderId(),cycleType, "0");
				orderService.insertOrderBakAndDelOrder(order.getOrderId(), Constant.HISORDER_TYPE_0, "邮箱侧订购成功&沃家总管侧存在有效订购关系&返充话费成功");
				/**订购成功回调通知**/
				noticeService.dtdtNoticeOrder(order.getOrderId());
			}else if(resultCode.equals("5")){//退订成功（可再订购）
				log.info("NoticeService optNoticeOrder wojia return resultCode 5-退订成功（可再订购）");
				//退订成功将订单关系数据转移到备份表中
				orderService.updateOrder(order.getOrderId(), null, "19", Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
				orderService.insertOrderBakAndDelOrder(order.getOrderId(), Constant.HISORDER_TYPE_0, "邮箱侧退订成功");
				
			}else if(resultCode.equals("6")){//订购失败
				log.info("NoticeService optNoticeOrder wojia return resultCode 6-订购失败");
				//订购失败更新在途表状态5-订购失败待原路退款
				orderService.updateOrder(order.getOrderId(), null, "5", Constant.IS_NEED_CHARGE_0,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
			}else if(resultCode.equals("7")){//退订失败
				log.info("NoticeService optNoticeOrder wojia return resultCode 7-退订失败");
				//一般情况下不会出现退订失败，退订失败不更新任何信息
			}
		} catch (Exception e) {
			log.error("NoticeService optNoticeOrder fail:"+e.getMessage(),e);
			e.printStackTrace();
		}
	}

	/**
	 * (非 Javadoc) 
	* <p>Title: dtdtNoticeOrder</p> 
	* <p>Description: 免流平台回调通知接入方</p> 
	* @param orderId
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.INoticeService#dtdtNoticeOrder(java.lang.String)
	 */
	@Override
	public void dtdtNoticeOrder(String orderId) {
		try {
			OrderRecord orderRecord = orderRecordMapper.selectByPrimaryKey(orderId);
			App app = appMapper.queryAppInfo(orderRecord.getAppKey());
			String state = orderRecord.getState();
			log.info("NoticeServiceImpl dtdtNoticeOrder() state=" + state);
			JSONObject json = new JSONObject();
			switch (Integer.valueOf(state)) {
			case 10:case 11:case 12:case 13:case 14:case 15:case 16:case 17:case 18:
				json.put("orderId", orderRecord.getOrderId());
				json.put("productCode", orderRecord.getProductCode());
				json.put("stateName", "订购成功");
				RestClient.doRest(app.getNoticeUrl(), "POST", json.toString());
				break;
			default:break;
			}
		} catch (Exception e) {
			log.error("noticeService dtdtNoticeOrder fail by orderId="+orderId);
		}
		
	}

	/**
	 * (非 Javadoc) 
	* <p>Title: thirdPayNotice</p> 
	* <p>Description: </p> 
	* @param params
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.INoticeService#thirdPayNotice(java.lang.String)
	 */
	@Override
	public boolean thirdPayNotice(String params) {
		try {
			JSONObject jsonParam = JSONObject.parseObject(params);
			String orderId = jsonParam.getString("orderId");
			String resultCode = jsonParam.getString("resultCode");
			String thirdOrderId = jsonParam.getString("thirdOrderId");
			/**更新充值状态 start**/
			payOrderService.updatePayOrderStatusAfterPayNotify(resultCode, orderId,thirdOrderId);
			/**更新充值状态 end**/
			//订购数据沉淀
			orderService.paySuccessOrderDeposition("SUCCESS", orderId);
			//订购数据沉淀
			return true;
		} catch (Exception e) {
			log.error("noticeService thirdPayNotice Exception:"+e.getMessage(),e);
		}
		return false;
	}
}
