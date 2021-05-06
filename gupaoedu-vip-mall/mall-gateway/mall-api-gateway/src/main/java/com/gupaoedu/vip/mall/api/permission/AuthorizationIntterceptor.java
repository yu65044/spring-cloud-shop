package com.gupaoedu.vip.mall.api.permission;

import com.gupaoedu.mall.util.JwtToken;
import com.gupaoedu.mall.util.MD5;
import com.gupaoedu.vip.mall.api.util.IpUtil;
import com.gupaoedu.vip.mall.permission.model.Permission;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class AuthorizationIntterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /****
     * 判断uri是否为有效路径
     * @param uri
     * @return
     */
    public Boolean isInvalid(String uri){
        RBloomFilter<String> uriBloomFilterArray = redissonClient.getBloomFilter("UriBloomFilterArray");
        return uriBloomFilterArray.contains(uri);
    }

    /***
     * 权限校验
     */
    public Boolean rolePermission(ServerWebExchange exchange,Map<String, Object> token){
        ServerHttpRequest request = exchange.getRequest();
        //获取uri  /cart/list
        String uri = request.getURI().getPath();
        //提交方式  GET/POST/*
        String method = request.getMethodValue();
        //服务名字
        URI routerUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String servicename = routerUri.getHost();

        //匹配->获取角色集合
        String[] roles = token.get("roles").toString().split(",");

        Permission permission = null;
        //循环判断每个角色是否有权限
        for (String role : roles) {
            //获取完全匹配权限集合
            Set<Permission> permissions = (Set<Permission>) redisTemplate.boundHashOps("RolePermissionMap").get("Role_0_" + role);

            if(permissions==null){
                continue;
            }
            //循环判断
            permission = match0(new ArrayList<Permission>(permissions), uri, method, servicename);
            if(permission!=null){
                break;
            }
        }

        //permission==null，通配符方式匹配
        if(permission==null){
            //通配符匹配
        }
        return permission!=null;
    }

    /***
     * 令牌校验
     */
    public Map<String,Object> tokenIntercept(ServerWebExchange exchange){
        ServerHttpRequest request = exchange.getRequest();
        //校验其他地址
        String clientIp = IpUtil.getIp(request);
        //获取令牌
        String token = request.getHeaders().getFirst("authorization");
        //令牌校验
        Map<String, Object> resultMap = AuthorizationIntterceptor.jwtVerify(token, clientIp);
        return resultMap;
    }

    /***
     * 是否需要拦截校验
     * true:需要拦截
     * false:不需要拦截
     */
    public Boolean isIntercept(ServerWebExchange exchange){
        ServerHttpRequest request = exchange.getRequest();
        //获取uri  /cart/list
        String uri = request.getURI().getPath();
        //提交方式  GET/POST/*
        String method = request.getMethodValue();
        //服务名字
        URI routerUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String servicename = routerUri.getHost();

        //从Redis缓存中进行匹配
        // 0:完全匹配
        List<Permission> permissionsMatch0 = (List<Permission>) redisTemplate.boundHashOps("RolePermissionAll").get("PermissionMatch0");
        if(permissionsMatch0!=null){

        }
        // 1:通配符匹配
        Permission permission = match0(permissionsMatch0, uri, method, servicename);
        //如果permission==null，则执行通配符匹配
        if(permission==null){
            //通配符匹配

            //如果通配符匹配也为空，表明当前请求不需要进行权限校验
            return false;
        }
        return true;
    }


    /***
     * 匹配方法:完全匹配
     */
    public Permission match0(List<Permission> permissionsMatch0,String uri,String method,String serviceName){
        for (Permission permission : permissionsMatch0) {
            String matchUrl = permission.getUrl();
            String matchMethod = permission.getMethod();
            if(matchUrl.equals(uri)){
                //提交方式和服务匹配
                if(matchMethod.equals(method) && serviceName.equals(permission.getServiceName())){
                    return permission;
                }
                if("*".equals(matchMethod) && serviceName.equals(permission.getServiceName())){
                    return permission;
                }
            }
        }
        return null;
    }

    /*****
     * 令牌解析
     */
    public static Map<String,Object> jwtVerify(String token,String clientIp){
        try {
            //解析Token
            Map<String, Object> dataMap = JwtToken.parseToken(token);
            //获取Token中IP的MD5
            String ip = dataMap.get("ip").toString();
            //比较Token中IP的MD5值和用户的IPMD5值
            if(ip.equals(MD5.md5(clientIp))){
                return dataMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
