package com.gupaoedu.vip.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*****
 * @Author:
 * @Description:
 ****/
@SpringBootApplication
@MapperScan(basePackages = {"com.gupaoedu.vip.mall.order.mapper"})
@EnableFeignClients(basePackages = {"com.gupaoedu.vip.mall.goods.feign","com.gupaoedu.vip.mall.cart.feign"})
public class MallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallOrderApplication.class,args);
    }
}
