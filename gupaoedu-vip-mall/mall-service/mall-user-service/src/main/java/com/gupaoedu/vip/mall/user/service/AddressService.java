package com.gupaoedu.vip.mall.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gupaoedu.vip.mall.user.model.Address;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
public interface AddressService extends IService<Address>{


    /****
     * 查询用户收件地址列表
     */
    List<Address> list(String userName);
}
