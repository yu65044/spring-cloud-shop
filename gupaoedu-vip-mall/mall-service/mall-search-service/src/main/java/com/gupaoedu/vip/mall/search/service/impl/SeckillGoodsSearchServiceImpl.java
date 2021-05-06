package com.gupaoedu.vip.mall.search.service.impl;
import com.gupaoedu.vip.mall.search.mapper.SeckillGoodsSearchMapper;
import com.gupaoedu.vip.mall.search.model.SeckillGoodsEs;
import com.gupaoedu.vip.mall.search.service.SeckillGoodsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SeckillGoodsSearchServiceImpl implements SeckillGoodsSearchService {

    @Autowired
    private SeckillGoodsSearchMapper seckillGoodsSearchMapper;

    /***
     * 秒杀商品导入索引库
     * @param seckillGoodsEs
     */
    @Override
    public void add(SeckillGoodsEs seckillGoodsEs) {
        seckillGoodsSearchMapper.save(seckillGoodsEs);
    }
}
