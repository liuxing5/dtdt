package com.asiainfo.dtdt.config.mybatis.plugin;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;

import com.github.pagehelper.PageHelper;

/**
 * 
 * Description: 
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 */
public class ConfigUtils {

    public static void getPlugins(SqlSessionFactoryBean sqlSessionFactoryBean) {
        Interceptor pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("dialect", "mysql");
        properties.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PerformanceInterceptor(), pageHelper});
    }


}
