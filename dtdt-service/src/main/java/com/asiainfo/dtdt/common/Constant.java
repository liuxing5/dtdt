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

	/**处理成功码**/
	public static final String SUCCESS_CODE	= "00000";
	/**处理成功描述**/
	public static final String SUCCESS_MSG	= "成功";
	
	/**参数为空错误码**/
	public static final String PARAM_NULL_CODE	= "10000";
	/**参数为空错误描述**/
	public static final String PARAM_NULL_MSG	= "为空";
	
	/**参数错误码**/
	public static final String PARAM_ERROR_CODE	= "20000";
	/**参数错误描述**/
	public static final String PARAM_ERROR_MSG	= "参数错误";
	
	/**签名错误码**/
	public static final String SIGN_ERROR_CODE	= "99998";
	/**签名错误描述**/
	public static final String SIGN_ERROR_MSG	= "签名错误";
	
	/**系统异常错误码**/
	public static final String ERROR_CODE	= "99999";
	/**系统异常错误描述**/
	public static final String ERROR_MSG	= "异常";
	
	/**沃家总管订购退订接口**/
	public static final String ORDER_URL  = "http://<url>/<path>/order";
	/**沃家总管查询订购信息接口**/
	public static final String QUERYORDER_URL  = "http://<url>/<path>/queryorder";
	
}
