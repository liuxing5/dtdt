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

}