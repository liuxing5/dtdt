package com.asiainfo.dtdt.task.config.transaction;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

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
public class TransactionManager {


    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("directionalDt") DataSource directionalDt) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(directionalDt);
        return dataSourceTransactionManager;
    }
}
