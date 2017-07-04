package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class HisOrder implements Serializable {

	private static final long serialVersionUID = 1L;

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

	private String remark;

	private Date copyTime2;

	private Byte copyType;

	private String copyRemark;

	private String redirectUrl;

}