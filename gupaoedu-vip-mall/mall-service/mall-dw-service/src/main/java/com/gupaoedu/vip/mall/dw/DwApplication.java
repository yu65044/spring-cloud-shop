package com.gupaoedu.vip.mall.dw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.gupaoedu.vip.mall.dw.mapper")
public class DwApplication {

    public static void main(String[] args) {
        SpringApplication.run(DwApplication.class,args);
    }
}
