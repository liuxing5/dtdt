package com.asiainfo.dtdt.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.annotation.Resource;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dtdt.api.IAuthoService;


/**
 * 
 * Description: 
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 * @author 
 */
@RestController
@Log4j2
@RequestMapping(value="/autho", produces = APPLICATION_JSON_UTF8_VALUE)
public class AuthoController {

	@Resource
	private IAuthoService authoService;
	
    @RequestMapping(value = "smscode", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    public String getSMSCode(@RequestBody JSONObject json) {
    	log.debug("directional data traffic!");
		return authoService.getSMSCode("123");
    }
	
}
