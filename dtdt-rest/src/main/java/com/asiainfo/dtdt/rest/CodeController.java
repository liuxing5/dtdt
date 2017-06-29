package com.asiainfo.dtdt.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.dtdt.interfaces.ICodeService;
/**
 * @author liuixng5
 */
@Controller
@RequestMapping(value="/code", method=RequestMethod.POST)
public class CodeController {
	
	private static final Logger logger = Logger.getLogger(CodeController.class);
	
	@Autowired
	private ICodeService codeService;
	
	@RequestMapping(value="/getCode", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getToken(String phone) {
		logger.info("CodeController getCode() phone=" + phone);
		return codeService.getCode(phone);
	}
}
