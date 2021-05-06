package com.gupaoedu.vip.mall.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

/*****
 * @Author:
 * @Description:
 ****/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable{

    @Id
    private String _id;       //主键
    private String userName; //用户名字
    private String name;     //商品名字
    private Integer price;   //单价
    private String image;    //商品图片
    private String skuId;   //商品ID
    private Integer num;    //商品数量
}