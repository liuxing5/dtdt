package com.asiainfo.dtdt.entity;

import java.util.Date;

public class HisOrderRecord {
    private String orderId;

    private String parentOrderId;

    private String partnerCode;

    private String appKey;

    private String partnerOrderId;

    private Byte cycleType2;

    private String productCode;

    private String state;

    private String mobilephone;

    private String orderChannel;

    private Integer price;

    private Integer count;

    private Long money;

    private Byte isNeedCharge;

    private Byte operSource;

    private Byte allowAutoPay;

    private Byte woOrder;

    private String remark;

    private Date refundValidTime;

    private Date refundTime;

    private Date validTime;

    private Date invalidTime;

    private Date updateTime;

    private Date createTime;

    private Date copyTime;

    private Byte copyType;

    private String copyRemark;
    
    private String redirectUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId == null ? null : parentOrderId.trim();
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode == null ? null : partnerCode.trim();
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey == null ? null : appKey.trim();
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId == null ? null : partnerOrderId.trim();
    }

    public Byte getCycleType2() {
        return cycleType2;
    }

    public void setCycleType2(Byte cycleType2) {
        this.cycleType2 = cycleType2;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public String getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(String orderChannel) {
        this.orderChannel = orderChannel == null ? null : orderChannel.trim();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Byte getIsNeedCharge() {
        return isNeedCharge;
    }

    public void setIsNeedCharge(Byte isNeedCharge) {
        this.isNeedCharge = isNeedCharge;
    }

    public Byte getOperSource() {
        return operSource;
    }

    public void setOperSource(Byte operSource) {
        this.operSource = operSource;
    }

    public Byte getAllowAutoPay() {
        return allowAutoPay;
    }

    public void setAllowAutoPay(Byte allowAutoPay) {
        this.allowAutoPay = allowAutoPay;
    }

    public Byte getWoOrder() {
        return woOrder;
    }

    public void setWoOrder(Byte woOrder) {
        this.woOrder = woOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getRefundValidTime() {
        return refundValidTime;
    }

    public void setRefundValidTime(Date refundValidTime) {
        this.refundValidTime = refundValidTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
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

    public Date getCopyTime() {
        return copyTime;
    }

    public void setCopyTime(Date copyTime) {
        this.copyTime = copyTime;
    }

    public Byte getCopyType() {
        return copyType;
    }

    public void setCopyType(Byte copyType) {
        this.copyType = copyType;
    }

    public String getCopyRemark() {
        return copyRemark;
    }

    public void setCopyRemark(String copyRemark) {
        this.copyRemark = copyRemark == null ? null : copyRemark.trim();
    }

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
    
}