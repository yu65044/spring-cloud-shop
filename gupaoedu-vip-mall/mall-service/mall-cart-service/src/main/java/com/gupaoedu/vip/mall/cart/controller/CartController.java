package com.gupaoedu.vip.mall.cart.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.cart.model.Cart;
import com.gupaoedu.vip.mall.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    /***
     * 删除购物车数据
     */
    @DeleteMapping
    public RespResult delete(@RequestBody List<String> ids){
        cartService.delete(ids);
        return RespResult.ok();
    }


    /***
     * 指定ID集合的购物车数据
     * http://localhost:8087/cart/list
     */
    @PostMapping(value = "/list")
    public RespResult<List<Cart>> list(@RequestBody List<String> ids){
        List<Cart> carts = cartService.list(ids);
        return RespResult.ok(carts);
    }

    /****
     * 增加购物车方法
     */
    @GetMapping(value = "/{id}/{num}")
    public RespResult add(@PathVariable(value = "id")String id,
                          @PathVariable(value = "num")Integer num){
        String userName = "gp";
        cartService.add(id,userName,num);
        return RespResult.ok();
    }


    /****
     * 购物车列表
     */
    @GetMapping(value = "/list")
    public RespResult<List<Cart>> list(){
        String userName = "gp";
        List<Cart> list = cartService.list(userName);
        return RespResult.ok(list);
    }
}
