package com.gupaoedu.vip.mall.goods.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.cart.model.Cart;
import com.gupaoedu.vip.mall.goods.model.Sku;
import com.gupaoedu.vip.mall.goods.service.SKuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*****
 * @Author: 波波
 * @Description: 云商城
 ****/
@RestController
@RequestMapping(value = "/sku")
public class SkuController {

    @Autowired
    private SKuService sKuService;


    /***
     * 库存递减
     */
    @PostMapping(value = "/dcount")
    public RespResult dcount(@RequestBody List<Cart> carts){
        sKuService.dcount(carts);
        return RespResult.ok();
    }

    /***
     * 根据ID查询商品详情
     * @return
     */
    @GetMapping(value = "/{id}")
    public RespResult<Sku> one(@PathVariable(value = "id")String id){
        Sku sku = sKuService.getById(id);
        return RespResult.ok(sku);
    }

    /****
     * 根据推广分类查询推广产品列表
     *
     */
    @GetMapping(value = "/aditems/type")
    public List<Sku> typeItems(@RequestParam(value = "id")Integer id){
        //查询
        List<Sku> skus = sKuService.typeSkuItems(id);
        return skus;
    }

    /****
     * 根据推广分类查询推广产品列表
     */
    @DeleteMapping(value = "/aditems/type")
    public RespResult delTypeItems(@RequestParam(value = "id")Integer id){
        sKuService.delTypeSkuItems(id);
        return RespResult.ok();
    }

    /****
     * 根据推广分类查询推广产品列表
     *
     */
    @PutMapping(value = "/aditems/type")
    public RespResult updateTypeItems(@RequestParam(value = "id")Integer id){
        //修改
        sKuService.updateTypeSkuItems(id);
        return RespResult.ok();
    }

}
