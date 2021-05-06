package com.gupaoedu.vip.mall.page.service.impl;

import com.alibaba.fastjson.JSON;
import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.goods.feign.CategoryFeign;
import com.gupaoedu.vip.mall.goods.feign.SpuFeign;
import com.gupaoedu.vip.mall.goods.model.Category;
import com.gupaoedu.vip.mall.goods.model.Product;
import com.gupaoedu.vip.mall.goods.model.Sku;
import com.gupaoedu.vip.mall.goods.model.Spu;
import com.gupaoedu.vip.mall.page.service.PageService;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@Service
public  class PageServiceImpl implements PageService {

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Value("${pagepath}")
    private String pagepath;

    @Autowired
    private TemplateEngine templateEngine;

    /****
     * 生成静态页
     * @param spuid
     * @throws Exception
     */
    @Override
    public void html(String spuid) throws Exception {
        //1、创建容器对象(上下文对象)
        Context context = new Context();
        //2、设置模板数据
        context.setVariables(loadData(spuid));
        //3、指定文件生成后存储路径
        File file = new File(pagepath,spuid+".html");
        PrintWriter writer = new PrintWriter(file,"UTF-8");
        //4、执行合成生成
        templateEngine.process("item",context,writer);
    }

    /****
     * 数据加载
     */
    public Map<String,Object> loadData(String spuid){
        //查询数据
        RespResult<Product> productResult = spuFeign.one(spuid);
        Product product = productResult.getData();
        if(product!=null){
            //Map
            Map<String,Object> resultMap = new HashMap<String,Object>();
            //Spu
            Spu spu = product.getSpu();
            resultMap.put("spu",spu);
            //图片处理
            resultMap.put("images",spu.getImages().split(","));
            //属性列表
            resultMap.put("attrs",JSON.parseObject(spu.getAttributeList(),Map.class));

            //三级分类
            RespResult<Category> one = categoryFeign.one(spu.getCategoryOneId());
            RespResult<Category> two = categoryFeign.one(spu.getCategoryTwoId());
            RespResult<Category> three = categoryFeign.one(spu.getCategoryThreeId());
            resultMap.put("one",one.getData());
            resultMap.put("two",two.getData());
            resultMap.put("three",three.getData());

            //Sku集合
            List<Map<String,Object>> skuList = new ArrayList<Map<String,Object>>();
            for (Sku sku : product.getSkus()) {
                Map<String,Object> skuMap = new HashMap<String,Object>();
                skuMap.put("id",sku.getId());
                skuMap.put("name",sku.getName());
                skuMap.put("price",sku.getPrice());
                skuMap.put("attr",sku.getSkuAttribute());
                //将skuMap添加到skuList中
                skuList.add(skuMap);
            }
            resultMap.put("skuList",skuList);

            return resultMap;
        }
        return null;
    }
}
