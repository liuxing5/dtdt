//package com.asiainfo.dtdt.controller;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.asiainfo.dtdt.interfaces.ITokenService;
///**
// * @author liuixng5
// */
//@Controller
//@RequestMapping(value="/token", method=RequestMethod.POST)
//public class TokenController {
//	
//	private static final Logger logger = Logger.getLogger(TokenController.class);
//	
//	@Autowired
//	private ITokenService tokenService;
//	
//	@RequestMapping(value="/getToken", produces = "application/json; charset=utf-8")
//	@ResponseBody
//	public String getToken(String partnerCode, String appKey) {
//		logger.info("TokenController getToken() partnerCode=" + partnerCode + " appKey=" + appKey);
//		return tokenService.getToken(partnerCode, appKey);
//	}
//}
