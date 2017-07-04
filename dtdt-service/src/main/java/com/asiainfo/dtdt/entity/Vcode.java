package com.asiainfo.dtdt.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Vcode implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String orderId;

	private String vcodeSendTime;

	private String lvcode;

	private String userInputVcode;

	private String userInputTime;

	private String vcodeValidResut;

}