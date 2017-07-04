package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Param implements Serializable {

	private static final long serialVersionUID = 1L;

	private String paramCode;

	private String paramValue;

	private String parentCode;

	private Integer status;

	private Date createTime;

	private String remark;

}