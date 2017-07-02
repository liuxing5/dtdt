package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

    private String productCode;
    
    private String woProductCode;

    private String productName;

    private Integer price;
    
    private Integer woProductPrice;

    private Byte state;

    private Byte cycleType;

    private Byte type;

    private Byte canUnsubscribe;

    private Date createTime;

    private Date updateTime;

}