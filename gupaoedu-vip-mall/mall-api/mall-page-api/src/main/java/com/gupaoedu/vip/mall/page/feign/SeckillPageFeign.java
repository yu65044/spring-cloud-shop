package com.gupaoedu.vip.mall.page.feign;

import com.gupaoedu.mall.util.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "mall-web-page")
public interface SeckillPageFeign {

    /***
     * 删除指定活动的页面
     */
    @DeleteMapping(value = "/page/seckill/goods/{acid}")
    RespResult deleByAct(@PathVariable("acid")String acid);

    /***
     * 生成秒杀商品详情页
     */
    @GetMapping(value = "/page/seckill/goods/{id}")
    RespResult page(@PathVariable("id") String id) throws Exception;
}
