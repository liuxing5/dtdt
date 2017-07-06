package com.asiainfo.dtdt.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;


@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class ResponseData<T> implements Serializable {

    private T data;
    private String code;
    private String desc;

    public ResponseData(T t) {
        this.data = t;
        this.code = "00000";
        this.desc = "成功";
    }
    
    public ResponseData(String code, String desc)
    {
    	this.code = code;
        this.desc = desc;
    }
    
    public ResponseData(String code, String desc, T data)
    {
    	this.code = code;
        this.desc = desc;
        this.data = data;
    }

}
