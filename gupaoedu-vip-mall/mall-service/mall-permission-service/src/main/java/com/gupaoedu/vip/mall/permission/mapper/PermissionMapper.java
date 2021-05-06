package com.gupaoedu.vip.mall.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gupaoedu.vip.mall.permission.model.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PermissionMapper extends BaseMapper<Permission> {

    /***
     * 查询所有角色的权限映射关系
     */
    @Select("SELECT * FROM role_permission")
    List<Map<Integer,Integer>> allRolePermissions();
}
