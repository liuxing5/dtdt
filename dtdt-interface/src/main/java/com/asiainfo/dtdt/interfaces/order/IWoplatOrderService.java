package com.asiainfo.dtdt.interfaces.order;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年6月29日 下午5:02:31 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public interface IWoplatOrderService {

	/**
	* @Title: IWoplatOrderService 
	* @Description: (查询是否存在订购信息) 
	* @param productCode
	* @param mobilephone
	* @param state
	* @return        
	* @throws
	 */
    String queryWoplatOrderByParam(String productCode,String mobilephone,String state);
}
