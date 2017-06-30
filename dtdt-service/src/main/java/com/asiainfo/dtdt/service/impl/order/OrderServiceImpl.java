package com.asiainfo.dtdt.service.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.cache.ICache;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.BaseSeq;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.DateUtil;
import com.asiainfo.dtdt.common.IsMobileNo;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.entity.App;
import com.asiainfo.dtdt.entity.Order;
import com.asiainfo.dtdt.entity.Product;
import com.asiainfo.dtdt.interfaces.IAppService;
import com.asiainfo.dtdt.interfaces.ICodeService;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.interfaces.order.IOrderRecordService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.order.IWoplatOrderService;
import com.asiainfo.dtdt.service.mapper.OrderMapper;
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
	
	@Autowired
	private ICache cache;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Resource
	private ICodeService codeService;
	
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
		if(cache.exists("Y_" + partnerCode + appKey + phone)){//判断验证码是否存在
			return ReturnUtil.returnJsonError(Constant.SENDSMS_EXPIRED_CODE, Constant.SENDSMS_EXPIRED_MSG, null);//验证码过期不存在
		}
		String vaildateCode = (String) cache.getItem("Y_" + partnerCode + appKey + phone);//获取有效的验证码
		if(!StringUtils.equals(vcode, vaildateCode)){
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
		/**查询产品价格信息 end**/
		
		/**检查是否存在互斥产品并存储在途数据**/
		Order order = null;
		if(checkWoplatOrderRecord(phone, productCode)){//检查沃家总管同步是否存在已订购的产品 不存在true 存在false
			if(checkOrderRecord(phone, productCode)){//检查我方是否存在已订购的产品 不存在true 存在false
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
		codeService.insertVcode(cache.getItem("Y_" + partnerCode + appKey + phone).toString(),DateUtil.getDateTime(new Date()),order.getOrderId(),vcode,DateUtil.getDateTime(new Date()),"0");
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
	public boolean checkOrderRecord(String phone,String productCode){
		logger.info("orderServiceImpl preOrder checkOrderRecord is param:{productCode:"+productCode+",phone:"+phone+"}");
		String orderRecord = orderRecordService.queryOrderRecordByParam(productCode, phone);
		if(StringUtils.isBlank(orderRecord)){
			logger.info("orderServiceImpl preOrder checkOrderRecord not Existence order message;");
			return true;
		}
		logger.info("orderServiceImpl preOrder checkOrderRecord Existence order message;");
		return false;
	}

	/**
	* @Title: OrderServiceImpl 
	* @Description: (检查沃家总管同步是否存在已订购的产品) 
	* @param phone 订购手机号码
	* @param productCode 订购产品编码
	* @return        
	* @throws
	 */
	public boolean checkWoplatOrderRecord(String phone,String productCode){
		logger.info("orderServiceImpl preOrder checkWoplatOrderRecord is param:{productCode:"+productCode+",phone:"+phone+"}");
		String woplatOrder = woplatOrderService.queryWoplatOrderByParam(productCode, phone, "2");//查询是否有同步的订购信息
		if(StringUtils.isBlank(woplatOrder)){
			logger.info("orderServiceImpl preOrder checkWoplatOrderRecord not Existence order message;");
			return true;
		}
		logger.info("orderServiceImpl preOrder checkWoplatOrderRecord Existence order message");
		return false;
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
	
}
