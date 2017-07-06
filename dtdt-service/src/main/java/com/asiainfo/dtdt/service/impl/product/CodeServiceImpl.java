package com.asiainfo.dtdt.service.impl.product;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asiainfo.dtdt.entity.Vcode;
import com.asiainfo.dtdt.interfaces.ICodeService;
import com.asiainfo.dtdt.service.mapper.VcodeMapper;
/**
* @ClassName: CodeServiceImpl 
* @Description: (记录短信接口) 
* @author xiangpeng 
* @date 2017年7月6日 上午11:18:27 
*
 */
@Service("codeServiceImpl")
public class CodeServiceImpl implements ICodeService {
	
	
	@Autowired
	private VcodeMapper vcodeMapper;
	

	/**
	* @Title: CodeServiceImpl 
	* @Description: (这里用一句话描述这个方法的作用) 
	* @param lvcode
	* @param vcodeSendTime
	* @param orderId
	* @param userInputVcode
	* @param userInputTime
	* @param vcodeValidResut
	* @return        
	* @throws
	 */
	public int insertVcode(String lvcode,String vcodeSendTime,String orderId,String userInputVcode,String userInputTime,String vcodeValidResut) {
		Vcode vcode = new Vcode();
		vcode.setLvcode(lvcode);
		vcode.setOrderId(orderId);
		vcode.setVcodeSendTime(vcodeSendTime);
		vcode.setUserInputVcode(userInputVcode);
		vcode.setUserInputTime(userInputTime);
		vcode.setVcodeValidResut(vcodeValidResut);
		int n = vcodeMapper.insertVcode(vcode);
		return n;
	}

}
