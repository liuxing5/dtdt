package com.asiainfo.dtdt.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.asiainfo.dtdt.api.IAuthoService;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.order.IQueryOrderService;

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
    
    @Bean
    public ReferenceBean<IProductService> configIProductServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IProductService> referenceBean = new ReferenceBean<IProductService>();
        referenceBean.setInterface(IProductService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }
    
    @Bean
    public ReferenceBean<IOrderService> configIOrderServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IOrderService> referenceBean = new ReferenceBean<IOrderService>();
        referenceBean.setInterface(IOrderService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }

    @Bean
    public ReferenceBean<IQueryOrderService> configIQueryOrderServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IQueryOrderService> referenceBean = new ReferenceBean<IQueryOrderService>();
        referenceBean.setInterface(IQueryOrderService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }
    
}

