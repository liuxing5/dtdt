package com.asiainfo.dtdt.config.dubbo;

import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Description: 
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 * @author 
 */
@Configuration
@Data
@ConditionalOnClass(Exporter.class)
public class DubboAnnotation {

    /**
     * dubbo 扫描
     *
     * @return
     */
    @Bean
    public AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage("com.asiainfo.dtdt.service.impl");
        return annotationBean;
    }
}
