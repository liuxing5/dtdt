package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class App implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long appId;

	private String appKey;

	private String secret;

	private Long partnerId;

	private String appName;

	private Byte state;

	private Byte isNeedCharge;

	private Date createTime;

	private String noticeUrl;

}