package com.asiainfo.dtdt.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Vcode implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String orderId;

	private String vcodeSendTime;

	private String lvcode;

	private String userInputVcode;

	private String userInputTime;

	private String vcodeValidResut;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getVcodeSendTime() {
		return vcodeSendTime;
	}

	public void setVcodeSendTime(String vcodeSendTime) {
		this.vcodeSendTime = vcodeSendTime == null ? null : vcodeSendTime.trim();
	}

	public String getLvcode() {
		return lvcode;
	}

	public void setLvcode(String lvcode) {
		this.lvcode = lvcode == null ? null : lvcode.trim();
	}

	public String getUserInputVcode() {
		return userInputVcode;
	}

	public void setUserInputVcode(String userInputVcode) {
		this.userInputVcode = userInputVcode == null ? null : userInputVcode.trim();
	}

	public String getUserInputTime() {
		return userInputTime;
	}

	public void setUserInputTime(String userInputTime) {
		this.userInputTime = userInputTime == null ? null : userInputTime.trim();
	}

	public String getVcodeValidResut() {
		return vcodeValidResut;
	}

	public void setVcodeValidResut(String vcodeValidResut) {
		this.vcodeValidResut = vcodeValidResut == null ? null : vcodeValidResut.trim();
	}
}