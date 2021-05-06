package com.gupaoedu.vip.mall.permission.init;

import com.gupaoedu.vip.mall.permission.model.Permission;
import com.gupaoedu.vip.mall.permission.service.PermissionService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InitPermission  implements ApplicationRunner {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /***
     * 权限初始化加载
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //加载所有权限   0 ： 完全匹配过滤  1： 通配符匹配
        List<Permission> permissionMatch0 = permissionService.findByMatch(0);
        List<Permission> permissionMatch1 = permissionService.findByMatch(1);

        //所有角色的权限 查询角色权限映射关系
        List<Map<Integer, Integer>> rolePermissions = permissionService.allRolePermissions();
        //匹配每个角色拥有的权限列表
        Map<String, Set<Permission>> roleMap = rolePermissionFilter(rolePermissions, permissionMatch0, permissionMatch1);
        //数据存入到Redis缓存
        redisTemplate.boundHashOps("RolePermissionAll").put("PermissionMatch0",permissionMatch0);
        redisTemplate.boundHashOps("RolePermissionAll").put("PermissionMatch1",permissionMatch1);
        //角色权限
        redisTemplate.boundHashOps("RolePermissionMap").putAll(roleMap);

        //存储权限判断部分uri到布隆过滤器中-完全匹配
        RBloomFilter<String> filters = redissonClient.getBloomFilter("UriBloomFilterArray");
        filters.tryInit(1000000L,0.0001);
        for (Permission permission : permissionMatch0) {
            filters.add(permission.getUrl());
        }
    }

    /****
     * 每个角色拥有的权限
     */
    public Map<String, Set<Permission>> rolePermissionFilter(List<Map<Integer, Integer>> rolePermissions,
                                     List<Permission> permissionMatch0,
                                     List<Permission> permissionMatch1){
        //每个角色拥有哪些权限 存入到map 中
        //Match 0  Match 1
        Map<String, Set<Permission>> rolePermissionMapping = new HashMap<String,Set<Permission>>();

        //循环所有的角色关系映射
        for (Map<Integer, Integer> rolePermissionMap : rolePermissions) {
            //角色ID
            Integer rid = rolePermissionMap.get("rid");
            //权限ID
            Integer pid = rolePermissionMap.get("pid");

            //定义一个Key
            String key0="Role_0_"+rid;
            String key1="Role_1_"+rid;

            //获取当前角色的权限列表
            /*Set<Permission> permissionsSet0 = rolePermissionMapping.get(key0);
            Set<Permission> permissionsSet1 = rolePermissionMapping.get(key1);
            if(permissionsSet0==null){
                permissionsSet0 = new HashSet<Permission>();
            }
            if(permissionsSet1==null){
                permissionsSet1 = new HashSet<Permission>();
            }*/
            Set<Permission> permissionsSet0 = rolePermissionMapping.get(key0);
            Set<Permission> permissionsSet1 = rolePermissionMapping.get(key1);
            permissionsSet0=permissionsSet0==null? new HashSet<Permission>() : permissionsSet0;
            permissionsSet1=permissionsSet1==null? new HashSet<Permission>() : permissionsSet1;

            //查找每个角色对应的权限-完全匹配
            for (Permission permission : permissionMatch0) {
                if(permission.getId().intValue()==pid.intValue()){
                    //权限匹配完成
                    permissionsSet0.add(permission);
                    break;
                }
            }
            //查找每个角色对应的权限-通配符匹配
            for (Permission permission : permissionMatch1) {
                if(permission.getId().intValue()==pid.intValue()){
                    //权限匹配完成
                    permissionsSet1.add(permission);
                    break;
                }
            }

            if(permissionsSet0.size()>0){
                rolePermissionMapping.put(key0,permissionsSet0);
            }
            if(permissionsSet1.size()>0){
                rolePermissionMapping.put(key1,permissionsSet1);
            }
        }
        return rolePermissionMapping;
    }
}
