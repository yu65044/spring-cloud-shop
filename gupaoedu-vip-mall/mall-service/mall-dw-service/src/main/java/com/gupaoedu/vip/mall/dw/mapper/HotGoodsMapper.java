package com.gupaoedu.vip.mall.dw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gupaoedu.vip.mall.dw.model.HotGoods;
import com.gupaoedu.vip.mall.dw.util.DruidPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sun.awt.image.IntegerInterleavedRaster;

import java.util.List;
import java.util.Map;

public interface HotGoodsMapper  extends BaseMapper<HotGoods> {

    /***
     * 1:时间限制  最近1小时内
     * 2:查询数量
     * 3:根据查询数量排序
     * 4:如果已经是分析过的热门商品，需要把它排除
     */
    @Select("SELECT uri,count(*) as viewCount FROM msitemslog WHERE __time>=TIMESTAMP '${time}' AND uri NOT IN ('${urls}') GROUP BY uri HAVING viewCount>#{max} ORDER BY viewCount desc LIMIT #{size}")
    List<Map<String,String>> searchHotGoods(@Param("size") Integer size,
                             @Param("time") String time,
                             @Param("urls")String urls,
                             @Param("max")Integer max);


    /***
     * 排除指定数据  NOT IN ('123.html','456.html','789.html')
     */
    @Select("SELECT ip,uri,__time as accesstime FROM msitemslog WHERE __time>= TIMESTAMP '${time}' AND uri NOT IN('${urls}') LIMIT #{size}")
    List<HotGoods> searchExclude(@Param("size")Integer size,@Param("time")String time,@Param("urls")String urls);

    /***
     * 查询过去几小时前N条记录
     */
    @Select("SELECT ip,uri,__time as accesstime FROM msitemslog WHERE __time>= TIMESTAMP '${time}' LIMIT #{size}")
    List<HotGoods> search(@Param("size")Integer size,@Param("time")String time);

    /***
     * 分页查询+排序
     */
    @Select("SELECT ip,uri,__time as accesstime FROM msitemslog ORDER BY ${sort} ${sortType} LIMIT #{size} offset #{offset}")
    List<HotGoods> pageListSort(DruidPage druidPage);


    /***
     * 分页查询
     */
    @Select("SELECT ip,uri,__time as accesstime FROM msitemslog LIMIT #{size} offset #{offset}")
    List<HotGoods> pageList(@Param("size")Integer size,@Param("offset")Long offset);


    /****
     * TopNum:前N条记录
     */
    @Select("SELECT ip,uri,__time as accesstime FROM msitemslog LIMIT #{size}")
    List<HotGoods> topNum(Integer size);
}
