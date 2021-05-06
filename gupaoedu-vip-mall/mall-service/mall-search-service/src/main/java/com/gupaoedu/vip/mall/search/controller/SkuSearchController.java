package com.gupaoedu.vip.mall.search.controller;

import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.search.model.SkuEs;
import com.gupaoedu.vip.mall.search.service.SkuSearchService;
import org.elasticsearch.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@RequestMapping(value = "/search")
public class SkuSearchController {

    @Autowired
    private SkuSearchService skuSearchService;


    /***
     * 商品搜索
     */
    @GetMapping
    public RespResult<Map<String,Object>> search(@RequestParam(required = false)Map<String,Object> searchMap){
        Map<String, Object> resultMap = skuSearchService.search(searchMap);
        return RespResult.ok(resultMap);
    }

    /*****
     * 增加索引
     */
    @PostMapping(value = "/add")
    public RespResult add(@RequestBody SkuEs skuEs){
        skuSearchService.add(skuEs);
        return RespResult.ok();
    }

    /***
     * 删除索引
     */
    @DeleteMapping(value = "/del/{id}")
    public RespResult del(@PathVariable(value = "id")String id){
        skuSearchService.del(id);
        return RespResult.ok();
    }
}
