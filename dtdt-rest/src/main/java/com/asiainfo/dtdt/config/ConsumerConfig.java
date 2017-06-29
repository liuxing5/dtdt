package com.asiainfo.dtdt.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.asiainfo.dtdt.api.IAuthoService;

/**
 * 
 * Description: dubbo consumer 注册
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 */
@Configuration
public class ConsumerConfig {


    private void buildcommon(@Qualifier("consumerRegistry") RegistryConfig registryConfig, @SuppressWarnings("rawtypes") ReferenceBean referenceBean) {
    	referenceBean.setRegistry(registryConfig);
        referenceBean.setTimeout(20000);
        referenceBean.setRetries(0);
        referenceBean.setFilter("simpleconsumerfilter");
        referenceBean.setCheck(false);
        referenceBean.setGroup("elf");
    }

    @Bean
    public ReferenceBean<IAuthoService> configClientSVReferenceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IAuthoService> referenceBean = new ReferenceBean<IAuthoService>();
        referenceBean.setInterface(IAuthoService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }

}

