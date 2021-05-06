package com.gupaoedu.vip.mall.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gupaoedu.vip.mall.goods.model.Category;

import java.util.List;

/*****
 * @Author: 波波
 * @Description: 云商城
 ****/
public interface CategoryService extends IService<Category> {

    /***
     * 根据分类父ID查询所有子类
     */
    List<Category> findByParentId(Integer pid);
}
