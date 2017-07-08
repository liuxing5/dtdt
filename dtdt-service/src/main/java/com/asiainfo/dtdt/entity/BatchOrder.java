
package com.asiainfo.dtdt.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
@Data
public class BatchOrder implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String STATE_INIT = "0";
	public static final String STATE_END = "1";
	
    /**
     * 批量订单ID
     */
    private String batchId;

    /**
     * 合作方编码
     */
    private String partnerCode;

    /**
     * 合作伙伴产品
     */
    private String appKey;

    /**
     * 合作伙伴订单ID
     */
    private String partnerOrderId;

    /**
     * 
     */
    private String productCode;

    /**
     * 订购：1，退订：2
     */
    private Byte operType;

    /**
     * 
     */
    private String state;

    /**
     * 订购时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 订购产品价格
     */
    private Integer price;

    /**
     * 支付成功跳转Url
     */
    private String redirectUrl;

    /**
     * 批量开的手机号码
     */
    private String mobilephones;

    /**
     * 手机号码个数
     */
    private Integer mobilephonesCount;

    /**
     * 
     */
    private String remark;

}