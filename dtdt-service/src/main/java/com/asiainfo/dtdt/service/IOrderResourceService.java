package com.asiainfo.dtdt.service;


public interface IOrderResourceService {

	public String checkCounts(String partnerCode);
	public void sendWarnMsg(String partnerCode, String hkey);
	public void refundOrderResource(String partnerCode);
}
