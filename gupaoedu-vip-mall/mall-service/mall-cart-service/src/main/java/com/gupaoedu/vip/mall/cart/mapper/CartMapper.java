package com.gupaoedu.vip.mall.cart.mapper;

import com.gupaoedu.vip.mall.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

/*****
 * @Author:
 * @Description:
 ****/
public interface CartMapper extends MongoRepository<Cart,String> {
}
