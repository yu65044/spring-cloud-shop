package com.gupaoedu.vip.mall.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gupaoedu.vip.mall.user.mapper.UserInfoMapper;
import com.gupaoedu.vip.mall.user.model.UserInfo;
import com.gupaoedu.vip.mall.user.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
