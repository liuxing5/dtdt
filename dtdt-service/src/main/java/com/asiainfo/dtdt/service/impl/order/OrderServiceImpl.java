package com.asiainfo.dtdt.service.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.BaseSeq;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.DateUtil;
import com.asiainfo.dtdt.common.IsMobileNo;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.common.request.HttpClientUtil;
import com.asiainfo.dtdt.common.util.MD5Util;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.HisOrderRecord;
import com.asiainfo.dtdt.entity.Order;
import com.asiainfo.dtdt.entity.OrderRecord;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.entity.Vcode;
import com.asiainfo.dtdt.entity.WoplatOrder;
import com.asiainfo.dtdt.interfaces.IAppService;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.interfaces.order.IOrderRecordService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.order.IWoplatOrderService;
import com.asiainfo.dtdt.method.OrderMethod;
import com.asiainfo.dtdt.service.mapper.HisOrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.ProductMapper;
import com.asiainfo.dtdt.service.mapper.VcodeMapper;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 上午11:41:11 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
public class OrderServiceImpl implements IOrderService{

	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
	
//	@Autowired
//	private ICache cache;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private VcodeMapper vcodeMapper;
	
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
	
	/**
	 * (非 Javadoc) 
	* <p>Title: order</p> 
	* <p>Description: </p> 
	* @param jsonStr
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IOrderService#order(java.lang.String)
	 */
	public String preOrder(String jsonStr) {
		logger.info("OrderServiceImpl preOrder() jsonStr:" + jsonStr);
		/**获取接口中传递的参数  start*/
		JSONObject jsonObject =JSONObject.parseObject(jsonStr);
		String seq = jsonObject.getString("seq");
		String partnerCode = jsonObject.getString("partnerCode").toString();
		String appKey = jsonObject.getString("appKey").toString();
		String phone = jsonObject.getString("phone").toString();
		String productCode = jsonObject.getString("productCode").toString();
		String orderMethod = jsonObject.get("orderMethod").toString();
		String allowAutoPay = jsonObject.get("allowAutoPay").toString();
		String vcode = jsonObject.get("vcode").toString();
		String redirectUrl = jsonObject.get("redirectUrl").toString();
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
		listItem.put("amount", order.getPrice());
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
		order.setState("0");//状态
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
		if(StringUtils.contains(product.getProductCode().substring(2, 3), Constant.CYCLE_TYPE_01)){
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(new Date()));//包月当月失效时间
		}else if(StringUtils.contains(product.getProductCode().substring(2, 3), Constant.CYCLE_TYPE_02)){
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(DateUtil.getCurrentNextYear(new Date(),6)));//包半年失效时间
		}else if(StringUtils.contains(product.getProductCode().substring(2, 3), Constant.CYCLE_TYPE_03)){
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
		orderMapper.insertOrder(order);
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
		logger.info("orderServiceImpl preOrder checkOrderRecord is param:{productCode:"+productCode+",phone:"+phone+"}");
		List<OrderRecord> orderRecordList = orderRecordService.queryOrderRecordByParam(appKey,productCode, phone);
		if(orderRecordList.size() == 0){
			logger.info("orderServiceImpl preOrder checkOrderRecord not Existence order message;");
			return null;
		}
		logger.info("orderServiceImpl preOrder checkOrderRecord Existence order message;");
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
		logger.info("orderServiceImpl preOrder checkWoplatOrderRecord is param:{productCode:"+productCode+",phone:"+phone+",state:"+state+"}");
		String woplatOrder = woplatOrderService.queryWoplatOrderByParam(productCode, phone, state);//查询是否有同步的订购信息
		if(StringUtils.isBlank(woplatOrder)){
			logger.info("orderServiceImpl preOrder checkWoplatOrderRecord not Existence order message;");
			return null;
		}
		logger.info("orderServiceImpl preOrder checkWoplatOrderRecord Existence order message");
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
		logger.info("begin orderService paySuccessOrderDeposition param: resultCode="+resultCode+",orderId="+orderId);
		try {
			if("SUCCESS".equals(resultCode)){//支付返回成功
				/**检查是否存在互斥产品并存储在途数据**/
				Order order = orderMapper.selectByPrimaryKey(orderId);
				String woplatOrder = checkWoplatOrderRecord(order.getMobilephone(), null,"2");//订购成功
				if(StringUtils.isBlank(woplatOrder)){//检查沃家总管同步是否存在已订购的产品 不存在true 存在false
					logger.info("begin orderService paySuccessOrderDeposition checkWoplatOrderRecord not Existence woplatOrder");
					if(checkOrderRecord(order.getAppKey(),order.getMobilephone(), order.getProductCode()).size() <= 0){//检查我方是否存在已订购的产品 不存在true 存在false
						logger.info("begin orderService paySuccessOrderDeposition checkOrderRecord not Existence order");
						String proStr  = productService.queryProduct(order.getProductCode());
						Product product = JSONObject.parseObject(proStr, Product.class);
						//不存在订购信息，调用沃家总管订购接口
						String wjzgResult = OrderMethod.order(order.getMobilephone(), product.getWoProductCode(), DateUtil.getDateTime(order.getCreateTime()),order.getOrderChannel() );
						if(StringUtils.isBlank(wjzgResult)){
							//订购接口处理异常
							logger.info("orderService order product post wojia return fail param:orderId="+orderId);
							return;
						}
						JSONObject wjzgJson = JSONObject.parseObject(wjzgResult);
						String ecode = wjzgJson.getString("ecode");
						String woOrderId = wjzgJson.getString("orderId");
						if("0".equals(ecode)){
							logger.info("orderService order product post wojia return success param:orderId="+orderId);
							//订购成功，处理数据
							//woOrder 0-我方初始化订购
							//沉淀订购关系数据成功
							updateOrder(orderId, woOrderId, "10", Constant.IS_NEED_CHARGE_0,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
//							return optOrderRecord(orderId, woOrderId, Constant.WOORDER_TYPE_0, "10", order.getPartnerCode(),Constant.IS_NEED_CHARGE_0,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
							return;
						}else if("4001".equals(ecode)){
							//存在重复订购，处理业务
							logger.info("orderService order product post wojia return repeat order param:orderId="+orderId);
							//沃家返回重复订购，邮箱侧创建订购关系，返回订购成功，当前月不用反冲话费
							//*状态13：邮箱侧订购成功&沃家总管侧存在有效订购关系&无需返充话费，此时邮箱侧合作方查询该笔订购状态为：订购成功
							//疑问？：重复订购
							optOrderRecord(orderId, woOrderId, Constant.WOORDER_TYPE_1, "13", order.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
							orderPayBak(orderId, Constant.HISORDER_TYPE_0, "邮箱侧订购成功&沃家总管侧存在有效订购关系&无需返充话费");
							return;
						}else if("4005".equals(ecode)){
							//订购互斥产品
							//存在重复订购，处理业务
							logger.info("orderService order product post wojia return mutex order param:orderId="+orderId);
							//邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求，此时邮箱侧合作方查询该笔订购状态为：订购成功；
							optOrderRecord(orderId, woOrderId, Constant.WOORDER_TYPE_3, "14", order.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
							orderPayBak(orderId, Constant.HISORDER_TYPE_0, "沃家总管返回互斥订购：邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求");
							return ;
						}
					}else {//存在介入方产品使用此免流产品,重复订购
						//optOrderRecord(orderId, null, Constant.WOORDER_TYPE_3, "14", order.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
						logger.info("orderService order product repeat order param:appkey="+order.getAppKey()+",orderId="+orderId);
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
			logger.error("orderService paySuccessOrderDeposition fail:"+e.getMessage(),e);
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
				logger.info("orderService order product deposit orderRecord data success:orderId="+orderId);
				return true;
			}
		}
		logger.info("orderService order product deposit orderRecord data fail:orderId="+orderId);
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
		logger.info("OrderServiceImpl closeOrder() orderStr:" + orderStr);
		
		//获取参数
		JSONObject jsonObject = JSONObject.parseObject(orderStr);
		String orderId = jsonObject.getString("orderId");
		
		//参数校验
		if (StringUtils.isBlank(orderId)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "orderId" + Constant.PARAM_NULL_MSG, null);
		}
		
		//查询订购关系表：校验包月类订购为成功，只限包月类产产品
		OrderRecord orderRecord = null;
		try {
			orderRecord = orderRecordMapper.selectMonthProduct(orderId);
			if (null == orderRecord) {
				return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, "包月类" + Constant.PRODUCT_EXISTENCE_MSG, null);
			}
		} catch (Exception e) {
			logger.info("OrderServiceImpl closeOrder() selectMonthProduct() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
		
		//查询产品表
		Product product = null;
		try {
			product = productMapper.queryProduct(orderRecord.getProductCode());
			if (null == product) {
				return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
			}
		} catch (Exception e) {
			logger.info("OrderServiceImpl closeOrder() selectMonthProduct() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
				
		//设置返回参数
		JSONObject data = new JSONObject();
		data.put("partnerOrderId", orderRecord.getParentOrderId());
		data.put("orderId", orderRecord.getOrderId());
		data.put("timeStamp", DateUtil.getSysdateYYYYMMDDHHMMSS());
		
		//调用wojia退订接口：成功更新状态并迁移到历史表，返回接口成功信息
		String responseStr = closeOrderFromWojia(orderRecord);
		JSONObject responseJson = JSONObject.parseObject(responseStr);
		logger.info("responseJson ecode = " + responseJson.getString("ecode") + " responseJson emsg = " + responseJson.getString("emsg"));
		if (!StringUtils.isBlank(responseStr) && responseJson.getString("ecode").equals("0")) {
			try {
				orderRecord.setState("19");//设置状态为19-退订成功
				orderRecordMapper.updateOrderRecord(orderRecord);
				insertHisOrderRecord(orderRecord);
				orderRecordMapper.deleteOrderRecord(orderRecord.getOrderId());
			} catch (Exception e) {
				logger.info("OrderServiceImpl closeOrder() updateTables Exception e=" + e);
				return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
			}
			//如果退订成功还需要返回退订详细信息：流量包名称，退订时间，退订生效时间等
			data.put("productName", product.getProductName());
			data.put("refundTime", orderRecord.getRefundTime());
			data.put("refundValidTime", orderRecord.getRefundValidTime());
			return ReturnUtil.returnJsonInfo(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, data.toString());
		}else {
			//如果退订失败需返回失败原因
			data.put("invalidTime", orderRecord.getInvalidTime());
			logger.info("responseJson ecode = " + responseJson.getString("ecode") + " responseJson emsg = " + responseJson.getString("emsg"));
			data.put("failMsg", responseJson.getString("emsg"));
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, data.toString());
		}
		
	}
	
	/**
	* @Title: closeOrder 
	* @Description: 调wojia退订接口
	* @param orderRecord
	* @return String
	* @throws
	 */
	private String closeOrderFromWojia(OrderRecord orderRecord) {
		logger.info("OrderServiceImpl closeOrderFromWojia() orderRecord:" + orderRecord);
		
		JSONObject jsonParam = new JSONObject();
		String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
		jsonParam.put("seq", UUID.randomUUID());
		jsonParam.put("appId", Constant.APPID);
		jsonParam.put("operType", 2);//1.订购，2.退订
		jsonParam.put("msisdn", orderRecord.getMobilephone());
		jsonParam.put("productId", orderRecord.getProductCode());
//		jsonParam.put("productId", "fd3cd79e20c14728-984f32dbfa56713c");//TODO
		jsonParam.put("orderId", orderRecord.getWoOrderId());//订购id，operType为2时必填
		jsonParam.put("subscriptionTime", timeStamp);
		jsonParam.put("orderMethod", orderRecord.getOrderChannel());
		jsonParam.put("timeStamp", timeStamp);
		
		//vcode设置
		try {
			Vcode vcode = vcodeMapper.selectByOrderId(orderRecord.getOrderId());
			jsonParam.put("vcode", vcode.getLvcode());
		} catch (Exception e) {
			logger.info("OrderServiceImpl closeOrderFromWojia() selectByOrderId() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
		
		//应用签名 MD5：（appId + msisdn + timeStamp + appkey）
		String signStr = Constant.APPID + orderRecord.getMobilephone() + timeStamp + Constant.APPKEY;
		String appSignature = MD5Util.MD5Encode(signStr);
		jsonParam.put("appSignature", appSignature);
		
		//请求接口并返回
		String response = "";
		try {
			response = HttpClientUtil.httpPost(Constant.ORDER_URL, jsonParam);
		} catch (Exception e) {
			logger.info("OrderServiceImpl closeOrderFromWojia() httpPost Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
		return response;
	
	}
	
	/**
	* @Title: insertHisOrderRecord 
	* @Description: 根据订单关系表入库订单关系历史表
	* @param orderRecord void
	* @throws
	 */
	private void insertHisOrderRecord(OrderRecord orderRecord) {
		logger.info("OrderServiceImpl insertHisOrderRecord() orderRecord:" + orderRecord);
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
			logger.info("OrderServiceImpl insertHisOrderRecord() Exception e=" + e);
			e.printStackTrace();
		}
	}
	
}
