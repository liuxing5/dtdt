package com.asiainfo.dtdt.service.impl.auth;

import com.alibaba.dubbo.config.annotation.Service;
import com.asiainfo.dtdt.api.IAuthoService;

@Service
public class AuthoServiceImpl implements IAuthoService
{

	public String getSMSCode(String phoneNum)
	{
		return "hello elf!";
	}

}
