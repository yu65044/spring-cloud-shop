package com.gupaoedu.vip.mall.seckill.feign;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.seckill.model.SeckillGoods;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "mall-seckill")
public interface SeckillGoodsFeign {


    /***
     * 根据活动查询秒杀商品集合
     * @param acid
     * @return
     */
    @GetMapping(value = "/seckill/goods/act/{acid}")
    RespResult<List<SeckillGoods>> actGoods(@PathVariable("acid") String acid);

    /***
     * 根据ID查询秒杀商品详情
     * @param id
     * @return
     */
    @GetMapping(value = "/seckill/goods/{id}")
    RespResult<SeckillGoods> one(@PathVariable("id") String id);
}
