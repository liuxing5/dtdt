package com.asiainfo.dtdt.service.impl.pay;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.util.WcPayCommonUtil;
import com.asiainfo.dtdt.entity.Payorder;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayService;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月30日 下午2:55:49 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
public class PayServiceImpl implements IPayService {
	
	private final static Log logger = LogFactory.getLog(PayServiceImpl.class);
	
	private String wechat_partner_key;
	
	private String alipay_publicKey;
	
	private String alipay_charset;

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
			logger.info("isProcessed begin out_trade_no:"+out_trade_no);
			String porder = payOrderService.queryPayOrderByPayId(out_trade_no);
			Payorder payorder = JSONObject.parseObject(porder, Payorder.class);
			if(payorder==null){
				logger.info("isProcessed end false payorder==null out_trade_no:"+out_trade_no);
				return false;
			}
			payStatus=payorder.getState();
			if(Constant.PAY_STATUS_SUCCESS.equals(payStatus)||Constant.PAY_STATUS_FAIL.equals(payStatus)){
				logger.info("isProcessed end true out_trade_no:"+out_trade_no);
				return true;
			}
		} catch (Exception e) {
			logger.error("isProcessed Exception out_trade_no:"+out_trade_no,e);
		}
		logger.info("isProcessed end false out_trade_no:"+out_trade_no);
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
			logger.info("weChatCheckSign  begin,params:"+params);
			String sign = (String) params.get("sign");
			params.remove("sign");
			return (sign.equals(WcPayCommonUtil.createSign("UTF-8", params, wechat_partner_key)));
		} catch (Exception e) {
			//验签异常
			logger.error("weChatCheckSign  Exception",e);
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
			logger.info("aliPayCheckSign  begin,params:"+params);
			return AlipaySignature.rsaCheckV1(params, alipay_publicKey, alipay_charset);
		} catch (Exception e) {
			//验签异常
			logger.error("aliPayCheckSign  Exception",e);
		}
		return false;
	}

}
