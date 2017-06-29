package com.asiainfo.dtdt.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Description: 
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 * @author 
 */
@Getter
@Setter
public class AopLog implements Serializable {

    private static final long serialVersionUID = -6181433271428846308L;
    private String request_time;
    private String method;
    private String application;
    private String request;
    private String response;
    private String time_wait;

    @Override
    public String toString() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        this.request_time = sdf.format(date);
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
