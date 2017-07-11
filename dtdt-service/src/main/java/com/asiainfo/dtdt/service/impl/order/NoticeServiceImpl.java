package com.asiainfo.dtdt.service.impl.order;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.RestClient;
import com.asiainfo.dtdt.common.util.DateUtil;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.BatchOrder;
import com.asiainfo.dtdt.entity.HisOrder;
import com.asiainfo.dtdt.entity.Order;
import com.asiainfo.dtdt.entity.OrderRecord;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.interfaces.order.INoticeService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.service.IOrderResourceService;
import com.asiainfo.dtdt.service.mapper.AppMapper;
import com.asiainfo.dtdt.service.mapper.BatchOrderMapper;
import com.asiainfo.dtdt.service.mapper.HisOrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.ProductMapper;
import com.asiainfo.dtdt.thread.NoticePartnerOrderThread;

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
public class NoticeServiceImpl implements INoticeService {

	@Resource
	private IOrderService orderService;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	
	@Autowired
	private IPayOrderService payOrderService;
	
	@Autowired
	private AppMapper appMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private HisOrderMapper hisOrderMapper;
	
	@Autowired
	private BatchOrderMapper batchOrderMapper;
	
	@Resource
	private IOrderResourceService orderResourceService;
	
	@Autowired
	private IProductService productService;

	
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
			NoticePartnerOrderThread noticePartnerOrderThread = new NoticePartnerOrderThread(orderId,orderState);
			Thread thread = new Thread(noticePartnerOrderThread);
			thread.start();
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
			if(order == null){
				log.info("NoticeService optNoticeOrder queryOrderByWoOrderId is null by woOrderId="+orderId+" woPlat orderState="+resultCode);
			}else{
				if(order.getOperType() == 1){
					orderNotice(resultCode, orderId, order);
				}
			}
			
			closeOrderNotice(resultCode, orderId, order);
			
		} catch (Exception e) {
			log.error("NoticeService optNoticeOrder fail:"+e.getMessage(),e);
			e.printStackTrace();
		}
	}

	/**
	 * 退订通知
	 * @param resultCode
	 * @param orderId
	 * @param order
	 */
	private void closeOrderNotice(String resultCode, String orderId,
			Order order) {
		//处理退订的业务
		log.info("repeat-order：begin NoticeService optNoticeOrder param:{resultCode="+resultCode+",orderId="+orderId+"}");
		boolean flag = (order == null ? true:false);
		
		OrderRecord orderRecord = null;
		try {
			orderRecord = orderRecordMapper.queryOrderRecordByWoOrderId(orderId);
		} catch (Exception e) {
			log.info("NoticeService queryOrderRecordByWoOrderId Exception e=" + e);
			e.printStackTrace();
		}
		
		if (!(orderRecord == null && order == null)) {
			if(resultCode.equals("3")){//退订中（退订申请已通过，产品次月失效，当月不可再订购。）
				log.info("NoticeService optNoticeOrder wojia return resultCode 3-退订中（退订申请已通过，产品次月失效，当月不可再订购。）");
				orderService.closeOrderUpdateTable(flag? null:order.getOrderId(), JSONObject.toJSONString(orderRecord), "20", flag);//状态20：邮箱侧退订中，此时邮箱侧合作方查询该笔订购状态为：退订中；
			}else if(resultCode.equals("7")){//退订失败
				log.info("NoticeService optNoticeOrder wojia return resultCode 7-退订失败");
				orderService.closeOrderUpdateTable(flag? null:order.getOrderId(), JSONObject.toJSONString(orderRecord), "23", flag);//我方平台自定义退订失败状态为23
			}
			/**退订处理完成回调通知**/
			dtdtNoticeOrder(orderRecord.getOrderId(),true);
		}
		log.info("repeat-order：end");
	}

	/**
	 * 订购通知
	 * @param resultCode
	 * @param orderId
	 * @param order
	 */
	private void orderNotice(String resultCode, String orderId, Order order) {
		boolean noticeSuccess = false;
		if(resultCode.equals("2")){//订购成功
			log.info("NoticeService optNoticeOrder wojia return resultCode 2-订购成功");
			noticeSuccess = true; 
			//沃家总管异步回调返回订购成功，我们需要生成订购关系，并且反冲话费
			/**调用反冲话费 start**/
			/*if(Constant.IS_NEED_CHARGE_0 == order.getIsNeedCharge()){
				chargeService.backChargeBill(orderId,Integer.parseInt(String.valueOf(order.getMoney())) , order.getMobilephone());
			}*/
			/**调用反冲话费 end**/
			orderService.updateOrder(order.getOrderId(), null, "11", Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
			Product product =  productMapper.queryProduct(order.getProductCode());
			orderService.insertFromOrderRecordById(order.getOrderId(),product.getCycleType(), "0");
			orderService.insertOrderBakAndDelOrder(order.getOrderId(), Constant.HISORDER_TYPE_0, "沃家总管订购成功");
			
		}else if(resultCode.equals("6")){//订购失败
			//判断产品类型，后向的加
			String strProduct = productService.queryProduct(order.getProductCode());
			log.debug("NoticeService optNoticeOrder {} get product| {} | ",order.getOrderId(),strProduct);
			if (StringUtils.isBlank(strProduct)) {
				log.info("NoticeService Product is null orderId {}" + order.getOrderId());
			}else{
				Product product = JSONObject.parseObject(strProduct, Product.class);
				if (product.getType() == 1) {
					orderResourceService.refundOrderResource(order.getPartnerCode());
					log.debug("NoticeService | {} | refundOrderResource",order.getOrderId());
				}
			}
			log.info("NoticeService optNoticeOrder wojia return resultCode 7-订购失败");
			//订购失败更新在途表状态5-订购失败待原路退款
			orderService.updateOrder(order.getOrderId(), null, "7", Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
			orderService.insertOrderBakAndDelOrder(order.getOrderId(), Constant.HISORDER_TYPE_0, "沃家总管订购失败");
			/**订购失败回调通知**/
//						dtdtNoticeOrder(order.getOrderId());
			log.info("NoticeService optNoticeOrder return code is notSuccess and noError orderState="+resultCode +" orderId="+orderId);
		}
		
		/**订购成功回调通知**/
		if(isBatchOrder(order.getPartnerOrderId())){//批量订单和单个分开通知
			log.debug("order | {} | is batch order",order.getOrderId());
			BatchOrder batchOrder = null;
			if(StringUtils.isNotBlank(order.getPartnerOrderId())){
				orderService.updateBatchOrderState(order.getPartnerOrderId());
				batchOrder = getBatchOrder(order.getPartnerOrderId());
				log.debug("batchOrder |{}| state is | {} |",batchOrder.getBatchId(),batchOrder.getState());
				if(BatchOrder.STATE_END.equals(batchOrder.getState())){
					/** 批量的通知 **/
					dtdtNoticeBatchOrder(batchOrder);
				}
			}else{
				log.info("notice batchOrder {} partnerOrderId is null",batchOrder.getBatchId());
			}
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
	public void dtdtNoticeOrder(String orderId,boolean noticeSuccess) {
		log.debug("dtdtNoticeOrder |{}| start",orderId);
		if(noticeSuccess){
			dtdtNoticeSuccess(orderId);
		}else{
			dtdtNoticeFail(orderId);
		}
		
	}
	/**
	 *  通知业务结果成功
	 * @param orderId
	 */
	private void dtdtNoticeSuccess(String orderId){
		try {
			OrderRecord orderRecord = orderRecordMapper.selectByPrimaryKey(orderId);
			noticePartner(orderId, orderRecord.getParentOrderId(), orderRecord.getProductCode(),
					orderRecord.getAppKey(), orderRecord.getPrice(), 
					String.valueOf(orderRecord.getAllowAutoPay()), orderRecord.getCreateTime(), 
					orderRecord.getValidTime(), orderRecord.getInvalidTime(), orderRecord.getState());
		} catch (Exception e) {
			log.error("noticeService dtdtNoticeOrder fail by orderId="+orderId);
		}
	}
	
	/**
	 * 通知业务结果失败
	 * @param orderId
	 */
	private void dtdtNoticeFail(String orderId){
		try {
			HisOrder hisOrder = hisOrderMapper.selectByPrimaryKey(orderId);
			String state = hisOrder.getState();
			log.info("dtdtNoticeFail order |{}| state |{}|",orderId,state);
			noticePartner(orderId, hisOrder.getPartnerOrderId(), hisOrder.getProductCode(),
					hisOrder.getAppKey(),hisOrder.getPrice(), hisOrder.getAllowAutoPay(),
					hisOrder.getCreateTime(), hisOrder.getValidTime(), hisOrder.getInvalidTime(),
					state);
		} catch (Exception e) {
			log.error("noticeService dtdtNoticeOrder fail by orderId="+orderId);
		}
	}
	
	private void noticePartner(String orderId,String partnerOrderId,String productCode,
			String appKey,Integer price,String allowAutoPay,Date createTime,
			Date validTime,Date invalidTime,String state) throws Exception{
		App app = appMapper.queryAppInfo(appKey);
		Product product = productMapper.queryProduct(productCode);
		JSONObject json = new JSONObject();
		json.put("orderId", orderId);
		json.put("partnerOrderId", partnerOrderId);
		json.put("productCode", productCode);
		json.put("productName", product.getProductName());
		json.put("createTime", DateUtil.getDateTime(createTime));
		json.put("validTime", DateUtil.getDateTime(validTime));
		json.put("invalidTime", invalidTime==null?"":DateUtil.getDateTime(invalidTime));
		json.put("state",noticeStateFormat(state));
		log.info("doRest |{}| param |{}",app.getNoticeUrl(),json.toString());
		String  result = RestClient.doRest(app.getNoticeUrl(), "POST", json.toString());
		log.info("合作方返回当前订单 |{}| 回调内容：|{}",orderId,result);
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

	public void dtdtNoticeBatchOrder(BatchOrder batchOrder) {
		log.debug("dtdtNoticeBatchOrder start | {} |",batchOrder.toString());
		try {
			App app = appMapper.queryAppInfo(batchOrder.getAppKey());
			Product product = productMapper.queryProduct(batchOrder.getProductCode());
			String state = batchOrder.getState();
			log.info("NoticeServiceImpl dtdtNoticeOrder() state=" + state);
			JSONObject json = new JSONObject();
			json.put("batchId", batchOrder.getBatchId());
			json.put("partnerOrderId", batchOrder.getPartnerOrderId());
			json.put("productCode", batchOrder.getProductCode());
			json.put("productName", product.getProductName());
			json.put("createTime", batchOrder.getCreateTime());
			List<HisOrder> hisOrderList = hisOrderMapper.getListByPartnerOrderId(batchOrder.getPartnerOrderId());
			String phones = "";
			int size = hisOrderList.size();
			if(size > 0){
				JSONArray phonesJson  = new JSONArray(size);
				JSONObject phoneJson = null;
				for(int i = 0; i < size; i++){
					phoneJson = new JSONObject();
					phoneJson.put(hisOrderList.get(i).getMobilephone(),
							noticeStateFormat(hisOrderList.get(i).getState()));
					phonesJson.add(phoneJson);
					phoneJson = null;
				}
				phones = phonesJson.toString();
			}
			json.put("phones", phones);
			log.debug("dtdtNoticeBatchOrder context param {} ",json.toString());
			String  result = RestClient.doRest(app.getNoticeUrl(), "POST", json.toString());
			log.info("合作方返回当前订单（"+batchOrder.getBatchId()+"）回调内容："+result);
		} catch (Exception e) {
			log.error("noticeService dtdtNoticeBatchOrder fail by batchId="+batchOrder.getBatchId());
		}
		
	}

	public BatchOrder getBatchOrder(String partnerOrderId){
		return batchOrderMapper.getBatchOrder(partnerOrderId);
	}

	/**
	 * 判断是否为批量订单
	 */
	public Boolean isBatchOrder(String partnerOrderId){
		return batchOrderMapper.getBatchOrderCountByPOID(partnerOrderId) > 0;
	}
	/**
	 * wojia状态包装
	 * @param state
	 * @return
	 */
	public String noticeStateFormat(String state){
		switch (Integer.valueOf(state)) {
		case 4: return "1";
		case 5:case 6:case 7:case 8:case 15:case 16:case 17:case 18:return "4";
		case 9:return "2";
		case 10:case 11:case 12:case 13:case 14:return "3";
		case 19:return "6";
		case 20:return "5";
		//case 21:json.put("stateMsg", "订购作废");break;
		case 22:return "7";
		case 99:return "8";
		default:return "";
		}
	}
}
