package com.asiainfo.dtdt.interfaces.order;


/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 下午5:02:13 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public interface IOrderRecordService {
	
	/**
	* @Title: IOrderRecordService 
	* @Description: (查询订购关系) 
	* @param productCode 	订购产品编码
	* @param mobilephone	订购联通号码
	* @return        
	* @throws
	 */
	String queryOrderRecordByParam(String productCode,String mobilephone);
}
