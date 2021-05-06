package com.gupaoedu.vip.mall.user.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.user.model.Address;
import com.gupaoedu.vip.mall.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/address")
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;

    /****
     * 用户收件地址列表
     */
    @GetMapping(value = "/list")
    public RespResult<List<Address>> list(){
        String userName = "gp";
        List<Address> list = addressService.list(userName);
        return RespResult.ok(list);
    }
}
