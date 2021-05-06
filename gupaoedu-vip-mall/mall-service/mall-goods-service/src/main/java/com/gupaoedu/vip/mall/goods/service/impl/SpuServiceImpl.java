package com.gupaoedu.vip.mall.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gupaoedu.vip.mall.goods.mapper.BrandMapper;
import com.gupaoedu.vip.mall.goods.mapper.CategoryMapper;
import com.gupaoedu.vip.mall.goods.mapper.SkuMapper;
import com.gupaoedu.vip.mall.goods.mapper.SpuMapper;
import com.gupaoedu.vip.mall.goods.model.*;
import com.gupaoedu.vip.mall.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*****
 * @Author: 波波
 * @Description: 云商城
 ****/
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper,Spu> implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    /***
     * 产品保存
     * @param product
     */
    @Override
    public void saveProduct(Product product) {
        //1.保存Spu
        Spu spu = product.getSpu();

        if(StringUtils.isEmpty(spu.getId())){
            spu.setIsMarketable(1); //已上架
            spu.setIsDelete(0); //未删除
            spu.setStatus(1);   //审核已通过
            spuMapper.insert(spu);
        }else{
            //修改
            spuMapper.updateById(spu);
            //删除Sku集合
            skuMapper.delete(new QueryWrapper<Sku>().eq("spu_id",spu.getId()));
        }

        //2.保存List<Sku>
        Date date = new Date();
        Category category = categoryMapper.selectById(spu.getCategoryThreeId());
        Brand brand = brandMapper.selectById(spu.getBrandId());
        for (Sku sku : product.getSkus()) {
            //SKU名称
            // {"适合人群":"有一定java基础的人","书籍分类":"科技"}
            String name = spu.getName();
            Map<String,String> skuattrMap = JSON.parseObject(sku.getSkuAttribute(),Map.class);
            for (Map.Entry<String, String> entry : skuattrMap.entrySet()) {
                name+="  "+entry.getValue();
            }
            sku.setName(name);
            //创建时间
            sku.setCreateTime(date);
            //修改时间
            sku.setUpdateTime(date);
            //分类ID
            sku.setCategoryId(spu.getCategoryThreeId());
            //分类名字
            sku.setBrandName(brand.getName());
            //品牌ID
            sku.setBrandId(spu.getBrandId());
            //品牌名字
            sku.setCategoryName(category.getName());
            //spuid
            sku.setSpuId(spu.getId());
            //状态 商品状态 1-正常，2-下架，3-删除
            sku.setStatus(1);

            //添加
            skuMapper.insert(sku);
        }
    }

    /****
     * 根据spuid查询Product
     * @param id
     * @return
     */
    @Override
    public Product findBySupId(String id) {
        //查询Spu
        Spu spu = spuMapper.selectById(id);
        //查询Sku集合
        QueryWrapper<Sku> queryWrapper = new QueryWrapper<Sku>();
        queryWrapper.eq("spu_id",id);
        List<Sku> skus = skuMapper.selectList(queryWrapper);

        //Product
        Product product = new Product();
        product.setSpu(spu);
        product.setSkus(skus);
        return product;
    }
}
