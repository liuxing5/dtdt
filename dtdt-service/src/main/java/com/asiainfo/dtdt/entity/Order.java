package com.asiainfo.dtdt.entity;

import java.util.Date;

public class Order {
    private String orderId;

    private String woOrderId;

    private String partnerCode;

    private String appKey;

    private String partnerOrderId;

    private String productCode;

    private Byte operType;

    private String refundOrderId;

    private Byte isRealRequestWoplat;

    private String state;

    private String mobilephone;

    private String orderChannel;

    private Date createTime;

    private Date updateTime;

    private Date validTime;

    private Date invalidTime;

    private Integer price;

    private Integer count;

    private Long money;

    private Byte isNeedCharge;

    private Byte chargeType;

    private String remark;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getWoOrderId() {
        return woOrderId;
    }

    public void setWoOrderId(String woOrderId) {
        this.woOrderId = woOrderId == null ? null : woOrderId.trim();
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public Byte getOperType() {
        return operType;
    }

    public void setOperType(Byte operType) {
        this.operType = operType;
    }

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId == null ? null : refundOrderId.trim();
    }

    public Byte getIsRealRequestWoplat() {
        return isRealRequestWoplat;
    }

    public void setIsRealRequestWoplat(Byte isRealRequestWoplat) {
        this.isRealRequestWoplat = isRealRequestWoplat;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Byte getChargeType() {
        return chargeType;
    }

    public void setChargeType(Byte chargeType) {
        this.chargeType = chargeType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}