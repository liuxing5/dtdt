package com.asiainfo.dtdt.service;


public interface IOrderResourceService {

	public String checkCounts(String partnerCode);
	public void sendWarnMsg(String partnerCode);
	public void refundOrderResource(String partnerCode);
}
