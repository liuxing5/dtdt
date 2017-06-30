package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class HisCharge implements Serializable {

	private static final long serialVersionUID = 1L;

	private String chargeId;

	private String orderId;

	private String rechargeParentId;

	private String rechargePhoneNum;

	private Integer rechargeMoney;

	private Date rechageTime;

	private Integer rechageNum;

	private String state;

	private String result;

	private Date returnTime;

	private String chargeSysUsername;

	private String chargeSysPwd;

	private String remark;

	private Date copyTime;

	private String copyRemark;

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId == null ? null : chargeId.trim();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getRechargeParentId() {
		return rechargeParentId;
	}

	public void setRechargeParentId(String rechargeParentId) {
		this.rechargeParentId = rechargeParentId == null ? null : rechargeParentId.trim();
	}

	public String getRechargePhoneNum() {
		return rechargePhoneNum;
	}

	public void setRechargePhoneNum(String rechargePhoneNum) {
		this.rechargePhoneNum = rechargePhoneNum == null ? null : rechargePhoneNum.trim();
	}

	public Integer getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(Integer rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public Date getRechageTime() {
		return rechageTime;
	}

	public void setRechageTime(Date rechageTime) {
		this.rechageTime = rechageTime;
	}

	public Integer getRechageNum() {
		return rechageNum;
	}

	public void setRechageNum(Integer rechageNum) {
		this.rechageNum = rechageNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result == null ? null : result.trim();
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public String getChargeSysUsername() {
		return chargeSysUsername;
	}

	public void setChargeSysUsername(String chargeSysUsername) {
		this.chargeSysUsername = chargeSysUsername == null ? null : chargeSysUsername.trim();
	}

	public String getChargeSysPwd() {
		return chargeSysPwd;
	}

	public void setChargeSysPwd(String chargeSysPwd) {
		this.chargeSysPwd = chargeSysPwd == null ? null : chargeSysPwd.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Date getCopyTime() {
		return copyTime;
	}

	public void setCopyTime(Date copyTime) {
		this.copyTime = copyTime;
	}

	public String getCopyRemark() {
		return copyRemark;
	}

	public void setCopyRemark(String copyRemark) {
		this.copyRemark = copyRemark == null ? null : copyRemark.trim();
	}
}