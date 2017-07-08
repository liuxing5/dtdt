package com.asiainfo.dtdt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.asiainfo.dtdt.common.util.SpringContextUtil;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
//@Import(FdfsClientConfig.class)
public class Application {
    public static void main(String[] args) {
    	 ApplicationContext app = SpringApplication.run(Application.class, args);
    	 SpringContextUtil.setApplicationContext(app);  //spring 上下文工具注入上下
        while (true) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}