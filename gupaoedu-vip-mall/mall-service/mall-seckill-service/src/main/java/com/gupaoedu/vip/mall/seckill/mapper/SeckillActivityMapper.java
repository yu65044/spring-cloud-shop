package com.gupaoedu.vip.mall.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gupaoedu.vip.mall.seckill.model.SeckillActivity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {

    /****
     * 活动列表（有效的）
     */
    @Select("SELECT * FROM seckill_activity WHERE end_time>NOW() ORDER BY start_time ASC LIMIT 5")
    List<SeckillActivity> validActivity();

}
