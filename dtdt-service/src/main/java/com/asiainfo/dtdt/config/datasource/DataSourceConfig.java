package com.asiainfo.dtdt.config.datasource;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 
 * Description: 注入数据源
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 * @author 
 */
@ConfigurationProperties(prefix = "datasource")
@Data
@Configuration
public class DataSourceConfig {

    private HikariConfig directionalDt; 


    @Primary
    @RefreshScope
    @Bean
    public HikariDataSource directionalDt() {
        return new HikariDataSource(directionalDt);
    }


}
