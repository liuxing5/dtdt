package com.asiainfo.dtdt.interfaces.order;

import java.util.Date;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年7月3日 下午3:54:02 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
public interface IChargeService {
	
	/**
	* @Title: IChargeService 
	* @Description: (反冲话费) 
	* @param orderId
	* @param amount
	* @param phone
	* @return        
	* @throws
	 */
	String backChargeBill(String orderId,int amount,String phone);
	
	/**
	* @Title: IChargeService 
	* @Description: (新增充值记录) 
	* @param chargeJson
	* @return        
	* @throws
	 */
	int insertCharge(String chargeId,String orderId,String rechargeParentId,String rechargePhoneNum,int rechargeMoney,Date rechargeTime);
	
}
