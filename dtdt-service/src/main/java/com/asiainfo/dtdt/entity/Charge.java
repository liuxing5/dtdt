package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


@Data
public class Charge implements Serializable {

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

}