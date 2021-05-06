package com.gupaoedu.vip.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*****
 * @Author:
 * @Description:
 ****/
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.gupaoedu.vip.mall.goods.feign","com.gupaoedu.vip.mall.seckill.feign"})
public class MallWebPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallWebPageApplication.class,args);
    }
}
