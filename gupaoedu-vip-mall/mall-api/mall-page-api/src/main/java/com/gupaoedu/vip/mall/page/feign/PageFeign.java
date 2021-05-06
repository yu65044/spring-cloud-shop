package com.gupaoedu.vip.mall.page.feign;

import com.gupaoedu.mall.util.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = "mall-web-page")
public interface PageFeign {

    /***
     * 生成静态页
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/page/{id}")
    RespResult html(@PathVariable(value = "id")String id) throws Exception;
}
