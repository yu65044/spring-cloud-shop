package com.gupaoedu.vip.canal.listener;

import com.gupaoedu.vip.canal.job.dynamic.DynamicJob;
import com.gupaoedu.vip.canal.job.dynamic.DynamicTaskCreate;
import com.gupaoedu.vip.mall.goods.feign.SkuFeign;
import com.gupaoedu.vip.mall.goods.model.AdItems;
import com.gupaoedu.vip.mall.seckill.model.SeckillActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import java.text.SimpleDateFormat;

/*****
 * @Author:
 * @Description:
 ****/
@Component
@CanalTable(value = "seckill_activity")
public class SeckillActivityHandler implements EntryHandler<SeckillActivity> {

    @Autowired
    private DynamicTaskCreate dynamicTaskCreate;

    /****
     * 增加活动
     * @param seckillActivity
     */
    @Override
    public void insert(SeckillActivity seckillActivity) {
        //创建任务调度，活动结束的时候执行
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        String cron = simpleDateFormat.format(seckillActivity.getEndTime());
        System.out.println("cron:"+cron);
        dynamicTaskCreate.create(seckillActivity.getId(), cron, 1, new DynamicJob(),seckillActivity.getId());
    }

    @Override
    public void update(SeckillActivity before, SeckillActivity after) {
        //创建任务调度，活动结束的时候执行
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        String cron = simpleDateFormat.format(after.getEndTime());
        System.out.println("cron:"+cron);
        dynamicTaskCreate.create(after.getId(), cron, 1, new DynamicJob(),after.getId());
    }

    @Override
    public void delete(SeckillActivity seckillActivity) {

    }
}
