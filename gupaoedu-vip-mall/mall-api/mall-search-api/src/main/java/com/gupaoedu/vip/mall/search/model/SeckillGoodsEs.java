package com.gupaoedu.vip.mall.search.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Document(indexName = "shopseckill",type = "seckillgoodses")
public class SeckillGoodsEs implements Serializable {

    @Id
    private String id;
    private String supId;
    private String skuId;
    @Field(type= FieldType.Text,searchAnalyzer = "ik_smart",analyzer = "ik_smart")
    private String name;
    private String images;
    private Integer price;
    private Integer seckillPrice;
    private Integer num;
    private Integer storeCount;
    @Field(type=FieldType.Keyword)
    private String activityId;
}

