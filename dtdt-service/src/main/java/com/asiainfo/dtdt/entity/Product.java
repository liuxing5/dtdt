package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Product implements Serializable{
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	
	private static final long serialVersionUID = 1L;

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

	public String getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public Integer getPrice() {
		return price;
	}

	public Byte getState() {
		return state;
	}

	public Byte getCycleType() {
		return cycleType;
	}

	public Byte getType() {
		return type;
	}

	public Byte getCanUnsubscribe() {
		return canUnsubscribe;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public void setCycleType(Byte cycleType) {
		this.cycleType = cycleType;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public void setCanUnsubscribe(Byte canUnsubscribe) {
		this.canUnsubscribe = canUnsubscribe;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}