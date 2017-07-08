package com.asiainfo.dtdt.service.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;




import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.awim.microservice.config.assistant.RedisAssistant;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.IsMobileNo;
import com.asiainfo.dtdt.common.RestClient;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.common.util.BaseSeq;
import com.asiainfo.dtdt.common.util.CheckParam;
import com.asiainfo.dtdt.common.util.DateUtil;
import com.asiainfo.dtdt.common.util.MD5Util;
import com.asiainfo.dtdt.common.util.RedisKey;
import com.asiainfo.dtdt.common.util.UuidUtil;
import com.asiainfo.dtdt.config.woplat.WoplatConfig;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.BatchOrder;
import com.asiainfo.dtdt.entity.HisOrder;
import com.asiainfo.dtdt.entity.HisOrderRecord;
import com.asiainfo.dtdt.entity.Order;
import com.asiainfo.dtdt.entity.OrderRecord;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.entity.WoplatOrder;
import com.asiainfo.dtdt.interfaces.IAppService;
import com.asiainfo.dtdt.interfaces.ICodeService;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.interfaces.order.INoticeService;
import com.asiainfo.dtdt.interfaces.order.IOrderRecordService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.order.IWoplatOrderService;
import com.asiainfo.dtdt.method.OrderMethod;
import com.asiainfo.dtdt.service.mapper.BatchOrderMapper;
import com.asiainfo.dtdt.service.mapper.HisOrderMapper;
import com.asiainfo.dtdt.service.mapper.HisOrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
import com.asiainfo.dtdt.service.mapper.OrderRecordMapper;
import com.asiainfo.dtdt.service.mapper.ProductMapper;
import com.asiainfo.dtdt.thread.BatchPostFixOrderThread;

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
	
	@Resource
	private WoplatConfig woplatConfig ;
	
	@Resource
	private RedisAssistant redisAssistant;
	
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private HisOrderMapper hisOrderMapper;
	
	@Autowired
	private OrderRecordMapper orderRecordMapper;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private HisOrderRecordMapper hisOrderRecordMapper;
	
	@Autowired
	private BatchOrderMapper batchOrderMapper;
	
	@Autowired
	private ICodeService codeService;
	
	@Autowired
	private IOrderRecordService orderRecordService;
	
	@Autowired
	private IWoplatOrderService woplatOrderService;
	
	@Autowired
	private IProductService productService;
	
	@Autowired
	private IAppService appService;
	
//	@Autowired
//	private IPayOrderService payOrderService;
	
	@Autowired
	private INoticeService noticeService;
	
	/**
	 * (非 Javadoc) 
	* <p>Title: order</p> 
	* <p>Description: </p> 
	* @param jsonStr
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IOrderService#order(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String preOrder(String jsonStr) {
		log.info("OrderServiceImpl preOrder() jsonStr:" + jsonStr);
		JSONObject jsonObject = null;
		/**获取接口中传递的参数  start*/
		try {
			jsonObject =JSONObject.parseObject(jsonStr);
		} catch (Exception e) {
			log.error("orderService preOrder check param is json error；"+e.getMessage(),e);
			e.printStackTrace();
			return ReturnUtil.returnJsonInfo(Constant.PARAM_ILLEGAL_CODE, Constant.PARAM_ILLEGAL_MSG, null);
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
			return ReturnUtil.returnJsonInfo(Constant.PARAM_ERROR_CODE, Constant.PARAM_ERROR_MSG, null);
		}
		/**获取接口中传递的参数  end*/
		/**校验接口中传递的参数是否合法  start*/
		if (StringUtils.isBlank(seq)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "seq"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(partnerCode)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "partnerCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(appKey)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "appKey"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(phone)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "phone"+Constant.PARAM_NULL_MSG, null);
		}
		if (!IsMobileNo.isMobile(phone)) {
			return ReturnUtil.returnJsonInfo(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
		}
		if (StringUtils.isBlank(productCode)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "productCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(orderMethod)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "orderMethod"+Constant.PARAM_NULL_MSG, null);
		}
		if(StringUtils.isBlank(allowAutoPay)){
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "allowAutoPay"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(vcode)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "vcode"+Constant.PARAM_NULL_MSG, null);
		}
		/**校验验证码是否正确 start*/
//		if(cache.exists("Y_" + partnerCode + appKey + phone)){//判断验证码是否存在
//			return ReturnUtil.returnJsonInfo(Constant.SENDSMS_EXPIRED_CODE, Constant.SENDSMS_EXPIRED_MSG, null);//验证码过期不存在
//		}
//		String vaildateCode = (String) cache.getItem("Y_" + partnerCode + appKey + phone);//获取有效的验证码
//		if(!StringUtils.equals(vcode, vaildateCode)){
//			return ReturnUtil.returnJsonInfo(Constant.SENDSMS_VALIDATE_CODE, Constant.SENDSMS_VALIDATE_MSG, null);//验证码错误
//		}
		/**校验验证码是否正确 end*/
		/**校验接口中传递的参数是否合法  end*/
		
		/**处理业务开始*/
		
		/**查询产品价格信息 start**/
		String strProduct = productService.queryProduct(productCode);
		if(StringUtils.isBlank(strProduct)){
			return ReturnUtil.returnJsonInfo(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
		}
		Product product = JSONObject.parseObject(strProduct, Product.class);
		/**查询产品价格信息 end**/
		
		/**检查是否存在互斥产品并存储在途数据**/
		Order order = null;
		String woplatOrder = checkWoplatOrderRecord(phone, null,"2");//订购成功
		if(StringUtils.isBlank(woplatOrder)){//检查沃家总管同步是否存在已订购的产品 不存在true 存在false
			if(checkOrderRecord(appKey,phone, productCode).size() <= 0){//检查我方是否存在已订购的产品 不存在true 存在false
				//检查沃家总管和我方都不存在需要记录预订购信息
				order = order(appKey, partnerCode, seq, phone, product, orderMethod, allowAutoPay,redirectUrl,"1");
			}else {
				return ReturnUtil.returnJsonInfo(Constant.VALIDATE_ORDER_EXISTENCE_CODE, Constant.VALIDATE_ORDER_EXISTENCE_MSG+productCode, null);
			}
		}else {//沃家总管存在已订购产品
			//沉淀订购信息
			order = order(appKey, partnerCode, seq, phone, product, orderMethod, allowAutoPay,redirectUrl,"1");
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
	 * 后向流量单个订购
	 */
	@Override
	public String postfixOrder(String jsonStr) {
		//参数校验
		log.info("OrderServiceImpl postfixOrder() jsonStr:" + jsonStr);
		JSONObject jsonObject = null;
		/**获取接口中传递的参数  start*/
		try {
			jsonObject =JSONObject.parseObject(jsonStr);
		} catch (Exception e) {
			log.error("orderService postfixOrder check param is json error；"+e.getMessage(),e);
			e.printStackTrace();
			return ReturnUtil.returnJsonError(Constant.PARAM_ILLEGAL_CODE, Constant.PARAM_ILLEGAL_MSG, null);
		}
//		String seq = null;
		String partnerCode = null;
		String appKey = null;
		String phone = null;
		String productCode = null;
		String orderMethod = null;
		String partnerOrderId = null;
		
		try {
//			seq = jsonObject.getString("seq");
			partnerCode = jsonObject.getString("partnerCode");
			appKey = jsonObject.getString("appKey");
//			partnerCode = "1234543245";
//			appKey = "fwerh4356ytrt54";
			phone = jsonObject.getString("phone").toString();
			productCode = jsonObject.getString("productCode").toString();
			orderMethod = jsonObject.get("orderMethod").toString();
			partnerOrderId = jsonObject.get("partnerOrderId").toString();
		} catch (NullPointerException e) {
			log.error("get param error is null");
			return ReturnUtil.returnJsonError(Constant.PARAM_ERROR_CODE, Constant.PARAM_ERROR_MSG, null);
		}
		/**获取接口中传递的参数  end*/
		
		boolean isBatch = false;
		if(jsonObject.containsKey("isBatch")){
			isBatch = jsonObject.getBoolean("isBatch");
		}
		
		/**校验接口中传递的参数是否合法  start*/
//		if (StringUtils.isBlank(seq)) {
//			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "seq"+Constant.PARAM_NULL_MSG, null);
//		}
		if (StringUtils.isBlank(partnerCode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(appKey)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "appKey"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(phone)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "phone"+Constant.PARAM_NULL_MSG, null);
		}
		
		if (StringUtils.isBlank(productCode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "productCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(orderMethod)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "orderMethod"+Constant.PARAM_NULL_MSG, null);
		}
		if(StringUtils.isBlank(partnerOrderId)){
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerOrderId"+Constant.PARAM_NULL_MSG, null);
		}
		/**校验验证码是否正确 start*/
		//是否可以订购
//		if(true){//如果不能订购返回原因
//			return null;
//		}
		
		//订购后向流量
		/**处理业务开始*/
		
		/**查询产品价格信息 start**/
		String strProduct = productService.queryProduct(productCode);
		if(StringUtils.isBlank(strProduct)){
			return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
		}
		Product product = JSONObject.parseObject(strProduct, Product.class);
		/**查询产品价格信息 end**/
		if (!IsMobileNo.isMobile(phone)) {
			if(isBatch){//历史数据表中插入失败数据并返回
				createHisOrder(appKey,partnerCode,partnerOrderId,
						phone,product,orderMethod,null,
						Constant.HISORDER_STATE_NOT_UNICOM,
						Constant.HISORDER_STATE_NOT_UNICOM_REMARK);//入历史表
				return ReturnUtil.returnJsonInfo(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
			}else{
				return ReturnUtil.returnJsonInfo(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
			}
		}
		/**检查是否存在互斥产品并存储在途数据**/
		Order order = null;
		//记录订购在途表
		order = order(appKey, partnerCode, partnerOrderId, phone, product, orderMethod,"0",null,"4");//待订购
		//发沃家起订购请求
		int num = updateOrder(order.getOrderId(), null, "9", (byte)0, (byte)0);
		String woResult = null;
		if(num > 0){
			woResult = postWoplatOrder(phone, product.getWoProductCode(), DateUtil.getDateTime(new Date()), order.getOrderChannel());
		}
		/**组装支付订单信息返回给接入商 start**/
		JSONObject json = new JSONObject();
		JSONObject woJson = JSONObject.parseObject(woResult);
		if(woplatConfig.getWoplat_success_code().equals(woJson.getString("ecode"))){
			updateOrder(order.getOrderId(),woJson.getString("orderId"), null,Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
			json.put("orderId", order.getOrderId());
			json.put("status", order.getState());
			json.put("money", order.getMoney());
			json.put("createTime", order.getCreateTime());
			List list = new ArrayList();
			JSONObject listItem = new JSONObject();
			listItem.put("productCode", order.getProductCode());
			listItem.put("productName", product.getProductName());
			listItem.put("productType", product.getCycleType());
			listItem.put("status", order.getState());
			listItem.put("price", order.getPrice());
			listItem.put("count", order.getCount());
			listItem.put("allowAutoPay", order.getAllowAutoPay());
			listItem.put("validTime", order.getValidTime());
			listItem.put("invalidTime", order.getInvalidTime());
			list.add(listItem);
			json.put("item", listItem);
			/**组装支付订单信息返回给接入商 end**/
			return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, json);
		}
		if(woJson.getString("ecode").equals(Constant.ERROR_CODE) || StringUtils.isBlank(woResult)){//请求沃家总管超时或者异常
			//更新在途订购订单状态7-订购失败
			updateOrder(order.getOrderId(),woJson.getString("orderId"), "7",Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
			//将在途表信息存放在备份表中
			insertOrderBakAndDelOrder(order.getOrderId(), "0", woJson.getString("emsg")+"),订购处理失败");
			json.put("orderId", order.getOrderId());
			return ReturnUtil.returnJsonInfo(Constant.ORDER_ERROR_CODE, Constant.ORDER_ERROR_MSG, json.toString());
		}
		//更新在途订购订单状态7-订购失败
		updateOrder(order.getOrderId(),woJson.getString("orderId"), "7",Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
		//将在途表信息存放在备份表中
		insertOrderBakAndDelOrder(order.getOrderId(), "0", woJson.getString("emsg")+",订购处理失败");
		json.put("orderId", order.getOrderId());
		String msg = null;
		if(woJson.getString("ecode").equals("1405")){
			msg = "用户 "+phone+" 不存在";
		}else if(woJson.getString("ecode").equals("4000")){
			msg = "产品 "+product.getProductName()+" 不存在或已失效";
		}else if(woJson.getString("ecode").equals("4001")){
			msg = "产品重复订购";
		}else if(woJson.getString("ecode").equals("4002")){
			msg = "产品无法订购";
		}else if(woJson.getString("ecode").equals("4005")){
			msg = "不能同时订购互斥产品";
		}
		return ReturnUtil.returnJsonInfo(Constant.ORDER_ERROR_CODE, Constant.ORDER_ERROR_MSG+msg, json.toString());
		
	}
	
	public void createHisOrder(String appKey,String partnerCode,String partnerOrderId,
			String phone,Product product,String orderMethod,
			String redirectUrl,String state,String copyRemark){
		HisOrder hisOrder = new HisOrder();
		hisOrder.setOrderId(BaseSeq.getLongSeq());//订购订单号
		hisOrder.setPartnerCode(partnerCode);//合作方编码
		hisOrder.setPartnerOrderId(partnerOrderId);//合作方订购订单ID
		hisOrder.setProductCode(product.getProductCode());//定向流量产品编码
		hisOrder.setOperType(Constant.ORDER_OPER_TYPE_0);//订购类型 订购：0，退订：1
		hisOrder.setIsRealRequestWoplat(Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);//是否真实请求沃家总管
		hisOrder.setState(state);//状态
		hisOrder.setMobilephone(phone);//订购手机号码
		hisOrder.setOrderChannel(orderMethod);//订购渠道
		hisOrder.setCreateTime(new Date());//订购时间
		hisOrder.setValidTime(new Date());//有效时间
		hisOrder.setRedirectUrl(redirectUrl);//支付成功跳转URL
		hisOrder.setPrice(product.getPrice());//产品价格
		hisOrder.setCount(1);//订购数量
		hisOrder.setMoney((long)product.getPrice()*hisOrder.getCount());//订单价格
		hisOrder.setAllowAutoPay("1");
		//根据接入方设置填写
		/**查询接入方设置的信息 start**/
		App app = JSONObject.parseObject(appService.queryAppInfo(appKey), App.class);
		/**查询接入方设置的信息 end**/
		hisOrder.setAppKey(appKey);//合作方产品ID
		hisOrder.setIsNeedCharge(app.getIsNeedCharge());//是否需要返充话费
		hisOrder.setCopyRemark(copyRemark);
		
		hisOrderMapper.insertSelective(hisOrder);
	}
	
	/**
	 * 后向流量批量订购
	 */
	@Override
	public String batchPostfixOrder(String jsonStr) {
		//参数校验
		log.info("OrderServiceImpl postfixOrder() jsonStr:" + jsonStr);
		JSONObject jsonObject = null;
		/**获取接口中传递的参数  start*/
		try {
			jsonObject =JSONObject.parseObject(jsonStr);
		} catch (Exception e) {
			log.error("orderService postfixOrder check param is json error；"+e.getMessage(),e);
			e.printStackTrace();
			return ReturnUtil.returnJsonError(Constant.PARAM_ILLEGAL_CODE, Constant.PARAM_ILLEGAL_MSG, null);
		}
//		String seq = null;
		String partnerCode = null;
		String appKey = null;
		JSONArray phones = null;
		String productCode = null;
		String orderMethod = null;
		String partnerOrderId = null;
		
		try {
//			seq = jsonObject.getString("seq");
			partnerCode = jsonObject.getString("partnerCode");
			appKey = jsonObject.getString("appKey");
//			partnerCode = "1234543245";
//			appKey = "fwerh4356ytrt54";
			phones = jsonObject.getJSONArray("phone");
			productCode = jsonObject.getString("productCode");
			orderMethod = jsonObject.getString("orderMethod").toString();
			partnerOrderId = jsonObject.getString("partnerOrderId");
		} catch (NullPointerException e) {
			log.error("get param error is null");
			return ReturnUtil.returnJsonError(Constant.PARAM_ERROR_CODE, Constant.PARAM_ERROR_MSG, null);
		}
		
		/**校验接口中传递的参数是否合法  start*/
		if(StringUtils.isBlank(partnerOrderId)){//校验partnerOrderId 唯一
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerOrderId"+Constant.PARAM_NULL_MSG, null);
		}else{
			if(1 == existPartnerOrderId(partnerOrderId)){
				return ReturnUtil.returnJsonError(Constant.PARTNERORDERID_EXIST_CODE, Constant.PARTNERORDERID_EXIST_MSG + partnerOrderId, null);
			}
		}
		
//		if (StringUtils.isBlank(seq)) {
//			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "seq"+Constant.PARAM_NULL_MSG, null);
//		}
		if (StringUtils.isBlank(partnerCode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(appKey)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "appKey"+Constant.PARAM_NULL_MSG, null);
		}
		
		if(null == phones || phones.size() <= 0 ){
			return ReturnUtil.returnJsonInfo(Constant.PHONES_ERROR_CODE, Constant.PHONES_ERROR_MSG, null);
		}
		
		if(phones.size() > 50 ){
			return ReturnUtil.returnJsonInfo(Constant.PHONES_TOO_LONG_CODE, Constant.PHONES_TOO_LONG_MSG, null);
		}
		
//		if (!IsMobileNo.isMobile(phones)) {
//			return ReturnUtil.returnJsonInfo(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
//		}
		if (StringUtils.isBlank(productCode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "productCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (StringUtils.isBlank(orderMethod)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "orderMethod"+Constant.PARAM_NULL_MSG, null);
		}

	
		/**查询产品价格信息 start**/
		String strProduct = productService.queryProduct(productCode);
		if(StringUtils.isBlank(strProduct)){
			return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
		}
		Product product = JSONObject.parseObject(strProduct, Product.class);
		if(product.getType() != 1){
			return ReturnUtil.returnJsonError(Constant.ORDER_TYPE_NOTFORWARD_CODE, Constant.ORDER_TYPE_NOTFORWARD_MSG+product.getProductName(), null);
		}
		BatchOrder batchOrder = null;
		
		batchOrder  = createBatchOrder(appKey, partnerCode, partnerOrderId,
				phones.toJSONString(), product, phones.size());
		
		if(null == batchOrder){//创建失败
			return ReturnUtil.returnJsonError(Constant.BATCH_ORDER_FAIL_CODE, Constant.BATCH_ORDER_FAIL_MSG
					+ partnerOrderId, null);
		}
		
		//创建线程拆单订购
		BatchPostFixOrderThread boThread = new BatchPostFixOrderThread(batchOrder);
		Thread t = new Thread(boThread);
		t.start();
				
		//返回订单受理成功
		JSONObject json = new JSONObject();
		json.put("batchOrderId", batchOrder.getBatchId());
		return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, json);
	}
	public int existPartnerOrderId(String partnerOrderId){
		return	orderMapper.existPartnerOrderId(partnerOrderId);
	}
	
	/**
	 * 创建批量订单
	 * @param appKey
	 * @param partnerCode
	 * @param partnerOrderId
	 * @param setMobilephones
	 * @param product
	 * @param state
	 * @param mobilephonesCount
	 * @return
	 */
	public BatchOrder createBatchOrder(String appKey,String partnerCode,String partnerOrderId,
			String setMobilephones,Product product,Integer mobilephonesCount){
		Date date = new Date();
		BatchOrder batchOrder = new BatchOrder();
		batchOrder.setBatchId(BaseSeq.getLongSeq());//订购订单号
		batchOrder.setAppKey(appKey);
		batchOrder.setPartnerCode(partnerCode);//合作方编码
		batchOrder.setPartnerOrderId(partnerOrderId);//合作方订购订单ID
		batchOrder.setProductCode(product.getProductCode());//定向流量产品编码
		batchOrder.setOperType(Constant.ORDER_OPER_TYPE_0);//订购类型 订购：0，退订：1
		batchOrder.setState("0");//初始状态0
		batchOrder.setCreateTime(date);
		batchOrder.setUpdateTime(date);
		batchOrder.setPrice(product.getPrice());
		batchOrder.setMobilephones(setMobilephones);
		batchOrder.setMobilephonesCount(mobilephonesCount);
		int i = batchOrderMapper.insert(batchOrder);
		if(1 != i)
			return null;
		return batchOrder;
	}

	/**
	* @Title: OrderServiceImpl 
	* @Description: (记录在途订购信息)         
	* @throws
	 */
	public Order order(String appKey,String partnerCode,String partnerOrderId,String phone,Product product,String orderMethod,String allowAutoPay,String redirectUrl,String state){
		Order order = new Order();
		order.setOrderId(BaseSeq.getLongSeq());//订购订单号
		order.setPartnerCode(partnerCode);//合作方编码
		order.setPartnerOrderId(partnerOrderId);//合作方订购订单ID
		order.setProductCode(product.getProductCode());//定向流量产品编码
		order.setOperType(Constant.ORDER_OPER_TYPE_0);//订购类型 订购：0，退订：1
		order.setIsRealRequestWoplat(Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);//是否真实请求沃家总管
		order.setState(state);//状态
		order.setMobilephone(phone);//订购手机号码
		order.setOrderChannel(orderMethod);//订购渠道
		order.setCreateTime(new Date());//订购时间
		order.setValidTime(new Date());//有效时间
		order.setRedirectUrl(redirectUrl);//支付成功跳转URL
		if(product.getType().equals((byte)1)){//包月后向流量
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(new Date()));//包月失效时间
			order.setAllowAutoPay((byte)1);
		}else if(StringUtils.contains(product.getProductCode().substring(2, 4), Constant.CYCLE_TYPE_02)){
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(DateUtil.getCurrentNextYear(new Date(),6)));//包半年失效时间
			order.setAllowAutoPay((byte)1);
		}else if(StringUtils.contains(product.getProductCode().substring(2, 4), Constant.CYCLE_TYPE_03)){
			order.setInvalidTime(DateUtil.getCurrentMonthEndTime(DateUtil.getCurrentNextYear(new Date(),12)));//包年失效时间
			order.setAllowAutoPay((byte)1);
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
	@SuppressWarnings("unchecked")
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
		/*if(Constant.ORDER_OPER_TYPE_0 == order.getOperType()){//如果是订购需要插入支付信息
			payOrderService.insertPayOrder(BaseSeq.getLongSeq(), order.getOrderId(), order.getMoney(), Constant.PAY_OPER_TYPE, Constant.PAY_STATE_INIT);
		}*/
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
							insertOrderBakAndDelOrder(orderId, Constant.HISORDER_TYPE_0, "邮箱侧订购成功&沃家总管侧存在有效订购关系&无需返充话费");
							noticeService.dtdtNoticeOrder(woOrderId);
							return;
						}else if("4005".equals(ecode)){
							//订购互斥产品
							//存在重复订购，处理业务
							log.info("orderService order product post wojia return mutex order param:orderId="+orderId);
							//邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求，此时邮箱侧合作方查询该笔订购状态为：订购成功；
							optOrderRecord(orderId, woOrderId, Constant.WOORDER_TYPE_3, "14", order.getPartnerCode(),Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
							insertOrderBakAndDelOrder(orderId, Constant.HISORDER_TYPE_0, "沃家总管返回互斥订购：邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求");
							noticeService.dtdtNoticeOrder(orderId);
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
					insertOrderBakAndDelOrder(orderId, Constant.HISORDER_TYPE_0, "沃家总管同步记录中存在订购关系：邮箱侧订购成功&沃家总管侧不存在有效订购关系&待邮箱侧向沃家总管侧发起订购请求");
					noticeService.dtdtNoticeOrder(orderId);
					return ;
				}
			}else {//支付失败
				//更新在途订购订单状态4-付款失败
				updateOrder(orderId,null, "4",Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
				//将在途表信息存放在备份表中
				insertOrderBakAndDelOrder(orderId, "2", "第三方支付失败");
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
			String cycleType = productCode.substring(2, 4);//当前订购流量包
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
	public void insertOrderBakAndDelOrder(String orderId,String copyType,String copyRemark){
		int num = orderMapper.insertFromHisOrderById(orderId, copyType, copyRemark);
		if(num > 0){
			orderMapper.deleteByPrimaryKey(orderId);//清除在途表信息
		}
	}
	
	/**
	 * (非 Javadoc) 
	* <p>Title: forwardOrder</p> 
	* <p>Description: (前向订购处理) </p> 
	* @param jsonStr
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IOrderService#forwardOrder(java.lang.String)
	 */
	@Override
	public String forwardOrder(String jsonStr) {
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
//		String seq = null;
		String phone = null;
		String appkey = null;
		String partnerCode = null;
		String productCode = null;
		String orderMethod = null;
//		String allowAutoPay = null;
		String vcode = null;
		String partnerOrderId = null;
		try {
//			seq = jsonObject.getString("seq");
			phone = jsonObject.getString("phone");
			appkey = jsonObject.getString("appkey");
			partnerCode = jsonObject.getString("partnerCode");
			productCode = jsonObject.getString("productCode");
			orderMethod = jsonObject.getString("orderMethod");
//			allowAutoPay = jsonObject.get("allowAutoPay").toString();
			vcode = jsonObject.getString("vcode");
			partnerOrderId = jsonObject.getString("partnerOrderId");
		} catch (NullPointerException e) {
			log.error("get param error is null");
			return ReturnUtil.returnJsonError(Constant.PARAM_ERROR_CODE, "seq,phone,productCode,orderMethod,vcode,partnerOrderId"+Constant.PARAM_ERROR_MSG, null);
		}
		/**获取接口中传递的参数  end*/
		/**校验接口中传递的参数是否合法  start*/
//		if (CheckParam.checkParamIsNull(seq)) {
//			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "seq"+Constant.PARAM_NULL_MSG, null);
//		}
		if (CheckParam.checkParamIsNull(phone)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "phone"+Constant.PARAM_NULL_MSG, null);
		}
		if (!IsMobileNo.isMobile(phone)) {
			return ReturnUtil.returnJsonInfo(Constant.NOT_UNICOM_CODE, Constant.NOT_UNICOM_MSG, null);
		}
		if (CheckParam.checkParamIsNull(productCode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "productCode"+Constant.PARAM_NULL_MSG, null);
		}
		if (CheckParam.checkParamIsNull(orderMethod)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "orderMethod"+Constant.PARAM_NULL_MSG, null);
		}
	/*	if(StringUtils.isBlank(allowAutoPay)){
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "allowAutoPay"+Constant.PARAM_NULL_MSG, null);
		}*/
		if (CheckParam.checkParamIsNull(vcode)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "vcode"+Constant.PARAM_NULL_MSG, null);
		}
		if (CheckParam.checkParamIsNull(partnerOrderId)) {
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerOrderId"+Constant.PARAM_NULL_MSG, null);
		}
		String paramErr =  CheckParam.checkParam(Constant.lengthParam, jsonStr);
		if(!CheckParam.checkParamIsNull(paramErr) && !"null".equals(paramErr)){
			return ReturnUtil.returnJsonError(Constant.PARAM_ERROR_CODE, Constant.PARAM_ERROR_MSG+":"+paramErr, null);
		}
		/**校验接口中传递的参数是否合法  start*/
		if(StringUtils.isBlank(partnerOrderId)){//校验partnerOrderId 唯一
			return ReturnUtil.returnJsonError(Constant.PARAM_NULL_CODE, "partnerOrderId"+Constant.PARAM_NULL_MSG, null);
		}else{
			if(1 == existPartnerOrderId(partnerOrderId)){
				return ReturnUtil.returnJsonError(Constant.PARTNERORDERID_EXIST_CODE, Constant.PARTNERORDERID_EXIST_MSG + partnerOrderId, null);
			}
		}
		/**校验验证码是否正确 start*/
		String vcodeKey = RedisKey.SMSC+"_"+partnerCode+"_"+appkey+"_"+phone;
		String redisVcode = redisAssistant.getStringValue(vcodeKey);
		if(StringUtils.isBlank(redisVcode)){//判断验证码是否存在
			return ReturnUtil.returnJsonError(Constant.SENDSMS_EXPIRED_CODE, Constant.SENDSMS_EXPIRED_MSG, null);//验证码过期不存在
		}
		if(!StringUtils.equals(vcode, redisVcode)){
			return ReturnUtil.returnJsonError(Constant.SENDSMS_VALIDATE_CODE, Constant.SENDSMS_VALIDATE_MSG, null);//验证码错误
		}
		/**校验验证码是否正确 end*/
		/**校验接口中传递的参数是否合法  end*/
		/**处理业务开始*/
		/**查询产品价格信息 start**/
		String strProduct = productService.queryProduct(productCode);
		if(StringUtils.isBlank(strProduct)){
			return ReturnUtil.returnJsonError(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
		}
		Product product = JSONObject.parseObject(strProduct, Product.class);
		if(product.getType() == 1){
			return ReturnUtil.returnJsonError(Constant.ORDER_TYPE_NOTFORWARD_CODE, Constant.ORDER_TYPE_NOTFORWARD_MSG+product.getProductName(), null);
		}
		/**查询产品价格信息 end**/
		
		/**检查是否存在互斥产品并存储在途数据**/
		Order order = null;
		//记录订购在途表
		order = order(appkey, partnerCode, partnerOrderId, phone, product, orderMethod, null,null,"4");//待订购
		//记录验证码信息
		codeService.insertVcode(redisVcode,DateUtil.getDateTime(new Date()),order.getOrderId(),vcode,DateUtil.getDateTime(new Date()),"0");//0-验证通过
		//清除redis中的验证码
		redisAssistant.clear(vcodeKey);
		//发沃家起订购请求
		int num = updateOrder(order.getOrderId(), null, "9", (byte)1, (byte)0);
		String woResult = null;
		if(num > 0){
			woResult = postWoplatOrder(phone, product.getWoProductCode(), DateUtil.getDateTime(new Date()), order.getOrderChannel());
		}
		/**组装支付订单信息返回给接入商 start**/
		JSONObject json = new JSONObject();
		JSONObject woJson = JSONObject.parseObject(woResult);
		if(woplatConfig.getWoplat_success_code().equals(woJson.getString("ecode"))){
			updateOrder(order.getOrderId(),woJson.getString("orderId"), null,Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_0);
			json.put("orderId", order.getOrderId());
//			json.put("partnerOrderId", order.getPartnerOrderId());
////			json.put("status", order.getState());
//			json.put("money", order.getMoney());
//			json.put("createTime", order.getCreateTime());
//			List list = new ArrayList();
//			JSONObject listItem = new JSONObject();
//			listItem.put("productCode", order.getProductCode());
//			listItem.put("productName", product.getProductName());
//			listItem.put("productType", product.getCycleType());
//			listItem.put("state", order.getState());
//			listItem.put("price", order.getPrice());
////			listItem.put("count", order.getCount());
//			listItem.put("allowAutoPay", order.getAllowAutoPay());
//			listItem.put("validTime", order.getValidTime());
//			listItem.put("invalidTime", order.getInvalidTime());
//			list.add(listItem);
//			json.put("item", listItem);
			/**组装支付订单信息返回给接入商 end**/
			return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, json);
		}
		if(woJson.getString("ecode").equals(Constant.ERROR_CODE) || StringUtils.isBlank(woResult)){//请求沃家总管超时或者异常
			//更新在途订购订单状态7-订购失败
			updateOrder(order.getOrderId(),woJson.getString("orderId"), "7",Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
			//将在途表信息存放在备份表中
			insertOrderBakAndDelOrder(order.getOrderId(), "0", woJson.getString("emsg")+"),订购处理失败");
//			json.put("orderId", order.getOrderId());
			return ReturnUtil.returnJsonInfo(Constant.ORDER_ERROR_CODE, Constant.ORDER_ERROR_MSG, null);
		}
		//更新在途订购订单状态7-订购失败
		updateOrder(order.getOrderId(),null, "7",Constant.IS_NEED_CHARGE_1,Constant.ORDER_IS_REAL_REQUEST_WOPLAT_1);
		//将在途表信息存放在备份表中
		insertOrderBakAndDelOrder(order.getOrderId(), "0", woJson.getString("emsg")+",订购处理失败");
//		json.put("orderId", order.getOrderId());
		String msg = null;
		if(woJson.getString("ecode").equals("1405")){
			msg = "用户 "+phone+" 不存在";
		}else if(woJson.getString("ecode").equals("4000")){
			msg = "产品 "+product.getProductName()+" 不存在或已失效";
		}else if(woJson.getString("ecode").equals("4001")){
			msg = "产品重复订购";
		}else if(woJson.getString("ecode").equals("4002")){
			msg = "产品无法订购";
		}else if(woJson.getString("ecode").equals("4005")){
			msg = "不能同时订购互斥产品";
		}
		return ReturnUtil.returnJsonInfo(Constant.ORDER_ERROR_CODE, Constant.ORDER_ERROR_MSG+msg, null);
	}
	
	/**
	 * @throws Exception 
	* @Title: OrderMethod 
	* @Description: (订购定向流量方法) 
	* @param operType 受理类型1-订购2-退订
	* @param msisdn 订购号码（联通手机号码）
	* @param productId	订购产品ID
	* @param subscriptionTime 订购时间
	* @param orderChannel 订购渠道1：APP 2：WEB 3：文件接口 4：其他
	* @return    订购ID    
	* @throws
	 */
	public  String postWoplatOrder(String msisdn,String productCode,String subscriptionTime,String orderChannel){
		log.info("**********请求沃家总管进行定向流量订购开始***********");
		JSONObject jsonObject = new JSONObject();
		String result = null;
		try {
			jsonObject.put("seq", UuidUtil.generateUUID());
			jsonObject.put("appId", woplatConfig.getWoAppId());
//			jsonObject.put("appId", Constant.APPID);
			jsonObject.put("operType", 1);
			jsonObject.put("msisdn", msisdn);
			jsonObject.put("productId", productCode);
			jsonObject.put("subscriptionTime", subscriptionTime);
			jsonObject.put("orderMethod",orderChannel);
			String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
			jsonObject.put("timeStamp", timeStamp);
			String signStr = woplatConfig.getWoAppId()+msisdn+timeStamp+woplatConfig.getWoAppKey();
//			String signStr = Constant.APPID+msisdn+timeStamp+Constant.APPKEY;
			jsonObject.put("appSignature", MD5Util.MD5Encode(signStr));
			log.info("post wojia order param:"+jsonObject.toString());
			result = RestClient.doRest(woplatConfig.getOrderUrl(), "POST", jsonObject.toString());
//			result = RestClient.doRest(Constant.ORDER_URL, "POST", jsonObject.toString());
			log.info("wojia order return result:"+result);
		} catch (Exception e) {
			log.error("post wojia order error:"+e.getMessage(), e);
			return null;
		}
		log.info("**********请求我家总管进行定向流量订购结束***********");
		return result;
	}
	
	/**
	* @Title: closeOrder 
	* @Description: 定向流量退订接口
	* @param orderStr
	* @param appkey
	* @return
	* @throws
	 */
	public String closeOrder(String orderStr, String appkey) {
		log.info("OrderServiceImpl closeOrder() orderStr:" + orderStr + " appkey=" + appkey);
		
		//获取参数
		JSONObject jsonObject = JSONObject.parseObject(orderStr);
		String orderId = jsonObject.getString("orderId");
		
		//参数校验
		if (StringUtils.isBlank(orderId)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "orderId" + Constant.PARAM_NULL_MSG, null);
		}
		if (orderId.length() != 32) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_LENGTH_CODE, "orderId" + Constant.PARAM_LENGTH_MSG, null);
		}
		
		//查询订购关系表：校验包月类订购为成功，只限包月类产产品
		OrderRecord orderRecord = null;
		try {
			orderRecord = orderRecordMapper.selectMonthProduct(orderId, appkey);
			if (null == orderRecord)
				return ReturnUtil.returnJsonInfo(Constant.NO_ORDER_CODE, Constant.NO_ORDER_MSG, null);
		} catch (Exception e) {
			log.info("OrderServiceImpl closeOrder() selectMonthProduct() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
			
		//查询产品表
		Product product = null;
		try {
			product = productMapper.queryProduct(orderRecord.getProductCode());
			if (null == product) {
				return ReturnUtil.returnJsonInfo(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
			}
			if(product.getType() == 1){
				return ReturnUtil.returnJsonError(Constant.ORDER_TYPE_NOTFORWARD_CODE, Constant.ORDER_TYPE_NOTFORWARD_MSG+product.getProductName(), null);
			}
		} catch (Exception e) {
			log.info("OrderServiceImpl closeOrder() queryProduct() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
		
		//如订单表 新orderId
		String newOrderId = BaseSeq.getLongSeq();
		insertOrder(newOrderId, orderRecord);
		
		log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder request start" + orderRecord);
		String wojiaStr = closeOrder(orderRecord.getMobilephone(), product.getWoProductCode(), orderRecord.getWoOrderId(), DateUtil.getSysdateYYYYMMDDHHMMSS(), orderRecord.getOrderChannel());
		
		//test data
//		JSONObject wojiaJson = new JSONObject();
//		wojiaJson.put("ecode", "0");
//		wojiaJson.put("emsg", "emsg");
		
		JSONObject wojiaJson = JSONObject.parseObject(wojiaStr);
		log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder response wojiaJson" + wojiaJson);
		
		//设置返回参数
		JSONObject data = new JSONObject();
		data.put("partnerOrderId", orderRecord.getPartnerCode());
		data.put("orderId", orderRecord.getOrderId());
		
		String ecode = wojiaJson.getString("ecode");
		String emsg = wojiaJson.getString("emsg");
		if("0".equals(ecode)){
			//如果退订成功还需要返回退订详细信息：流量包名称，退订时间，退订生效时间等
			log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder success ecode=" + ecode + " emsg=" + emsg);
			try {
				data.put("productName", product.getProductName());
				data.put("refundTime", DateUtil.getDateTime(orderRecord.getRefundTime()));
				data.put("refundValidTime", DateUtil.getDateTime(orderRecord.getRefundValidTime()));	
				return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, data);
			} catch (Exception e) {
				log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder getDateTime Exception e" + e);
				return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
			}
		} else {
			//如果退订失败需返回失败原因
			log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder order fail ecode=" + ecode + " emsg=" + emsg);
			//wojia返回一种信息： 	emsg:产品无法退订： orderId：0f9a7d11-8976-46ff-ad13-4950a1ed600d，这里统一返回给合作伙伴
			try {
				//t_s_order 表到 t_s_his_order 表
				insertFromHisOrderById(orderId, "0", "退订失败 ecode=" + ecode + " emsg=" + emsg);
				orderMapper.deleteByPrimaryKey(orderId);
			} catch (Exception e) {
				log.info("OrderServiceImpl closeOrder() OrderMethod.closeOrder order fail Exception e" + e);
				return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
			}
			return ReturnUtil.returnJsonObj(Constant.CLOSE_ORDER_FAIL_CODE, Constant.CLOSE_ORDER_FAIL_MSG, null);
		} 
	}
	
	/**
	* @Title: closeOrderNew
	* @Description: (定向流量退订接口)-new
	* @param orderStr
	* @param appkey
	* @return        
	* @throws
	 */
	public String closeOrderNew(String orderStr, String appkey) {
		log.info("OrderServiceImpl closeOrderNew() orderStr:" + orderStr + " appkey=" + appkey);
		
		//获取参数：合作方请求orderId（我方平台的orderId）
		JSONObject jsonObject = JSONObject.parseObject(orderStr);
		String orderId = jsonObject.getString("orderId");
		
		//参数校验
		if (StringUtils.isBlank(orderId)) {
			return ReturnUtil.returnJsonInfo(Constant.PARAM_NULL_CODE, "orderId" + Constant.PARAM_NULL_MSG, null);
		}
		
		//查询订购关系表：不校验包月类
		OrderRecord orderRecord = null;
		try {
			orderRecord = orderRecordMapper.selectByPrimaryKey(orderId);
			if (null == orderRecord)
				return ReturnUtil.returnJsonInfo(Constant.NO_ORDER_CODE, Constant.NO_ORDER_MSG, null);
		} catch (Exception e) {
			log.info("OrderServiceImpl closeOrderNew() selectByPrimaryKey() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
			
		//查询产品表：校验是否向前收费产品
		Product product = null;
		try {
			product = productMapper.queryProduct(orderRecord.getProductCode());
			if (null == product) {
				return ReturnUtil.returnJsonInfo(Constant.PRODUCT_EXISTENCE_CODE, Constant.PRODUCT_EXISTENCE_MSG, null);
			}
			if(product.getType() == 1){
				return ReturnUtil.returnJsonError(Constant.ORDER_TYPE_NOTFORWARD_CODE, Constant.ORDER_TYPE_NOTFORWARD_MSG+product.getProductName(), null);
			}
		} catch (Exception e) {
			log.info("OrderServiceImpl closeOrderNew() queryProduct() Exception e=" + e);
			return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
		}
		
		//如订单表 新orderId
		String newOrderId = BaseSeq.getLongSeq();
		insertOrder(newOrderId, orderRecord);
		
		log.info("OrderServiceImpl closeOrderNew() OrderMethod.closeOrder request start" + orderRecord);
		String wojiaStr = closeOrder(orderRecord.getMobilephone(), product.getWoProductCode(), orderRecord.getWoOrderId(), DateUtil.getSysdateYYYYMMDDHHMMSS(), orderRecord.getOrderChannel());
		
		//test data
//		JSONObject wojiaJson = new JSONObject();
//		wojiaJson.put("ecode", "0");
//		wojiaJson.put("emsg", "emsg");
		
		JSONObject wojiaJson = JSONObject.parseObject(wojiaStr);
		log.info("OrderServiceImpl closeOrderNew() OrderMethod.closeOrder response wojiaJson" + wojiaJson);
		
		//设置返回参数
		JSONObject data = new JSONObject();
		data.put("partnerOrderId", orderRecord.getPartnerCode());
		data.put("orderId", orderRecord.getOrderId());
		
		String ecode = wojiaJson.getString("ecode");
		String emsg = wojiaJson.getString("emsg");
		if("0".equals(ecode)){
			//如果退订成功还需要返回退订详细信息：流量包名称，退订时间，退订生效时间等
			log.info("OrderServiceImpl closeOrderNew() OrderMethod.closeOrder order success ecode=" + ecode + " emsg=" + emsg);
			try {
				data.put("productName", product.getProductName());
				data.put("refundTime", DateUtil.getDateTime(orderRecord.getRefundTime()));
				data.put("refundValidTime", DateUtil.getDateTime(orderRecord.getRefundValidTime()));	
				return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, data);
			} catch (Exception e) {
				log.info("OrderServiceImpl closeOrderNew() OrderMethod.closeOrder getDateTime Exception e" + e);
				return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
			}
		} else {
			//如果退订失败需返回失败原因
			log.info("OrderServiceImpl closeOrderNew() OrderMethod.closeOrder order fail ecode=" + ecode + " emsg=" + emsg);
			//wojia返回一种信息： 	emsg:产品无法退订： orderId：0f9a7d11-8976-46ff-ad13-4950a1ed600d，这里统一返回给合作伙伴
			
			try {
				//t_s_order 表到 t_s_his_order 表
				insertFromHisOrderById(orderId, "0", "退订失败 ecode=" + ecode + " emsg=" + emsg);
				orderMapper.deleteByPrimaryKey(orderId);
			} catch (Exception e) {
				log.info("OrderServiceImpl closeOrderNew() OrderMethod.closeOrder order fail Exception e" + e);
				return ReturnUtil.returnJsonInfo(Constant.ERROR_CODE, Constant.ERROR_MSG, null);
			}
			return ReturnUtil.returnJsonObj(Constant.CLOSE_ORDER_FAIL_CODE, Constant.CLOSE_ORDER_FAIL_MSG, null);
		}
	}
	
	/**
	 * 
	* @Title: insertOrder 
	* @Description: 根据订购关系表数据，在请求退订新生成一条在途数据
	* @param orderRecord void
	* @throws
	 */
	private void insertOrder(String newOrderId, OrderRecord orderRecord) {
		log.info("OrderServiceImpl insertOrder() newOrderId:" + newOrderId + " orderRecord=" + orderRecord);
		Order order = new Order();
		Date now = new Date();
		
		order.setOrderId(newOrderId);
		order.setWoOrderId(orderRecord.getWoOrderId());
		order.setPartnerCode(orderRecord.getPartnerCode());
		order.setAppKey(orderRecord.getAppKey());
		order.setPartnerOrderId(orderRecord.getPartnerOrderId());
		order.setProductCode(orderRecord.getProductCode());
		order.setOperType((byte)2);
		order.setRefundOrderId(orderRecord.getOrderId());
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
		order.setInvalidTime(DateUtil.getCurrentMonthEndTime(now));//月底
		order.setPrice(orderRecord.getPrice());
		order.setCount(orderRecord.getCount());
		order.setMoney(orderRecord.getMoney());
		order.setIsNeedCharge(orderRecord.getIsNeedCharge());
		order.setAllowAutoPay(orderRecord.getAllowAutoPay());
		order.setRedirectUrl(orderRecord.getRedirectUrl());
		order.setIsRealRequestWoplat((byte)1);
		order.setRemark("退订");
		try {
			orderMapper.insertOrder(order);
		} catch (Exception e) {
			log.info("OrderServiceImpl insertOrder() Exception: e=" + e);
			e.printStackTrace();
		}
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
		hisOrderRecord.setRemark("退订移历史表");
		try {
			hisOrderRecordMapper.insertSelective(hisOrderRecord);
		} catch (Exception e) {
			log.info("OrderServiceImpl insertHisOrderRecord() Exception e=" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @throws Exception 
	* @Title: OrderMethod 
	* @Description: (退订定向流量方法) 
	* @param operType 受理类型1-订购2-退订
	* @param msisdn 订购号码（联通手机号码）
	* @param productId	订购产品ID
	* @param orderId 订购ID
	* @param subscriptionTime 订购时间
	* @param orderMethod 订购渠道1：APP 2：WEB 3：文件接口 4：其他
	* @return        
	* @throws
	 */
	public String closeOrder(String msisdn,String productId,String orderId,String subscriptionTime,String orderMethod){
		log.info("**********请求沃家总管进行定向流量退订开始***********");
		JSONObject jsonObject = new JSONObject();
		String result = null;
		try {
			jsonObject.put("seq", UuidUtil.generateUUID());
			jsonObject.put("appId", woplatConfig.getWoAppId());
			jsonObject.put("operType", "2");
			jsonObject.put("msisdn", msisdn);
			jsonObject.put("productId", productId);
			jsonObject.put("orderId", orderId);
			jsonObject.put("subscriptionTime", subscriptionTime);
			jsonObject.put("orderMethod",orderMethod);
			String timeStamp = DateUtil.getSysdateYYYYMMDDHHMMSS();
			jsonObject.put("timeStamp", timeStamp);
			String signStr = woplatConfig.getWoAppId()+msisdn+timeStamp+woplatConfig.getWoAppKey();
			jsonObject.put("appSignature", MD5Util.MD5Encode(signStr));
			log.info("post wojia closeOrder param:" + jsonObject.toString());
//			result = HttpClientUtil.httpPost(woplatConfig.getOrderUrl(), jsonObject);
			result = RestClient.doRest(woplatConfig.getOrderUrl(), "POST", jsonObject.toString());
			log.info("wojia closeOrder return result:"+result);
		} catch (Exception e) {
			log.error("post wojia closeOrder error:"+e.getMessage(), e);
			return null;
		}
		log.info("**********请求沃家总管进行定向流量退订结束***********");
		return result;
	}
	
	/**
	* @Title: closeOrderUpdateTable 
	* @Description: 沃家退订接口回调后，更新 order和orderRecord表状态并迁移到历史表
	* @param newOrderId
	* @param orderRecord
	* @param state
	* @return String
	* @throws
	 */
	public void closeOrderUpdateTable(String orderId, String orderRecordJson, String state) {
		log.info("OrderServiceImpl closeOrderUpdateTable() orderRecordJson =" + orderRecordJson);
		
		OrderRecord orderRecord = JSONObject.parseObject(orderRecordJson, OrderRecord.class);
		Date date = new Date();
		try {
			//t_s_order 表到 t_s_his_order 表
			insertFromHisOrderById(orderId, "0", "包月退订");//copy_type：入表方式（0：包月退订 1：包半年、包年到期失效 2：人工操作）
			orderMapper.deleteByPrimaryKey(orderId);
			
			//t_s_order_record 表到 t_s_his_order_record 表
			orderRecord.setState(state);//设置状态：19-退订成功 23-退订失败
			orderRecord.setRemark("退订失败");
			orderRecord.setUpdateTime(date);
			orderRecord.setRefundTime(date);
			orderRecordMapper.updateOrderRecord(orderRecord);
			if ("19".equals(state)) {
				orderRecord.setRemark("退订成功");
				orderRecord.setRefundValidTime(DateUtil.getCurrentMonthEndTime(date));//月底
				insertHisOrderRecord(orderRecord);
				orderRecordMapper.deleteOrderRecord(orderRecord.getOrderId());
			}
		} catch (Exception e) {
			log.info("OrderServiceImpl closeOrderUpdateTable() Exception e=" + e);
			e.printStackTrace();
		}
	}

	@Override
	public int updateBatchOrderState(String batchOrderId) {
		return batchOrderMapper.updateBatchOrderState(batchOrderId);
	}
}
