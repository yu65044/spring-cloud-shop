package com.gupaoedu.vip.mall.search.feign;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.search.model.SeckillGoodsEs;
import com.gupaoedu.vip.mall.search.model.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = "mall-search")
public interface SeckillGoodsSearchFeign {


    /***
     * 将秒杀商品导入索引库
     */
    @PostMapping("/seckill/goods/import")
    RespResult add(@RequestBody SeckillGoodsEs seckillGoodsEs);
}
