package com.asiainfo.dtdt.task.entity;

import java.util.Date;

import lombok.Data;

@Data
public class PartnerSmsConfig {
    /**
     * 
     */
    private String id;

    /**
     * 合作方编码
     */
    private String partnerCode;
    
    /**
     * 合作方名称
     */
    private String partnerName;

    /**
     * 预存订购次数
     */
    private String contacts;

    /**
     * 手机号码
     */
    private String mobilephone;

    /**
     * 告警阈值
     */
    private Long warnThreshold;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

}