package com.asiainfo.dtdt.service.mapper;

import com.asiainfo.dtdt.entity.Vcode;

public interface VcodeMapper {

	/**
	* @Title: VcodeMapper 
	* @Description: (记录有效的验证码) 
	* @param record
	* @return        
	* @throws
	 */
    int insertVcode(Vcode record);
    
	/**
	* @Title: selectByOrderId 
	* @Description: (根据orderId查询验证码) 
	* @param record
	* @return        
	* @throws
	 */
    Vcode selectByOrderId(String orderId);
    
}