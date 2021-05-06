package com.gupaoedu.vip.mall.permission.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gupaoedu.vip.mall.permission.model.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService extends IService<Permission> {

    /***
     * 根据不同匹配方式查询权限列表
     */
    List<Permission> findByMatch(Integer matchMethod);

    /****
     * 查询所有角色的权限映射关系
     */
    List<Map<Integer,Integer>> allRolePermissions();

}
