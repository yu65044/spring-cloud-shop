package com.gupaoedu.vip.mall.user.controller;

import com.gupaoedu.mall.util.JwtToken;
import com.gupaoedu.mall.util.MD5;
import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.user.model.UserInfo;
import com.gupaoedu.vip.mall.user.service.UserInfoService;
import com.gupaoedu.vip.mall.util.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user/info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /****
     * 登录  http://localhost:8088/user/info/login
     * @param username
     * @param pwd
     * @return
     */
    @PostMapping("/login")
    public RespResult<String> login(@RequestParam(value = "username")String username,
                                    @RequestParam(value = "pwd")String pwd,
                                    HttpServletRequest request) throws Exception{
        //查询用户
        UserInfo userinfo = userInfoService.getById(username);
        //匹配
        if(userinfo!=null && pwd.equals(userinfo.getPassword())){
            //封装令牌
            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("username",userinfo.getUsername());
            dataMap.put("name",userinfo.getName());
            dataMap.put("roles",userinfo.getRoles());
            //获取IP
            String ip = IPUtils.getIpAddr(request);
            dataMap.put("ip", MD5.md5(ip));
            //'创建令牌
            String token = JwtToken.createToken(dataMap);
            return RespResult.ok(token);
        }
        return RespResult.error("账号或者密码不对");
    }

}
