package com.asiainfo.dtdt.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.asiainfo.dtdt.interfaces.IAppService;
import com.asiainfo.dtdt.interfaces.IAuthoService;
import com.asiainfo.dtdt.interfaces.IProductService;
import com.asiainfo.dtdt.interfaces.order.INoticeService;
import com.asiainfo.dtdt.interfaces.order.IOrderRecordService;
import com.asiainfo.dtdt.interfaces.order.IOrderService;
import com.asiainfo.dtdt.interfaces.order.IQueryOrderService;
import com.asiainfo.dtdt.interfaces.order.IWoplatOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayOrderService;
import com.asiainfo.dtdt.interfaces.pay.IPayService;

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
        //referenceBean.setGroup("elf");
        referenceBean.setGroup("test");
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
    public ReferenceBean<IAppService> configIAppServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IAppService> referenceBean = new ReferenceBean<IAppService>();
        referenceBean.setInterface(IAppService.class);
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
    
    @Bean
    public ReferenceBean<IOrderRecordService> configIOrderRecordServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IOrderRecordService> referenceBean = new ReferenceBean<IOrderRecordService>();
        referenceBean.setInterface(IOrderRecordService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }
    
    @Bean
    public ReferenceBean<IWoplatOrderService> configIWoplatOrderServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IWoplatOrderService> referenceBean = new ReferenceBean<IWoplatOrderService>();
        referenceBean.setInterface(IWoplatOrderService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }
    
    @Bean
    public ReferenceBean<IPayOrderService> configIPayOrderServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IPayOrderService> referenceBean = new ReferenceBean<IPayOrderService>();
        referenceBean.setInterface(IPayOrderService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }
    
    @Bean
    public ReferenceBean<IPayService> configIPayServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<IPayService> referenceBean = new ReferenceBean<IPayService>();
        referenceBean.setInterface(IPayService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }
    
    @Bean
    public ReferenceBean<INoticeService> configINoticeServiceBean(@Qualifier("consumerRegistry") RegistryConfig registryConfig) {
        ReferenceBean<INoticeService> referenceBean = new ReferenceBean<INoticeService>();
        referenceBean.setInterface(INoticeService.class);
        buildcommon(registryConfig, referenceBean);
        return referenceBean;
    }
}

