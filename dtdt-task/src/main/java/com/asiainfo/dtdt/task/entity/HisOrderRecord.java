package com.asiainfo.dtdt.task.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class HisOrderRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	private String orderId;

	private String parentOrderId;

	private String partnerCode;

	private String appKey;

	private String partnerOrderId;

	private Byte cycleType;

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
	
	private String woOrderId;

}