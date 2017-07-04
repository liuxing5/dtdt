package com.asiainfo.dtdt.service.impl.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.common.util.StringUtil;
import com.asiainfo.dtdt.common.AESEncryptUtil;
import com.asiainfo.dtdt.common.BaseSeq;
import com.asiainfo.dtdt.common.Constant;
import com.asiainfo.dtdt.common.CryptogramUtil;
import com.asiainfo.dtdt.common.DataUtil;
import com.asiainfo.dtdt.common.EncodeUtils;
import com.asiainfo.dtdt.common.RestClient;
import com.asiainfo.dtdt.common.ReturnUtil;
import com.asiainfo.dtdt.common.request.HttpClientUtil;
import com.asiainfo.dtdt.config.woplat.WoplatConfig;
import com.asiainfo.dtdt.entity.Charge;
import com.asiainfo.dtdt.interfaces.order.IChargeService;
import com.asiainfo.dtdt.service.mapper.ChargeMapper;

import lombok.extern.log4j.Log4j2;

/** 
* @author 作者 : xiangpeng
* @date 创建时间：2017年7月3日 下午3:57:23 
* @version 1.0 
* @parameter 
* @since 
* @return 
*/
@Service
@Log4j2
public class ChargeServiceImpl implements IChargeService {

	@Autowired
	private ChargeMapper chargeMapper;
	
	/**
	 * (非 Javadoc) 
	* <p>Title: backChargeBill</p> 
	* <p>Description: 反冲话费</p> 
	* @param orderId 订单ID
	* @param amount 充值金额
	* @param phone 充值号码
	* @return 
	 * @see com.asiainfo.dtdt.interfaces.order.IChargeService#backChargeBill(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String backChargeBill(String orderId, int amount, String phone) {
		String chargeId = BaseSeq.getLongSeq();
		int num = insertCharge(chargeId, orderId, null, phone, amount, new Date());
		String result = null;
		Set<Integer> set = null;
		if(num > 0){//已记录充值信息
//			//面额数据
			try {
				set = getDenomination();
			} catch (Exception e) {
				log.error("chargeService  backChargeBill getDenomination error:"+e.getMessage(),e);
				e.printStackTrace();
				return ReturnUtil.returnJsonError( Constant.RECHARGE_ERROR_CODE, Constant.RECHARGE_ERROR_MSG, null);
			}
			//保存拆分后的金额
			List<Integer> resultList = new ArrayList<Integer>();
			
			DataUtil.splitNumber(amount,
					DataUtil.setToArrayDes(set), resultList);
			if(resultList.size() > 1){
				for (Integer rechargeMoney : resultList) {
					insertCharge(BaseSeq.getLongSeq(), orderId, chargeId, phone, rechargeMoney, new Date());
				}
				boolean isRecharge = false;//判断拆分充值返回是否为失败超时或者成功（true：失败／超时，false：成功）
				//先入库再调接口，防止拆分时调接口出现拆分不全
				List<Charge> chargeList = chargeMapper.getChargeByParentId(chargeId);
				for (int i = 0; i < chargeList.size(); i++) {
					Charge charge = chargeList.get(i);
					result = callChargeIntf(JSONObject.toJSONString(charge), charge.getChargeSysUsername(), charge.getChargeSysPwd());
					JSONObject callJson = JSONObject.parseObject(result);
					//充值平台充值失败或者超时做标记处理
					if(Constant.RECHARGE_FAIL_CODE.toString().equals(callJson.get("code"))){
						log.info("充值失败-进行标记");
						isRecharge = true;
					}else if(Constant.RECHARGE_TIMEOUT_CODE.toString().equals(callJson.get("code"))){
						log.info("充值超时-进行标记");
						isRecharge = true;
					}
					log.info("isRecharge :"+isRecharge);
				}
				//判断拆分充值中是否有失败超时记录,没有失败记录则更新拆分前记录的状态为成功
				if(!isRecharge){
					Charge parentCharge = new Charge();
					parentCharge.setChargeId(chargeId);
					parentCharge.setState("2");
					parentCharge.setReturnTime(new Date());
					int count = chargeMapper.updateByPrimaryKeySelective(parentCharge);
					log.info("拆分更新充值记录"+count+"成功："+parentCharge.getChargeId()+"更新状态为："+parentCharge.getState());
				}
			}else{
				Charge reCharge = chargeMapper.selectByPrimaryKey(chargeId);
				result = callChargeIntf(JSONObject.toJSONString(reCharge), reCharge.getChargeSysUsername(), reCharge.getChargeSysPwd());
			}
		}
		return result;
	}

	/**
	 * (非 Javadoc) 
	* <p>Title: insertCharge</p> 
	* <p>Description: 新增充值记录</p> 
	* @param chargeId 充值ID
	* @param orderId	订单ID
	* @param rechargeParentId	充值父ID
	* @param rechargePhoneNum 	充值号码
	* @param rechargeMoney	充值金额
	* @param rechargeTime	充值时间
	* @return 
	* @see com.asiainfo.dtdt.interfaces.order.IChargeService#insertCharge(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int insertCharge(String chargeId, String orderId, String rechargeParentId, String rechargePhoneNum,
			int rechargeMoney, Date rechargeTime) {
		Charge charge = new Charge();
		charge.setChargeId(chargeId);
		charge.setOrderId(orderId);
		charge.setRechargeParentId(rechargeParentId);
		charge.setRechargePhoneNum(rechargePhoneNum);
		charge.setRechargeMoney(rechargeMoney);
		charge.setRechageTime(rechargeTime);
		charge.setRechageNum(1);
		charge.setState("1");//状态（1：充值中 2：成功 3：失败 4：超时）
		charge.setChargeSysUsername("wojia");
		try {
			charge.setChargeSysPwd(CryptogramUtil.GenerateDigest("123qwe"));
		} catch (Exception e) {
			log.error("ChargeService insertCharge error:orderId"+orderId,e);
			e.printStackTrace();
		}
		return chargeMapper.insertSelective(charge);
	}
	
	/**
	* @Title: ChargeServiceImpl 
	* @Description: (充值服务) 
	* @param chargeStr 充值信息
	* @param chargeSysUserName 充值平台账号
	* @param chargeSysPwd 充值账号密码
	* @return        
	* @throws
	 */
	public String callChargeIntf(String chargeStr,String chargeSysUserName, String chargeSysPwd){
		Charge charge = JSONObject.parseObject(chargeStr, Charge.class);
		log.info("callChargeIntf Recharge:" + chargeStr);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String result = "";
		try {
			paramMap.put("user", AESEncryptUtil.encrypt(chargeSysUserName,Constant.CHARGEENCRYFACTOR));
			paramMap.put("userOrderNo", charge.getChargeId());
			paramMap.put("phoneNum", AESEncryptUtil.encrypt(charge.getRechargePhoneNum(), Constant.CHARGEENCRYFACTOR));
			paramMap.put("password", AESEncryptUtil.encrypt(chargeSysPwd, Constant.CHARGEENCRYFACTOR));
			paramMap.put("bill", String.valueOf(charge.getRechargeMoney()));
			log.info("request reqJson:" + paramMap.toString());
			result = HttpClientUtil.doRestChargeSys(Constant.RECHARGE_URL, paramMap);
			log.info("callChargeIntf result:" + result);
		} catch (Exception e) {
			log.info("callChargeIntf response error for update recharge to fail:" + e,e);
			charge.setReturnTime(new Date());
			charge.setState("3");//充值失败
			int n = chargeMapper.updateByPrimaryKeySelective(charge);
			if (n == 1){
				return ReturnUtil.returnJsonError( Constant.RECHARGE_ERROR_CODE, Constant.RECHARGE_ERROR_MSG, null);
			}
		}
		JSONObject resultJson  = JSONObject.parseObject(result.substring(1, result.length()-1).replaceAll("\\\\", ""));
		
		//3.更新充值表
		if (StringUtils.isNotEmpty(result)){
			charge.setResult(result);
			if (result.length() > 500){
				charge.setResult(result.substring(0, 500));
			}
		}
		charge.setReturnTime(new Date());
		charge.setRechageTime(new Date());
		try{
			//接口返回后处理
			if (checkSuccessCode(resultJson.getString("phonebillCode"))){
				charge.setState("2");//成功
				log.info("callChargeIntf " + charge.getRechargePhoneNum() + " 充值后开始更新充值表！");
				int up = chargeMapper.updateByPrimaryKeySelective(charge);
				if (up == 1){
					return ReturnUtil.returnJsonObj(Constant.SUCCESS_CODE, Constant.SUCCESS_MSG, charge);
				}
			}else if(checkTimeoutCode(resultJson.getString("phonebillCode"))){//充值超时判断
	        	charge.setState("4");//超时
				log.info("callChargeIntf " + charge.getRechargePhoneNum() + " 充值超时后开始更新充值表！");
				int up = chargeMapper.updateByPrimaryKeySelective(charge);
				if (up == 1){
					return ReturnUtil.returnJsonError(Constant.RECHARGE_TIMEOUT_CODE, Constant.RECHARGE_TIMEOUT_MSG, "phonebillCode=" + resultJson.getString("phonebillCode"));
				}
	        } else{
        		charge.setState("3");//充值处理失败（程序不再充值）
				
				log.info("callChargeIntf " + charge.getRechargePhoneNum() + " 充值失败更新充值表！");
				int up = chargeMapper.updateByPrimaryKeySelective(charge);
				if (up == 1){
					return ReturnUtil.returnJsonError(Constant.RECHARGE_FAIL_CODE, Constant.RECHARGE_FAIL_MSG, "phonebillCode=" + resultJson.getString("phonebillCode"));
				}
			}
		} catch (Exception e){
			log.info("callChargeIntf " + charge.getRechargePhoneNum() + " throw Exception" + e,e);
			return ReturnUtil.returnJsonError(Constant.RECHARGE_ERROR_CODE, Constant.RECHARGE_ERROR_MSG, "phonebillCode=" + resultJson.getString("phonebillCode"));
		}
		return ReturnUtil.returnJsonError(Constant.RECHARGE_ERROR_CODE, Constant.RECHARGE_ERROR_MSG, "phonebillCode=" + resultJson.getString("phonebillCode"));
	}
	
	private static WoplatConfig woplatConfig = new WoplatConfig();
	
	/**
	 * 验证充值是否成功
	 * @author liuxp5
	 * @date 2016-05-13
	 * @param rspresult
	 * @return
	 */
	public static boolean checkSuccessCode(String rspresult) {
		System.out.println("recharge_success_code:"+woplatConfig.getRecharge_success_code());
		if (woplatConfig.getRecharge_success_code().equals(rspresult)
				|| null == rspresult
				|| "null".equals(rspresult)
				|| ("recharge_success_code").indexOf(rspresult) != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 验证充值是否失败
	 * @author liuxp5
	 * @date 2016-05-13
	 * @param rspresult
	 * @return
	 */
	public static boolean checkFailCode(String rspresult) {
		System.out.println("recharge_fail_code:"+woplatConfig.getRecharge_fail_code());
		if (woplatConfig.getRecharge_fail_code().equals(rspresult)
				|| null == rspresult
				|| "null".equals(rspresult)
				|| ("recharge_fail_code").indexOf(rspresult) != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 验证充值是否超时
	 * @author liuxp5
	 * @date 2016-05-13
	 * @param rspresult
	 * @return
	 */
	public static boolean checkTimeoutCode(String rspresult) {
		System.out.println("recharge_timeout_code:"+woplatConfig.getRecharge_timeout_code());
		if (woplatConfig.getRecharge_timeout_code().equals(rspresult)
				|| null == rspresult
				|| "null".equals(rspresult)
				|| ("recharge_timeout_code").indexOf(rspresult) != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * @throws Exception 
	* @Title: ChargeServiceImpl 
	* @Description: (获取面额数据) 
	* @throws Exception        
	* @throws
	 */
	public Set<Integer> getDenomination() throws Exception{
		String pwd = EncodeUtils.encode(Constant.CHARGEENCRYFACTOR,
				Constant.CHARGESYSPASSWORD);
		String result;
		String username = Constant.CHARGESYSUSERNAME;
		String uri = Constant.GETDMTFROMCHARGESYSTM + "?userName=" + username +"&password=" + pwd;
		result = RestClient.doRest(uri,
				"GET","");
		JSONObject resultJson = JSONObject.parseObject(result);
		log.info("get RechargeDenomination From ChargeSystm reslut: "
				+ result);
		if (StringUtils.equals("success", resultJson.getString("success")))
		{
			if (!resultJson.getString("data").isEmpty())
			{
				return DataUtil.str2Set(resultJson.getString("data"), null, null);
			}
		}
		return null;
	}
}
