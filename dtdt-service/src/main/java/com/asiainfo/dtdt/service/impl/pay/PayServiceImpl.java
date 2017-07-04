package com.asiainfo.dtdt.service.impl.pay;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.util.AliPayCommonUtil;
import com.asiainfo.dtdt.common.util.WcPayCommonUtil;
import com.asiainfo.dtdt.common.util.XmlUtils;
import com.asiainfo.dtdt.entity.Payorder;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayService;

import lombok.extern.log4j.Log4j2;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月30日 下午2:55:49 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Log4j2
@Service
public class PayServiceImpl implements IPayService {
	
	private String wechat_partner_key;
	
	private String alipay_publicKey;
	
	private String alipay_charset;

	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IPayOrderService payOrderService;
	/**
	 * (非 Javadoc) 
	* <p>Title: isProcessed</p> 
	* <p>Description: 判断业务是否已经处理过</p> 
	* @param out_trade_no
	* @return 
	* @see com.asiainfo.dtdt.interfaces.pay.IPayService#isProcessed(java.lang.String)
	 */
	public boolean isProcessed(String out_trade_no) {
		String payStatus = "";
		try {
			log.info("isProcessed begin out_trade_no:"+out_trade_no);
			String porder = payOrderService.queryPayOrderByPayId(out_trade_no);
			Payorder payorder = JSONObject.parseObject(porder, Payorder.class);
			if(payorder==null){
				log.info("isProcessed end false payorder==null out_trade_no:"+out_trade_no);
				return false;
			}
			payStatus=payorder.getState();
			if(Constant.PAY_STATUS_SUCCESS.equals(payStatus)||Constant.PAY_STATUS_FAIL.equals(payStatus)){
				log.info("isProcessed end true out_trade_no:"+out_trade_no);
				return true;
			}
		} catch (Exception e) {
			log.error("isProcessed Exception out_trade_no:"+out_trade_no,e);
		}
		log.info("isProcessed end false out_trade_no:"+out_trade_no);
		return false;
	}

	/**
	* <p>Title: weChatCheckSign</p> 
	* <p>Description: 微信回调验签</p> 
	* @param params
	* @return 
	* @see com.asiainfo.dtdt.interfaces.pay.IPayService#weChatCheckSign(java.util.Map)
	 */
	public boolean weChatCheckSign(Map params){
		try {
			log.info("weChatCheckSign  begin,params:"+params);
			String sign = (String) params.get("sign");
			params.remove("sign");
			return (sign.equals(WcPayCommonUtil.createSign("UTF-8", params, wechat_partner_key)));
		} catch (Exception e) {
			//验签异常
			log.error("weChatCheckSign  Exception",e);
		}
		return false;
	}
	
	/**
	 * (非 Javadoc) 
	* <p>Title: aliPayCheckSign</p> 
	* <p>Description: 支付宝回调验签</p> 
	* @param params
	* @return 
	* @see com.asiainfo.dtdt.interfaces.pay.IPayService#aliPayCheckSign(java.util.Map)
	 */
	public boolean aliPayCheckSign(Map params){
		try {
			log.info("aliPayCheckSign  begin,params:"+params);
			return AlipaySignature.rsaCheckV1(params, alipay_publicKey, alipay_charset);
		} catch (Exception e) {
			//验签异常
			log.error("aliPayCheckSign  Exception",e);
		}
		return false;
	}

	public boolean wcPayNotify(String jsonStr) {
		log.info("payService wcPayNotify  begin");
		try {
			Map map =XmlUtils.doXMLParse(jsonStr);
	        log.info("==wcPayNotify==weChatCheckSign success=="+" map:"+map);
	        String returnCode = (String) map.get("return_code");
			if (returnCode.equals("SUCCESS")) {
				//验签不通过
				if(!weChatCheckSign(map)){
					log.error("==wcPayNotify==weChatCheckSign fail=="+" map:"+map);
					return false; 
				}
				String resultCode = (String) map.get("result_code");
				// 获取商户订单号(payId)
				String out_trade_no = (String) map.get("out_trade_no");
				String transaction_id = (String) map.get("transaction_id");
				//判断业务是否已经处理过
				if(isProcessed(out_trade_no)){
					log.info("==wcPayNotify==isProcessed==微信回调,业务已经处理过");
					return true;
				}
				log.info("==wcPayNotify==isProcessed==微信回调,业务正常处理");
				try {
					log.info("==wcPayNotify==updatePayOrderStatusAfterPayNotify==begin");
					//数据沉淀
					/**更新充值状态 start**/
					payOrderService.updatePayOrderStatusAfterPayNotify(resultCode, out_trade_no,transaction_id);
					/**更新充值状态 end**/
					log.info("==wcPayNotify==updatePayOrderStatusAfterPayNotify==success");
				} catch (Exception e) {
					log.error("==wcPayNotify==updatePayOrderStatusAfterPayNotify==exception",e);
					return false;
				}
				/**支付成功沉淀订购关系数据 start**/
				String payStr = payOrderService.queryPayOrderByPayId(out_trade_no);
				Payorder payorder = JSONObject.parseObject(payStr, Payorder.class);
				orderService.paySuccessOrderDeposition(resultCode, payorder.getOrderId());
				/**支付成功沉淀订购关系数据 end**/
				return true;
			}
		} catch (Exception e) {
			log.error("==wcPayNotify==wcPayNotify==exception"+e.getMessage(),e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * (非 Javadoc) 
	* <p>Title: aliPayNotify</p> 
	* <p>Description: </p> 
	* @param params
	* @return 
	* @see com.asiainfo.dtdt.interfaces.pay.IPayService#aliPayNotify(java.util.Map)
	 */
	public boolean aliPayNotify(Map<String,String> params) {
		log.info("aliPayNotify begin,params:"+params);
		//异步通知ID
		String notify_id= params.get("notify_id");
		//商户订单号
		String out_trade_no = params.get("out_trade_no");
		//判断业务是否已经处理过
		if(isProcessed(out_trade_no)){
			log.info("==aliPayNotify==isProcessed==支付宝回调,业务已经处理过");
			return true;
		}
		log.info("==aliPayNotify==isProcessed==支付宝回调,业务正常处理");
		//支付宝交易号  
		String trade_no = params.get("trade_no");
		//交易状态
		String trade_status = params.get("trade_status");
		try {
			log.info("==aliPayNotify==updateRedpacketPayStatus==begin,trade_status:"+trade_status);
			//数据沉淀
			if("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)){
				orderService.paySuccessOrderDeposition("SUCCESS", out_trade_no);
			}else{
				orderService.paySuccessOrderDeposition("FAIL", out_trade_no);
			}
			log.info("==aliPayNotify==updateRedpacketPayStatus==success");
			//响应支付宝
			return true;
		} catch (Exception e) {
			log.error("==aliPayNotify==updateRedpacketPayStatus==exception"+e.getMessage(),e);
			e.printStackTrace();
		}
		return false;
	}
	
}
