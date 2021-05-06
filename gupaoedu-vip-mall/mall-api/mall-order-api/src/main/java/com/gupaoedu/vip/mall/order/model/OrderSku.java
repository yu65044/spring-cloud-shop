package com.gupaoedu.vip.mall.order.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*****
 * @Author:
 * @Description:
 ****/
@Data
@AllArgsConstructor
@NoArgsConstructor
//MyBatisPlus表映射注解
@TableName(value = "order_sku")
public class OrderSku {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String image;
    private String skuId;
    private String orderId;
    private String name;
    private Integer price;
    private Integer num;    //数量
    private Integer money;  //总金额
}