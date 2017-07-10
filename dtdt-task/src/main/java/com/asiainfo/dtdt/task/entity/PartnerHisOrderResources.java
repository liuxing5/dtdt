
package com.asiainfo.dtdt.task.entity;

import java.util.Date;

import lombok.Data;
@Data
public class PartnerHisOrderResources {
    /**
     * 资源批次ID
     */
    private String batchId;

    /**
     * 合作方编码
     */
    private String partnerCode;

    /**
     * 预存订购次数
     */
    private Long preCount;

    /**
     * 失效后，使用了的次数
     */
    private Long useCount;
    
    /**
     * 告警阈值
     */
    private Long warnThreshold;

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