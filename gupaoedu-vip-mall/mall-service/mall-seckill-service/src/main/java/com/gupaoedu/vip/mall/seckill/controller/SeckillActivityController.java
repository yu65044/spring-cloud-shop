package com.gupaoedu.vip.mall.seckill.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.seckill.model.SeckillActivity;
import com.gupaoedu.vip.mall.seckill.service.SeckillActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/activity")
public class SeckillActivityController {

    @Autowired
    private SeckillActivityService seckillActivityService;

    /****
     * 有效活动列表
     * http://localhost:8092/test/lock
     */
    @GetMapping
    public RespResult list(){
        //查询活动列表
        List<SeckillActivity> seckillActivities = seckillActivityService.validActivity();
        return RespResult.ok(seckillActivities);
    }
}
