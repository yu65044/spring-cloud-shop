package com.gupaoedu.vip.mall.dw.feign;

import com.gupaoedu.mall.util.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(value = "mall-dw")
public interface HotGoodsFeign {

    /***
     * 热门商品分析
     */
    @PostMapping(value = "/hot/goods/search/{size}/{hour}/{max}")
    RespResult<List<Map<String,String>>> searchHotGoods(@PathVariable("size")Integer size,
                                                               @PathVariable("hour")Integer hour,
                                                               @PathVariable("max")Integer max,
                                                               @RequestBody String[] urls);
}
