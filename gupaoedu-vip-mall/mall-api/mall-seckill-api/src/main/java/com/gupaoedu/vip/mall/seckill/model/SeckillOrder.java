package com.gupaoedu.vip.mall.seckill.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//MyBatisPlus表映射注解
@TableName(value = "seckill_order")
public class SeckillOrder {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String seckillGoodsId;
    private String weixinTransactionId;
    private String username;
    private Integer money;
    private Integer status;
    private Date createTime;
    private Date payTime;
    private Integer num;    //抢购数量
}