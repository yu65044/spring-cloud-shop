package com.gupaoedu.vip.mall.seckill.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.seckill.model.SeckillGoods;
import com.gupaoedu.vip.mall.seckill.model.SeckillOrder;
import com.gupaoedu.vip.mall.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/seckill/order")
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    /***
     * 抢单（非热门抢单）
     * @param username
     * @param id
     * @param num
     * @return
     */
    @PostMapping
    public RespResult add(String username,String id,Integer num){
        return RespResult.ok("抢单成功");
    }

}
