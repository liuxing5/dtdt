package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Payorder implements Serializable {

	private static final long serialVersionUID = 1L;

	private String payId;

	private String orderId;

	private String payAccount;

	private Long payMoney;

	private String payType;

	private Byte operType;

	private String state;

	private Long originRefoundMoney;

	private Date originRefoundTime;

	private Date manMadeRefoundTime;

	private String manMadeRefoundType;

	private Date accountDay;

	private String prePayId;

	private String thirdPayId;

	private Date updateTime;

	private Date createTime;

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId == null ? null : payId.trim();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount == null ? null : payAccount.trim();
	}

	public Long getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Long payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType == null ? null : payType.trim();
	}

	public Byte getOperType() {
		return operType;
	}

	public void setOperType(Byte operType) {
		this.operType = operType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public Long getOriginRefoundMoney() {
		return originRefoundMoney;
	}

	public void setOriginRefoundMoney(Long originRefoundMoney) {
		this.originRefoundMoney = originRefoundMoney;
	}

	public Date getOriginRefoundTime() {
		return originRefoundTime;
	}

	public void setOriginRefoundTime(Date originRefoundTime) {
		this.originRefoundTime = originRefoundTime;
	}

	public Date getManMadeRefoundTime() {
		return manMadeRefoundTime;
	}

	public void setManMadeRefoundTime(Date manMadeRefoundTime) {
		this.manMadeRefoundTime = manMadeRefoundTime;
	}

	public String getManMadeRefoundType() {
		return manMadeRefoundType;
	}

	public void setManMadeRefoundType(String manMadeRefoundType) {
		this.manMadeRefoundType = manMadeRefoundType == null ? null : manMadeRefoundType.trim();
	}

	public Date getAccountDay() {
		return accountDay;
	}

	public void setAccountDay(Date accountDay) {
		this.accountDay = accountDay;
	}

	public String getPrePayId() {
		return prePayId;
	}

	public void setPrePayId(String prePayId) {
		this.prePayId = prePayId == null ? null : prePayId.trim();
	}

	public String getThirdPayId() {
		return thirdPayId;
	}

	public void setThirdPayId(String thirdPayId) {
		this.thirdPayId = thirdPayId == null ? null : thirdPayId.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}