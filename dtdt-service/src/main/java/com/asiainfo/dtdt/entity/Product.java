package com.asiainfo.dtdt.entity;

import java.util.Date;

public class Product {
    private Long id;

    private String productCode;

    private String productName;

    private Integer price;

    private Byte state;

    private Byte cycleType;

    private Byte type;

    private Byte canUnsubscribe;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Byte getCycleType() {
        return cycleType;
    }

    public void setCycleType(Byte cycleType) {
        this.cycleType = cycleType;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getCanUnsubscribe() {
        return canUnsubscribe;
    }

    public void setCanUnsubscribe(Byte canUnsubscribe) {
        this.canUnsubscribe = canUnsubscribe;
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
}