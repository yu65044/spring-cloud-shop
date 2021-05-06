package com.gupaoedu.vip.mall.page.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.page.service.SeckillPageService;
import com.gupaoedu.vip.mall.seckill.feign.SeckillGoodsFeign;
import com.gupaoedu.vip.mall.seckill.model.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/page")
public class SeckillPageController {

    @Autowired
    private SeckillPageService seckillPageService;

    @Autowired
    private SeckillGoodsFeign seckillGoodsFeign;

    /***
     * 删除指定活动的页面
     */
    @DeleteMapping(value = "/seckill/goods/{acid}")
    public RespResult deleByAct(@PathVariable("acid")String acid){
        //1.查询当前活动ID对应的商品列表数据\
        RespResult<List<SeckillGoods>> listRespResult = seckillGoodsFeign.actGoods(acid);
        List<SeckillGoods> goodsList = listRespResult.getData();
        //2.根据列表数据删除页面
        if(goodsList!=null){
            //循环删除页面
            for (SeckillGoods seckillGoods : goodsList) {
                seckillPageService.delete(seckillGoods.getId());
            }
        }
        return RespResult.ok();
    }

    /***
     * 生成秒杀商品详情页
     */
    @GetMapping(value = "/seckill/goods/{id}")
    public RespResult page(@PathVariable("id") String id) throws Exception {
        //生成秒杀商品详情页
        seckillPageService.html(id);
        return RespResult.ok();
    }
}
