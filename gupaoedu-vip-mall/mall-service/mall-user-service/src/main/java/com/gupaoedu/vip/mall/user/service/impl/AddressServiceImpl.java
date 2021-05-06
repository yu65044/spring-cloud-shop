package com.gupaoedu.vip.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gupaoedu.vip.mall.user.mapper.AddressMapper;
import com.gupaoedu.vip.mall.user.model.Address;
import com.gupaoedu.vip.mall.user.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper,Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    /****
     * 查询用户收件地址列表
     */
    @Override
    public List<Address> list(String userName) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<Address>();
        queryWrapper.eq("username",userName);
        return addressMapper.selectList(queryWrapper);
    }
}
