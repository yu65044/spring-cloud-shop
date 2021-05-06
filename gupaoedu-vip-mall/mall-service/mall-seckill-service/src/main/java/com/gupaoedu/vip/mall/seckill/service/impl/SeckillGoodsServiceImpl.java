package com.gupaoedu.vip.mall.seckill.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gupaoedu.vip.mall.seckill.mapper.SeckillGoodsMapper;
import com.gupaoedu.vip.mall.seckill.model.SeckillGoods;
import com.gupaoedu.vip.mall.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper,SeckillGoods> implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /****
     * 热门商品分离
     * @param uri：商品ID
     */
    @Override
    public void isolation(String uri) {
        //1.锁定商品
        QueryWrapper<SeckillGoods> seckillGoodsQueryWrapper = new QueryWrapper<SeckillGoods>();
        seckillGoodsQueryWrapper.eq("id",uri);
        seckillGoodsQueryWrapper.eq("islock",0);
        seckillGoodsQueryWrapper.gt("store_count",0);
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setIslock(1);
        int count = seckillGoodsMapper.update(seckillGoods, seckillGoodsQueryWrapper);

        //2.分离->查询出来存入到Redis缓存
        if(count>0){
            //查询商品个数
            seckillGoods = seckillGoodsMapper.selectById(uri);
            // HotSeckillGoods
            //                1:5
            redisTemplate.boundHashOps("HotSeckillGoods").increment(uri,seckillGoods.getStoreCount());
        }
    }

    //根据活动ID查询商品信息
    @Override
    public List<SeckillGoods> actGoods(String acid) {
        QueryWrapper<SeckillGoods> seckillGoodsQueryWrapper = new QueryWrapper<SeckillGoods>();
        seckillGoodsQueryWrapper.eq("activity_id",acid);
        return seckillGoodsMapper.selectList(seckillGoodsQueryWrapper);
    }
}
