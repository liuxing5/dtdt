package com.asiainfo.dtdt.service.impl.pay;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.entity.Payorder;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.service.mapper.PayorderMapper;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月30日 下午3:21:54 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
public class PayOrderServiceImpl implements IPayOrderService {

	private final static Log logger = LogFactory.getLog(PayOrderServiceImpl.class);
	
	@Autowired
	private PayorderMapper payorderMapper;
	
	/**
	 * (非 Javadoc) 
	* <p>Title: queryPayOrderByPayId</p> 
	* <p>Description: 查询支付订单信息</p> 
	* @param payId
	* @return 
	* @see com.asiainfo.dtdt.interfaces.pay.IPayOrderService#queryPayOrderByPayId(java.lang.String)
	 */
	public String queryPayOrderByPayId(String payId) {
		logger.info("payOrderServiceImpl queryPayOrderByPayId param:payId="+payId);
		return JSONObject.toJSONString(payorderMapper.queryPayOrderByPayId(payId));
	}

	/**
	 * (非 Javadoc) 
	* <p>Title: updatePayOrderStatusAfterPayNotify</p> 
	* <p>Description: 回调处理支付订单信息</p> 
	* @param resultCode 回调返回码
	* @param out_trade_no 订单号
	* @param transaction_id 第三方支付单号
	* @see com.asiainfo.dtdt.interfaces.pay.IPayOrderService#updatePayOrderStatusAfterPayNotify(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void updatePayOrderStatusAfterPayNotify(String resultCode, String out_trade_no, String transaction_id) {
		logger.info("payOrderService updatePayOrderStatusAfterPayNotify  begin,resultCode:"+resultCode+
				" out_trade_no:"+out_trade_no+" transaction_id:"+transaction_id);
		Payorder payorder = new Payorder();
		payorder.setPayId(out_trade_no);
		payorder.setThirdPayId(transaction_id);
		if("SUCCESS".equals(resultCode)){
			payorder.setState(Constant.PAY_STATUS_SUCCESS);
		}else{
			payorder.setState(Constant.PAY_STATUS_FAIL);
		}
		payorderMapper.updatePayOrder(payorder);
		logger.info("payOrderService updatePayOrderStatusAfterPayNotify end");
	}
	
	/**
	 * (非 Javadoc) 
	* <p>Title: insertPayOrder</p> 
	* @Description: (记录支付信息) 
	* @param payId 支付id
	* @param orderId 支付订单号
	* @param payMoney	支付金额
	* @param operType	支付类型
	* @param state	支付状态0-初始化，1-支付中，2-支付成功，3-支付失败，4-退款中，5退款成功，6退款失败
	* @return 
	* @see com.asiainfo.dtdt.interfaces.pay.IPayOrderService#insertPayOrder(java.lang.String, java.lang.String, java.lang.Long, byte, java.lang.String)
	 */
	public int insertPayOrder(String payId,String orderId,Long payMoney,byte operType,String state){
		Payorder payorder = new Payorder();
		payorder.setPayId(payId);
		payorder.setOrderId(orderId);
		payorder.setPayMoney(payMoney);
		payorder.setOperType(operType);
		payorder.setState(state);
		return payorderMapper.insertSelective(payorder);
	}

}
