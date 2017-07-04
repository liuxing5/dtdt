package com.asiainfo.dtdt.service.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.BaseSeq;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.DateUtil;
import com.asiainfo.dtdt.common.IsMobileNo;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.HisOrderRecord;
import com.asiainfo.dtdt.entity.Order;
import com.asiainfo.dtdt.entity.OrderRecord;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.entity.WoplatOrder;
import com.asiainfo.dtdt.interfaces.IAppService;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.interfaces.order.IOrderRecordService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.order.IWoplatOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.method.OrderMethod;
import com.asiainfo.dtdt.service.mapper.HisOrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.ProductMapper;
import com.asiainfo.dtdt.service.mapper.VcodeMapper;

import lombok.extern.log4j.Log4j2;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 上午11:41:11 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Log4j2
@Service
public class OrderServiceImpl implements IOrderService{

//	@Autowired
//	private ICache cache;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private HisOrderRecordMapper hisOrderRecordMapper;
	
//	@Resource
//	private ICodeService codeService;
	
	@Autowired
	private IOrderRecordService orderRecordService;
	
	@Autowired
	private IWoplatOrderService woplatOrderService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IAppService appService;
	
	@Autowired
	private IPayOrderService payOrderService;
	
	/**
	 * (非 Javadoc) 
	* <p>Title: order</p> 
	* <p>Description: </p> 
	* @param jsonStr
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IOrderService#order(java.lang.String)
	 */
	public String preOrder(String jsonStr) {
		log.info("OrderServiceImpl preOrder() jsonStr:" + jsonStr);
		JSONObject jsonObject = null;
		/**获取接口中传递的参数  start*/
		try {
			jsonObject =JSONObject.parseObject(jsonStr);
		} catch (Exception e) {
			log.error("orderService preOrder check param is json error；"+e.getMessage(),e);
			e.printStackTrace();
			return ReturnUtil.returnJsonError(Constant.PARAM_ILLEGAL_CODE, Constant.PARAM_ILLEGAL_MSG, null);
		}
		String seq = null;
		String partnerCode = null;
		String appKey = null;
		String phone = null;
		String productCode = null;
		String orderMethod = null;
		String allowAutoPay = null;
		String vcode = null;
		String redirectUrl = null;
		try {
			seq = jsonObject.getString("seq");
			partnerCode = jsonObject.getString("partnerCode").toString();
			appKey = jsonObject.getString("appKey").toString();
			phone = jsonObject.getString("phone").toString();
			productCode = jsonObject.getString("productCode").toString();
			orderMethod = jsonObject.get("orderMethod").toString();
			allowAutoPay = jsonObject.get("allowAutoPay").toString();
			vcode = jsonObject.get("vcode").toString();
			redirectUrl = jsonObject.get("redirectUrl").toString();
		} catch (NullPointerException e) {
			log.error("get param error is null");
			return ReturnUtil.returnJsonError(Constant.PARAM_ERROR_CODE, Constant.PARAM_ERROR_MSG, null);
		}
		/**获取接口中传递的参数  end*/
		/**校验接口中传递的参数是否合法  start*/
		if (StringUtils.isBlank(seq)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "seq"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(partnerCode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(appKey)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "appKey"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(phone)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "phone"+Constant.PARAM_NULL_MSG, null);
		}
		if (!IsMobileNo.isMobile(phone)) {
			return ReturnUtil.returnJsonInfo(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
		}
		if (StringUtils.isBlank(productCode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "productCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(orderMethod)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "orderMethod"+Constant.PARAM_NULL_MSG, null);
		}
		if(StringUtils.isBlank(allowAutoPay)){
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "allowAutoPay"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(vcode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "vcode"+Constant.PARAM_NULL_MSG, null);
		}
		/**校验验证码是否正确 start*/
//		if(cache.exists("Y_" + partnerCode + appKey + phone)){//判断验证码是否存在
//			return ReturnUtil.returnJsonError(Constant.SENDSMS_EXPIRED_CODE, Constant.SENDSMS_EXPIRED_MSG, null);//验证码过期不存在
//		}
//		String vaildateCode = (String) cache.getItem("Y_" + partnerCode + appKey + phone);//获取有效的验证码
//		if(!StringUtils.equals(vcode, vaildateCode)){
//			return ReturnUtil.returnJsonError(Constant.SENDSMS_VALIDATE_CODE, Constant.SENDSMS_VALIDATE_MSG, null);//验证码错误
//		}
		/**校验验证码是否正确 end*/
		/**校验接口中传递的参数是否合法  end*/
		
		/**处理业务开始*/
		
		/**查询产品价格信息 start**/
		String strProduct = productService.queryProduct(productCode);
		if(StringUtils.isBlank(strProduct)){
			return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
		}
		Product product = JSONObject.parseObject(strProduct, Product.class);
		/**查询产品价格信息 end**/
		
		/**检查是否存在互斥产品并存储在途数据**/
		Order order = null;
		String woplatOrder = checkWoplatOrderRecord(phone, null,"2");//订购成功
		if(StringUtils.isBlank(woplatOrder)){//检查沃家总管同步是否存在已订购的产品 不存在true 存在false
			if(checkOrderRecord(appKey,phone, productCode).size() <= 0){//检查我方是否存在已订购的产品 不存在true 存在false
				//检查沃家总管和我方都不存在需要记录预订购信息
				order = order(appKey, partnerCode, seq, phone, product, orderMethod, allowAutoPay,redirectUrl);
			}else {
				return ReturnUtil.returnJsonError(Constant.VALIDATE_ORDER_EXISTENCE_CODE, Constant.VALIDATE_ORDER_EXISTENCE_MSG+productCode, null);
			}
		}else {//沃家总管存在已订购产品
			//沉淀订购信息
			order = order(appKey, partnerCode, seq, phone, product, orderMethod, allowAutoPay,redirectUrl);
		}
		//记录验证码信息
//		codeService.insertVcode(cache.getItem("Y_" + partnerCode + appKey + phone).toString(),DateUtil.getDateTime(new Date()),order.getOrderId(),vcode,DateUtil.getDateTime(new Date()),"0");
		/**组装支付订单信息返回给接入商 start**/
		JSONObject json = new JSONObject();
		json.put("orderId", order.getOrderId());
		json.put("status", order.getState());
		json.put("amount", order.getMoney());
		json.put("createTime", DateUtil.getDateTime(order.getCreateTime()));
		List list = new ArrayList();
		JSONObject listItem = new JSONObject();
		listItem.put("productCode", order.getProductCode());
		listItem.put("productName", product.getProductName());
		listItem.put("productType", product.getCycleType());
		listItem.put("status", order.getState());
		listItem.put("price", order.getPrice());
		listItem.put("count", order.getCount());
		listItem.put("allowAutoPay", order.getAllowAutoPay());
		listItem.put("validTime", DateUtil.getDateTime(order.getValidTime()));
		listItem.put("invalidTime", DateUtil.getDateTime(order.getInvalidTime()));
		list.add(listItem);
		json.put("item", listItem);
		/**组装支付订单信息返回给接入商 end**/
		return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, json);
	}

	/**
	* @Title: OrderServiceImpl 
	* @Description: (记录在途订购信息)         
	* @throws
	 */
	public Order order(String appKey,String partnerCode,String seq,String phone,Product product,String orderMethod,String allowAutoPay,String redirectUrl){
		Order order = new Order();
		order.setOrderId(BaseSeq.getLongSeq());//订购订单号
		order.setPartnerCode(partnerCode);//合作方编码
		order.setPartnerOrderId(seq);//合作方订购订单ID
		order.setProductCode(product.getProductCode());//定向流量产品编码
		order.setOperType(Constant.ORDER_OPER_TYPE_0);//订购类型 订购：0，退订：1
		order.setIsRealRequestWoplat(Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);//是否真实请求沃家总管
		order.setState("1");//状态
		order.setMobilephone(phone);//订购手机号码
		order.setOrderChannel(orderMethod);//订购渠道
		order.setCreateTime(new Date());//订购时间
		order.setValidTime(new Date());//有效时间
		order.setRedirectUrl(redirectUrl);//支付成功跳转URL
		if(StringUtils.contains("0", allowAutoPay)){//是否自动续订
			order.setAllowAutoPay((byte)0);
		}else{
			order.setAllowAutoPay((byte)1);
		}
		if(StringUtils.contains(product.getProductCode().substring(2, 4), Constant.CYCLE_TYPE_01)){
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(new Date()));//包月当月失效时间
		}else if(StringUtils.contains(product.getProductCode().substring(2, 4), Constant.CYCLE_TYPE_02)){
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(DateUtil.getCurrentNextYear(new Date(),6)));//包半年失效时间
		}else if(StringUtils.contains(product.getProductCode().substring(2, 4), Constant.CYCLE_TYPE_03)){
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(DateUtil.getCurrentNextYear(new Date(),12)));//包年失效时间
		}
		order.setPrice(product.getPrice());//产品价格
		order.setCount(1);//订购数量
		order.setMoney((long)product.getPrice()*order.getCount());//订单价格
		//根据接入方设置填写
		/**查询接入方设置的信息 start**/
		App app = JSONObject.parseObject(appService.queryAppInfo(appKey), App.class);
		/**查询接入方设置的信息 end**/
		order.setAppKey(appKey);//合作方产品ID
		order.setIsNeedCharge(app.getIsNeedCharge());//是否需要返充话费
		insertOrder(JSONObject.toJSONString(order));
		return order;
	}
	
	/**
	* @Title: OrderServiceImpl 
	* @Description: (检查我方是否存在已订购的产品) 
	* @param phone 订购手机号码
	* @param productCode 订购产品编码
	* @return        
	* @throws
	 */
	public List<OrderRecord> checkOrderRecord(String appKey,String phone,String productCode){
		log.info("orderServiceImpl preOrder checkOrderRecord is param:{productCode:"+productCode+",phone:"+phone+"}");
		List<OrderRecord> orderRecordList = orderRecordService.queryOrderRecordByParam(appKey,productCode, phone);
		if(orderRecordList.size() == 0){
			log.info("orderServiceImpl preOrder checkOrderRecord not Existence order message;");
			return null;
		}
		log.info("orderServiceImpl preOrder checkOrderRecord Existence order message;");
		return orderRecordList;
	}

	/**
	* @Title: OrderServiceImpl 
	* @Description: (检查沃家总管同步是否存在已订购的产品) 
	* @param phone 订购手机号码
	* @param productCode 订购产品编码
	* @param state 状态
	* @return        
	* @throws
	 */
	public String checkWoplatOrderRecord(String phone,String productCode,String state){
		log.info("orderServiceImpl preOrder checkWoplatOrderRecord is param:{productCode:"+productCode+",phone:"+phone+",state:"+state+"}");
		String woplatOrder = woplatOrderService.queryWoplatOrderByParam(productCode, phone, state);//查询是否有同步的订购信息
		if(StringUtils.isBlank(woplatOrder)){
			log.info("orderServiceImpl preOrder checkWoplatOrderRecord not Existence order message;");
			return null;
		}
		log.info("orderServiceImpl preOrder checkWoplatOrderRecord Existence order message");
		return woplatOrder;
	}
	
	/**
	 * (非 Javadoc) 
	* <p>Title: insertOrder</p> 
	* <p>Description: 插入在途订单</p> 
	* @param orderStr
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IOrderService#insertOrder(java.lang.String)
	 */
	public int insertOrder(String orderStr) {
		Order order = JSONObject.parseObject(orderStr, Order.class);
		if(Constant.ORDER_OPER_TYPE_0 == order.getOperType()){//如果是订购需要插入支付信息
			payOrderService.insertPayOrder(BaseSeq.getLongSeq(), order.getOrderId(), order.getMoney(), Constant.PAY_OPER_TYPE, Constant.PAY_STATE_INIT);
		}
		return orderMapper.insertOrder(order);
	}

	/**
	 * (非 Javadoc) 
	* <p>Title: paySuccessOrderDeposition</p> 
	* <p>Description: 支付成功处理订购信息</p> 
	* @param arg0
	* @param arg1
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IOrderService#paySuccessOrderDeposition(java.lang.String, java.lang.String)
	 */
	public void paySuccessOrderDeposition(String resultCode,String orderId) {
		log.info("begin orderService paySuccessOrderDeposition param: resultCode="+resultCode+",orderId="+orderId);
		try {
			if("SUCCESS".equals(resultCode)){//支付返回成功
				/**检查是否存在互斥产品并存储在途数据**/
				Order order = orderMapper.selectByPrimaryKey(orderId);
				String woplatOrder = checkWoplatOrderRecord(order.getMobilephone(), null,"2");//订购成功
				if(StringUtils.isBlank(woplatOrder)){//检查沃家总管同步是否存在已订购的产品 不存在true 存在false
					log.info("begin orderService paySuccessOrderDeposition checkWoplatOrderRecord not Existence woplatOrder");
					if(checkOrderRecord(order.getAppKey(),order.getMobilephone(), order.getProductCode()).size() <= 0){//检查我方是否存在已订购的产品 不存在true 存在false
						log.info("begin orderService paySuccessOrderDeposition checkOrderRecord not Existence order");
						String proStr  = productService.queryProduct(order.getProductCode());
						Product product = JSONObject.parseObject(proStr, Product.class);
						//不存在订购信息，调用沃家总管订购接口
						String wjzgResult = OrderMethod.order(order.getMobilephone(), product.getWoProductCode(), DateUtil.getDateTime(order.getCreateTime()),order.getOrderChannel() );
						if(StringUtils.isBlank(wjzgResult)){
							//订购接口处理异常
							log.info("orderService order product post wojia return fail param:orderId="+orderId);
							return;
						}
						JSONObject wjzgJson = JSONObject.parseObject(wjzgResult);
						String ecode = wjzgJson.getString("ecode");
						String woOrderId = wjzgJson.getString("orderId");
						if("0".equals(ecode)){
							log.info("orderService order product post wojia return success param:orderId="+orderId);
							//订购成功，处理数据
							//woOrder 0-我方初始化订购
							//沉淀订购关系数据成功
							updateOrder(orderId, woOrderId, "10", Constant.IS_NEED_CHARGE_0,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
//							return optOrderRecord(orderId, woOrderId, Constant.WOORDER_TYPE_0, "10", order.getPartnerCode(),Constant.IS_NEED_CHARGE_0,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
							return;
						}else if("4001".equals(ecode)){
							//存在重复订购，处理业务
							log.info("orderService order product post wojia return repeat order param:orderId="+orderId);
							//沃家返回重复订购，邮箱侧创建订购关系，返回订购成功，当前月不用反冲话费
							//*状态13：邮箱侧订购成功&沃家总管侧存在有效订购关系&无需返充话费，此时邮箱侧合作方查询该笔订购状态为：订购成功
							//疑问？：重复订购
							optOrderRecord(orderId, woOrderId, Constant.WOORDER_TYPE_1, "13", order.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
							orderPayBak(orderId, Constant.HISORDER_TYPE_0, "邮箱侧订购成功&沃家总管侧存在有效订购关系&无需返充话费");
							return;
						}else if("4005".equals(ecode)){
							//订购互斥产品
							//存在重复订购，处理业务
							log.info("orderService order product post wojia return mutex order param:orderId="+orderId);
							//邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求，此时邮箱侧合作方查询该笔订购状态为：订购成功；
							optOrderRecord(orderId, woOrderId, Constant.WOORDER_TYPE_3, "14", order.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
							orderPayBak(orderId, Constant.HISORDER_TYPE_0, "沃家总管返回互斥订购：邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求");
							return ;
						}
					}else {//存在介入方产品使用此免流产品,重复订购
						//optOrderRecord(orderId, null, Constant.WOORDER_TYPE_3, "14", order.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
						log.info("orderService order product repeat order param:appkey="+order.getAppKey()+",orderId="+orderId);
						return ;
					}
				}else {//沃家总管存在已订购产品
					WoplatOrder woOrder  = JSONObject.parseObject(woplatOrder, WoplatOrder.class);
//					String woCycleType = woOrder.getProductCode().substring(2, 3);//沃家总管订购流量包
//					String cycleType = woOrder.getProductCode().substring(2, 3);//当前订购流量包
					optOrderRecord(orderId, null, Constant.WOORDER_TYPE_3, "14", woOrder.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
					orderPayBak(orderId, Constant.HISORDER_TYPE_0, "沃家总管同步记录中存在订购关系：邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求");
					return ;
				}
			}else {//支付失败
				//更新在途订购订单状态4-付款失败
				updateOrder(orderId,null, "4",Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
				//将在途表信息存放在备份表中
				orderPayBak(orderId, "2", "第三方支付失败");
			}
		} catch (Exception e) {
			log.error("orderService paySuccessOrderDeposition fail:"+e.getMessage(),e);
			e.printStackTrace();
		}
		return;
	}
	
	/**
	* @Title: OrderServiceImpl 
	* @Description: (处理成功沉淀订购关系) 
	* @param orderId 订单ID
	* @param woOrderId 沃家总管订购ID
	* @param woOrder 沃家总管方订购记录 0：我方初始化订购 1：其他代理商订购 2：其他代理商订购失效、到期或退订由我方续订
	* @param state
	* @param productCode 产品编码
	* @return        
	* @throws
	 */
	public boolean optOrderRecord(String orderId,String woOrderId,String woOrder,String state,String productCode,byte isNeedCharge,byte isRealRequestWoplat){
		int num = updateOrder(orderId, woOrderId, state,isNeedCharge,isRealRequestWoplat);
		if(num > 0){
			String cycleType = productCode.substring(2, 3);//当前订购流量包
			byte cycleType2 = 0;
			if(Constant.CYCLE_TYPE_01.equals(cycleType)){//包月
				//判断wojia总管订购流量包为包月并且当前也为包月流量包
				cycleType2 = 0;
			}else if(Constant.CYCLE_TYPE_02.equals(cycleType)){//包半年
				//判断wojia总管订购流量包为包半年并且当前也为包半年流量包
				cycleType2 = 1;
			}else if(Constant.CYCLE_TYPE_03.equals(cycleType)){//包年
				//判断wojia总管订购流量包为包年并且当前也为包年流量包
				cycleType2 = 2;
			}
			//沉淀订购关系
			int count = insertFromOrderRecordById(orderId, cycleType2, woOrder);//0-我方初始化订购
			if(count > 0){
				log.info("orderService order product deposit orderRecord data success:orderId="+orderId);
				return true;
			}
		}
		log.info("orderService order product deposit orderRecord data fail:orderId="+orderId);
		return false;
	}
	
	/**
    * @Title: OrderMapper 
    * @Description: (将在途订购信息存放到备份表中) 
    * @param orderId
    * @param copyType
    * @param copyRemark
    * @return        
    * @throws
     */
	public int insertFromHisOrderById(String orderId,String copyType,String copyRemark) {
		
		return orderMapper.insertFromHisOrderById(orderId, copyType, copyRemark);
	}

	/**
    * @Title: OrderMapper 
    * @Description: (将在途订购信息沉淀到订购关系表) 
    * @param orderId 订单ID
    * @param cycleType 产品周期 包月：0，包半年：1，包年：2
    * @param woOrder  沃家总管方订购记录 0：我方初始化订购 1：其他代理商订购 2：其他代理商订购失效、到期或退订由我方续订
    * @return        
    * @throws
     */
	public int insertFromOrderRecordById(String orderId,byte cycleType2,String woOrder) {
		
		return orderMapper.insertFromOrderRecordById(orderId, cycleType2, woOrder);
	}

	/**
	* @Title: OrderServiceImpl 
	* @Description: (更新在途订购订单状态) 
	* @param orderId
	* @param state
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
	* @return        
	* @throws
	 */
	public int updateOrder(String orderId,String woOrderId,String state,byte isNeedCharge,byte isRealRequestWoplat){
		Order order = new Order();
		order.setOrderId(orderId);
		order.setWoOrderId(woOrderId);
		order.setState(state);
		order.setIsRealRequestWoplat(isRealRequestWoplat);
		order.setIsNeedCharge(isNeedCharge);
		int num = orderMapper.updateByPrimaryKeySelective(order);
		return num;
	}
	
	/**
	* @Title: OrderServiceImpl 
	* @Description: (将在途订单订购信息存放在备份表中并删除在途表信息) 
	* @param orderId 订单ID
	* @param copyType	入表方式（0：订单完工 1：未支付失效 2：支付失败 3：人工操作）
	* @param copyRemark  入表备注      
	* @throws
	 */
	public void orderPayBak(String orderId,String copyType,String copyRemark){
		int num = orderMapper.insertFromHisOrderById(orderId, copyType, copyRemark);
		if(num > 0){
			orderMapper.deleteByPrimaryKey(orderId);//清除在途表信息
		}
	}

	/**
	* @Title: closeOrder 
	* @Description:定向流量退订接口
	* @param orderStr
	* @return
	* @throws
	 */
	public String closeOrder(String orderStr) {
		log.info("OrderServiceImpl closeOrder() orderStr:" + orderStr);
		
		//获取参数
		JSONObject jsonObject = JSONObject.parseObject(orderStr);
		String woOrderId = jsonObject.getString("orderId");
		
		//参数校验
		if (StringUtils.isBlank(woOrderId)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "orderId" + Constant.PARAM_NULL_MSG, null);
		}
		
		//查询订购关系表：校验包月类订购为成功，只限包月类产产品
		OrderRecord orderRecord = null;
		try {
			orderRecord = orderRecordMapper.selectMonthProduct(woOrderId);
			if (null == orderRecord)
				return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, "包月类" + Constant.PRODUCT_EXISTENCE_MSG, null);
		} catch (Exception e) {
			log.info("OrderServiceImpl closeOrder() selectMonthProduct() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
			
		//查询产品表
		Product product = null;
		try {
			product = productMapper.queryProduct(orderRecord.getProductCode());
			if (null == product) 
					return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
		} catch (Exception e) {
			log.info("OrderServiceImpl closeOrder() selectMonthProduct() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
			
		//设置返回参数
		JSONObject data = new JSONObject();
		data.put("partnerOrderId", orderRecord.getParentOrderId());
		data.put("orderId", orderRecord.getOrderId());
		data.put("timeStamp", DateUtil.getSysdateYYYYMMDDHHMMSS());
			
		//拼装在途表数据，退订完成移历史表
		Order order = closeOrderParams(orderRecord);
			
		boolean flag = true;
		while (flag) {
			log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder request start" + orderRecord);
			String wojiaStr = OrderMethod.closeOrder(orderRecord.getMobilephone(), orderRecord.getProductCode(), woOrderId, DateUtil.getSysdateYYYYMMDDHHMMSS(), orderRecord.getOrderChannel());
					
			JSONObject wojiaJson = JSONObject.parseObject(wojiaStr);
			log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder response wojiaJson" + wojiaJson);
					
			String ecode = wojiaJson.getString("ecode");
			if("0".equals(ecode)){
				log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder order success ecode=" + ecode);
				//如果退订成功还需要返回退订详细信息：流量包名称，退订时间，退订生效时间等
				data.put("productName", product.getProductName());
				data.put("refundTime", orderRecord.getRefundTime());
				data.put("refundValidTime", orderRecord.getRefundValidTime());	
				updateTable(orderRecord, "19", order, woOrderId);
				flag = false;
				return ReturnUtil.returnJsonInfo(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, data.toJSONString());
			} else {
				log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder order fail ecode=" + ecode);
				try {
					Thread.sleep(3000);//过3秒再次请求
				} catch (InterruptedException e) {
					log.info("OrderServiceImpl closeOrder() InterruptedException e" + e);
				}
				
				//查询wojia订购状态
				log.info("OrderServiceImpl closeOrder() OrderMethod.queryOrder request start");
				String queryWojiaStr = OrderMethod.queryOrder(orderRecord.getMobilephone(), woOrderId);
						
				JSONObject queryWojiaJson = JSONObject.parseObject(queryWojiaStr);
				log.info("OrderServiceImpl closeOrder() OrderMethod.queryOrder response queryWojiaJson" + queryWojiaJson);
						
				String qryEcode = queryWojiaJson.getString("ecode");
				String qryEmsg = queryWojiaJson.getString("emsg");
				if("0".equals(qryEcode)){
					log.info("OrderServiceImpl closeOrder() OrderMethod.queryOrder order success ecode=" + ecode);
					//如果退订成功还需要返回退订详细信息：流量包名称，退订时间，退订生效时间等
					data.put("productName", product.getProductName());
					data.put("refundTime", orderRecord.getRefundTime());
					data.put("refundValidTime", orderRecord.getRefundValidTime());
					updateTable(orderRecord, "19", order, woOrderId);
					flag = false;
					return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, data.toString());
				} else {
					//如果退订失败需返回失败原因
					data.put("failMsg", qryEmsg);
					updateTable(orderRecord, "23", order, woOrderId);
					return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, data.toString());
				} 
			}
		}
		return ReturnUtil.returnJsonInfo(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, data.toJSONString());
	}
	
	/**
	* @Title: updateTable 
	* @Description: 调用wojia退订接口：成功更新 order和orderRecord表为已退订	状态并迁移到历史表，返回接口成功信息
	* @param orderId
	* @param orderRecord
	* @param product
	* @param data
	* @param order
	* @param woOrderId
	* @return String
	* @throws
	 */
	private void updateTable(OrderRecord orderRecord, String state, Order order, String woOrderId) {
		log.info("OrderServiceImpl updateTable() orderRecord =" + orderRecord + "woOrderId=" + woOrderId);

		try {
			order.setWoOrderId(woOrderId);
			insertFromHisOrderById(orderRecord.getOrderId(), "0", "包月退订");//copy_type：入表方式（0：包月退订 1：包半年、包年到期失效 2：人工操作）
			orderMapper.deleteByPrimaryKey(orderRecord.getOrderId());
				
			orderRecord.setState(state);//设置状态：19-退订成功
			orderRecordMapper.updateOrderRecord(orderRecord);
			insertHisOrderRecord(orderRecord);
			orderRecordMapper.deleteOrderRecord(orderRecord.getOrderId());
		} catch (Exception e) {
			log.info("OrderServiceImpl updateTable() Exception e=" + e);
		}
	}
	
	/**
	 * 
	* @Title: closeOrderParams 
	* @Description: 根据订购关系表数据，在请求退订后新生成一条在途数据
	* @param orderRecord void
	* @throws
	 */
	private Order closeOrderParams(OrderRecord orderRecord) {
		Order order = new Order();
		String order_id = String.valueOf(UUID.randomUUID());
		Date now = new Date();
		
		order.setOrderId(order_id);
		order.setPartnerCode(orderRecord.getPartnerCode());
		order.setAppKey(orderRecord.getAppKey());
		order.setPartnerOrderId(orderRecord.getPartnerOrderId());
		order.setProductCode(orderRecord.getProductCode());
		order.setOperType((byte)1);
		order.setRefundOrderId(order_id);
		/**
		 * 是否真实请求沃家总管（0：真实请求 1：未请求）
            	如果我方同一手机号码，在多个app下订购了同一流量产品，
            	则1、只有第一次订购会像沃家总管发起订购请求；
            	2、只有最后一个退订时，才能真实像沃家总管发起退订请求；
		 */
		order.setIsRealRequestWoplat((byte)1);
		order.setState(orderRecord.getState());
		order.setMobilephone(orderRecord.getMobilephone());
		order.setOrderChannel(orderRecord.getOrderChannel());
		order.setCreateTime(now);
		order.setUpdateTime(now);
		order.setValidTime(now);
		order.setInvalidTime(now);
		order.setPrice(orderRecord.getPrice());
		order.setCount(orderRecord.getCount());
		order.setMoney(orderRecord.getMoney());
		order.setIsNeedCharge(orderRecord.getIsNeedCharge());
		order.setAllowAutoPay(orderRecord.getAllowAutoPay());
		order.setRedirectUrl(orderRecord.getRedirectUrl());
		order.setIsRealRequestWoplat((byte)1);
		order.setRemark("");
		return order;
	}
	
	/**
	* @Title: insertHisOrderRecord 
	* @Description: 根据订单关系表入库订单关系历史表
	* @param orderRecord void
	* @throws
	 */
	private void insertHisOrderRecord(OrderRecord orderRecord) {
		log.info("OrderServiceImpl insertHisOrderRecord() orderRecord:" + orderRecord);
		Date date = new Date();
		
		HisOrderRecord hisOrderRecord = new HisOrderRecord();
		hisOrderRecord.setOrderId(orderRecord.getOrderId());
		hisOrderRecord.setParentOrderId(orderRecord.getParentOrderId());
		hisOrderRecord.setPartnerCode(orderRecord.getPartnerCode());
		hisOrderRecord.setAppKey(orderRecord.getAppKey());
		hisOrderRecord.setPartnerOrderId(orderRecord.getPartnerOrderId());
		hisOrderRecord.setCycleType2(orderRecord.getCycleType2());
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
		hisOrderRecord.setRemark(orderRecord.getRemark());
		hisOrderRecord.setRefundValidTime(orderRecord.getRefundValidTime());;
		hisOrderRecord.setRefundTime(orderRecord.getRefundTime());
		hisOrderRecord.setValidTime(orderRecord.getValidTime());
		hisOrderRecord.setInvalidTime(orderRecord.getInvalidTime());
		hisOrderRecord.setCreateTime(date);
		hisOrderRecord.setUpdateTime(date);
		hisOrderRecord.setRedirectUrl(orderRecord.getRedirectUrl());
		hisOrderRecord.setWoOrderId(orderRecord.getWoOrderId());
		try {
			hisOrderRecordMapper.insertSelective(hisOrderRecord);
		} catch (Exception e) {
			log.info("OrderServiceImpl insertHisOrderRecord() Exception e=" + e);
			e.printStackTrace();
		}
	}
	
}
