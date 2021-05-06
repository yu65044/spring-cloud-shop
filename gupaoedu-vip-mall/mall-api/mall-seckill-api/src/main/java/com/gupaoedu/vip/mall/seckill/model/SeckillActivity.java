package com.gupaoedu.vip.mall.seckill.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//MyBatisPlus表映射注解
@TableName(value = "seckill_activity")
@Table
public class SeckillActivity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String activityName;
    private Integer type;   //活动分类 0shop秒杀、1 每日特价、2 大牌闪购、3 品类秒杀、4 节日活动
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
}