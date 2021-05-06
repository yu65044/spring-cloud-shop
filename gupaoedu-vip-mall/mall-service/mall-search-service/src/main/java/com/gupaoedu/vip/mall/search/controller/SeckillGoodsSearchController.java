package com.gupaoedu.vip.mall.search.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.search.model.SeckillGoodsEs;
import com.gupaoedu.vip.mall.search.service.SeckillGoodsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/seckill/goods")
public class SeckillGoodsSearchController {

    @Autowired
    private SeckillGoodsSearchService seckillGoodsSearchService;

    /***
     * 将秒杀商品导入索引库
     * http://localhost:8084/seckill/goods/import
     */
    @PostMapping(value = "/import")
    public RespResult add(@RequestBody SeckillGoodsEs seckillGoodsEs){
        seckillGoodsSearchService.add(seckillGoodsEs);
        return RespResult.ok();
    }
}
