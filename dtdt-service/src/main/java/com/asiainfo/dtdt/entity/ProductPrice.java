package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
@Data
public class ProductPrice implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long productId;

    private Long partnerId;

    private Long appId;

    private Integer partnerPrice;

    private Integer appPrice;

    private Byte state;

    private Date createTime;

    private Date updateTime;

    private String areaCode;

    private String areaName;

}