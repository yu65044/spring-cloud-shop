package com.gupaoedu.vip.mall.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gupaoedu.vip.mall.goods.model.SkuAttribute;

import java.util.List;

/*****
 * @Author: 波波
 * @Description: 云商城
 ****/
public interface SkuAttributeService extends IService<SkuAttribute> {


    /***
     * 根据分类ID查询属性加集合
     */
    List<SkuAttribute> queryList(Integer id);
}
