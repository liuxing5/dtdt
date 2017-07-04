package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Partner implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long partnerId;

	private String partnerCode;

	private String partnerName;

	private Byte state;

	private String contacts;

	private String telephone;

	private String mobilephone;

	private String address;

	private Date createTime;

	private Date updateTime;

}