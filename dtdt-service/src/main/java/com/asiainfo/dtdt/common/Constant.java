package com.asiainfo.dtdt.common;
/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 上午10:19:12 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public class Constant {

	public static final String APPID = "209b6980-d0d5-4b64-a8e0-1eb384ad9de7";
	public static final String  APPKEY = "38d3e80a-e1e7-4b50-b50d-158c51b5bb0f";
	
	public static final String CHARGESYSUSERNAME = "wojia";
	
	public static final String CHARGESYSPASSWORD = "123qwe";
	
	public static final String CHARGEENCRYFACTOR = "mobile%userN%pwd";
	
	public static final String GETDMTFROMCHARGESYSTM = "http://120.197.60.118:21180/aiwmRecharge/query/denominationQuery.do";
	
	public static final String RECHARGE_URL	= "http://120.52.21.138:11186/RechargeApi/rest/activity/recharge.n";
	
	public static final String WJZG_RETURN_SUCCESS_CODE	= "0";
	
	/**处理成功码**/
	public static final String SUCCESS_CODE			= "00000";
	/**处理成功描述**/
	public static final String SUCCESS_MSG			= "成功";
	
	/**参数为空错误码**/
	public static final String PARAM_NULL_CODE		= "10000";
	/**参数为空错误描述**/
	public static final String PARAM_NULL_MSG		= "参数为空";
	
	/**参数错误码**/
	public static final String PARAM_ERROR_CODE		= "20000";
	/**参数错误描述**/
	public static final String PARAM_ERROR_MSG		= "参数错误";
	
	/**参数错误码**/
	public static final String PARAM_ILLEGAL_CODE		= "20001";
	/**参数错误描述**/
	public static final String PARAM_ILLEGAL_MSG		= "消息体不是合法的json字符串";
	
	/**签名错误码**/
	public static final String SIGN_ERROR_CODE		= "99998";
	/**签名错误描述**/
	public static final String SIGN_ERROR_MSG		= "签名错误";
	
	/**系统异常错误码**/
	public static final String ERROR_CODE			= "99999";
	/**系统异常错误描述**/
	public static final String ERROR_MSG			= "异常";
	
	/**沃家总管订购退订接口**/
	public static final String ORDER_URL  			= "http://120.52.120.106:9008/order";
	/**沃家总管查询订购信息接口**/
	public static final String QUERYORDER_URL  		= "http://120.52.120.106:9008/queryorder";
	
	/**非联通号码**/
	public static final String NOT_UNICOM_CODE 		= "20001";
	/**非联通号码描述**/
	public static final String NOT_UNICOM_MSG 		= "非联通号码";
	
	/**发送短信异常码**/
	public static final String SENDSMS_ERROR_CODE	= "30000";
	/**发送短信异常描述**/
	public static final String SENDSMS_ERROR_MSG	= "发送短信异常";
	
	/**短信验证码过期错误码**/
	public static final String SENDSMS_EXPIRED_CODE	= "30001";
	/**短信验证码过期错误描述**/
	public static final String SENDSMS_EXPIRED_MSG	= "验证码已过期";
	
	/**短信验证码验证错误码**/
	public static final String SENDSMS_VALIDATE_CODE	= "30002";
	/**短信验证码验证错误描述**/
	public static final String SENDSMS_VALIDATE_MSG	= "短信验证码错误";
	
	/**重复订购免流产品错误编码**/
	public static final String VALIDATE_ORDER_EXISTENCE_CODE	=	"40000";
	/**重复订购免流产品错误描述**/
	public static final String VALIDATE_ORDER_EXISTENCE_MSG		=	"重复订购产品：";
	
	/**订购免流产品不存在编码**/
	public static final String PRODUCT_EXISTENCE_CODE	=	"40001";
	/**订购免流产品不存在编码描述**/
	public static final String PRODUCT_EXISTENCE_MSG	=	"订购产品不存在";
	
	/**产品不存在编码**/
	public static final String NO_PRODUCT_CODE	=	"40002";
	/**产品不存在编码描述**/
	public static final String NO_PRODUCT_MSG	=	"无可订购产品";
	
	/**订单不存在编码**/
	public static final String NO_ORDER_CODE	=	"40010";
	/**订单不存在编码描述**/
	public static final String NO_ORDER_MSG	=	"订单不存在";
	
	/**定向流量产品第二三位表示产品周期*/
	/**包月**/
	public static final String CYCLE_TYPE_01	=	"01";
	/**包半年**/
	public static final String CYCLE_TYPE_02	=	"02";
	/**包年**/
	public static final String CYCLE_TYPE_03	=	"03";
	
	/**订购类型：1-订购**/
	public static final byte ORDER_OPER_TYPE_0	= 1;
	/**订购类型：2-退订**/
	public static final byte ORDER_OPER_TYPE_1	= 2;
	
	/***是否真实请求沃家总管（0：真实请求 1：未请求）如果我方同一手机号码，在多个app下订购了同一流量产品，则1、只有第一次订购会像沃家总管发起订购请求；2、只有最后一个退订时，才能真实像沃家总管发起退订请求；**/
	/**是否真实请求沃家总管 0-真实请求**/
	public static final byte ORDER_IS_REAL_REQUEST_WOPLAT_0	= 0;
	/**是否真实请求沃家总管 1-未请求**/
	public static final byte ORDER_IS_REAL_REQUEST_WOPLAT_1	= 1;
	
	/**订购渠道（APP、WEB、FILE：文件接口、Others：其他）**/
	/**订购渠道（APP）**/
	public static final String ORDER_CHANNEL_APP	=	"APP";
	/**订购渠道（WEB）**/
	public static final String ORDER_CHANNEL_WEB	=	"WEB";
	/**订购渠道（FILE：文件接口,沃家总管下发的全量订购信息）**/
	public static final String ORDER_CHANNEL_FILE	=	"FILE";
	/**订购渠道（Others：其他）**/
	public static final String ORDER_CHANNEL_OTHERS	=	"OTHERS";
	
	/**是否需要返充话费（0：需要）**/
	public static final byte IS_NEED_CHARGE_0		=	0;
	/**是否需要返充话费（1：不需要）**/
	public static final byte IS_NEED_CHARGE_1		=	1;
	
	/**充值类型 0-每月固定时间**/
	public static final byte CHARGE_TYPE_0			=	0;
	/**充值类型 1-一次性**/
	public static final byte CHARGE_TYPE_1			=	1;
	
	/**第三方支付状态**/
	/**0-初始化状态*/
	public static final String PAY_STATUS_INIT		= "0";
	/**1-支付失败*/
	public static final String PAY_STATUS_FAIL		= "1";
	/**2-支付成功*/
	public static final String PAY_STATUS_SUCCESS		= "2";
	/**3-退款中*/
	public static final String PAY_STATUS_REFUNDINIT		= "3";
	/**4-退款失败*/
	public static final String PAY_STATUS_REUNFFAIL		= "4";
	/**5-退款成功*/
	public static final String PAY_STATUS_REFUNDSUCCESS		= "5";
	
	/**0：我方初始化订购 1：其他代理商订购 2：其他代理商订购失效、到期或退订由我方续订 3：由我方订购互斥产品**/
	public static final String WOORDER_TYPE_0	= "0";
	/**1：其他代理商订购 **/
	public static final String WOORDER_TYPE_1	= "1";
	/**2：其他代理商订购失效、到期或退订由我方续订**/
	public static final String WOORDER_TYPE_2	= "2";
	/**3：由我方订购互斥产品**/
	public static final String WOORDER_TYPE_3	= "3";
	
	/**入表方式（0：订单完工 1：未支付失效 2：人工操作）**/
	public static final String HISORDER_TYPE_0	= "0";
	/**1：未支付失效 **/
	public static final String HISORDER_TYPE_1	= "1";
	/**2：人工操作**/
	public static final String HISORDER_TYPE_2	= "2";
	
	/**支付类型，1-支付，2退款**/
	public static final byte PAY_OPER_TYPE	=	1;
	public static final byte PAY_REFUND_OPER_TYPE	=	2;
	
	/**支付状态0-初始化，1-支付中，2-支付成功，3-支付失败，4-退款中，5退款成功，6退款失败**/
	public static final String PAY_STATE_INIT		=	"0";
	public static final String PAYMENT_STATE		=	"1";
	public static final String PAY_STATE_SUCCESS	=	"2";
	public static final String PAY_STATE_FAIL		=	"3";
	public static final String PAY_STATE_REFUND		=	"4";
	public static final String PAY_STATE_REFUND_SUCCESS	=	"5";
	public static final String PAY_STATE_REFUND_FAIL	=	"6";
	
	/**
     * 充值失败
     */
    public static final Integer RECHARGE_FAIL_CODE		   		= 916;
    public static final String RECHARGE_FAIL_MSG		   		= "充值失败";
    /**
     * 充值异常
     */
    public static final Integer RECHARGE_ERROR_CODE		   		= 917;
    public static final String RECHARGE_ERROR_MSG		   		= "充值异常";
    /**
     * 充值异常
     */
    public static final Integer RECHARGE_TIMEOUT_CODE		   		= 918;
    public static final String RECHARGE_TIMEOUT_MSG		   		= "充值超时";

}
