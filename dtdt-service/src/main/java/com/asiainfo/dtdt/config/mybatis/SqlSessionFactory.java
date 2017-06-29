package com.asiainfo.dtdt.config.mybatis;

import javax.sql.DataSource;

import lombok.Data;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.asiainfo.dtdt.config.mybatis.plugin.ConfigUtils;

/**
 * 
 * Description: 
 * Date: 2017年6月28日 
 * Copyright (c) 2017 AI
 * 
 * @author 
 */

@Data
@Configuration
@ConditionalOnClass(DataSource.class)
public class SqlSessionFactory {


    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("directionalDt") DataSource directionalDt) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(directionalDt);
        ConfigUtils.getPlugins(sqlSessionFactoryBean);
        return sqlSessionFactoryBean;
    }


}
