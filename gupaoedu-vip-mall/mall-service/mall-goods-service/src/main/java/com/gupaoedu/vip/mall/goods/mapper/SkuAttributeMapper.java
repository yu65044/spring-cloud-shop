package com.gupaoedu.vip.mall.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gupaoedu.vip.mall.goods.model.SkuAttribute;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/*****
 * @Author: 波波
 * @Description: 云商城
 ****/
public interface SkuAttributeMapper extends BaseMapper<SkuAttribute> {

    /****
     * 1、根据分类ID查询属性ID集合
     * 2、根据属性ID集合查询属性集合
     */
    @Select("select * from sku_attribute where id IN(SELECT attr_id FROM category_attr WHERE category_id=#{id})")
    List<SkuAttribute> queryByCategoryId(Integer id);

}
