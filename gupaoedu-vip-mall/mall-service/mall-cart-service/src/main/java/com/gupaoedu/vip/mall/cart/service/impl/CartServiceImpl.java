package com.gupaoedu.vip.mall.cart.service.impl;

import com.google.common.collect.Lists;
import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.cart.mapper.CartMapper;
import com.gupaoedu.vip.mall.cart.model.Cart;
import com.gupaoedu.vip.mall.cart.service.CartService;
import com.gupaoedu.vip.mall.goods.feign.SkuFeign;
import com.gupaoedu.vip.mall.goods.model.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private MongoTemplate mongoTemplate;


    /***
     * 根据集合ID删除指定的购物车列表
     */
    @Override
    public void delete(List<String> ids) {
        // _id in(ids)
        mongoTemplate.remove(Query.query(Criteria.where("_id").in(ids)),Cart.class);
    }

    /***
     * 查询指定购物车ID集合的列表
     */
    @Override
    public List<Cart> list(List<String> ids) {
        if(ids!=null && ids.size()>0){
            //根据ID集合查询
            Iterable<Cart> carts = cartMapper.findAllById(ids);
            return Lists.newArrayList(carts);
        }
        return null;
    }

    /***
     * 购物车列表
     */
    @Override
    public List<Cart> list(String userName) {
        //条件构建
        Cart cart = new Cart();
        cart.setUserName(userName);
        return cartMapper.findAll(Example.of(cart), Sort.by("_id"));
    }

    /***
     * 加入购物车
     * @param id
     * @param userName
     * @param num:当前商品加入购物车总数量
     * @return
     */
    @Override
    public void add(String id, String userName, Integer num) {
        //ID 不能冲突
        //1)删除当前ID对应的商品之前的购物车记录
        cartMapper.deleteById(userName+id);

        if(num>0){
            //2）根据ID查询Sku详情
            RespResult<Sku> skuResp = skuFeign.one(id);

            //3)将当前ID商品对应的数据加入购物车（存入到MongoDB）
            Sku sku= skuResp.getData();
            Cart cart = new Cart(userName+id,userName,sku.getName(),sku.getPrice(),sku.getImage(),id,num);
            cartMapper.save(cart);
        }
    }
}
