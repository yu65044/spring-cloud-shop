package com.gupaoedu.vip.mall.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/*****
 * @Author: 波波
 * @Description: 云商城
 * http://localhost:8082/file/download/news1.jpg
 ****/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MallFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallFileApplication.class,args);
    }
}
