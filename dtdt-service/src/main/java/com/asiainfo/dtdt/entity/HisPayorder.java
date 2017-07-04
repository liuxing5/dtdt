package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class HisPayorder implements Serializable {

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

	private Date copyTime;

	private String copyRemark;

}