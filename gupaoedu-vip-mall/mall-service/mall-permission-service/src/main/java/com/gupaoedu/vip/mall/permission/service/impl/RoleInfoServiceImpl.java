package com.gupaoedu.vip.mall.permission.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gupaoedu.vip.mall.permission.mapper.RoleInfoMapper;
import com.gupaoedu.vip.mall.permission.service.RoleInfoService;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleInfo;

@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements RoleInfoService {
}
