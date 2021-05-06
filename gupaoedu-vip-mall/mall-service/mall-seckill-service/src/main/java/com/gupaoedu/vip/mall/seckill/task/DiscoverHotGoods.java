package com.gupaoedu.vip.mall.seckill.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.elasticjob.lite.annotation.ElasticSimpleJob;
import com.gupaoedu.mall.util.RespResult;
import com.gupaoedu.vip.mall.dw.feign.HotGoodsFeign;
import com.gupaoedu.vip.mall.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ElasticSimpleJob(
        jobName = "${elaticjob.zookeeper.namespace}",
        shardingTotalCount = 1,
        cron = "0/10 * * * * ? *"
)
@Component
public class DiscoverHotGoods implements SimpleJob {

    @Autowired
    private HotGoodsFeign hotGoodsFeign;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${hot.size}")
    private Integer size;
    @Value("${hot.hour}")
    private Integer hour;
    @Value("${hot.max}")
    private Integer max;

    //任务执行方法
    @Override
    public void execute(ShardingContext shardingContext) {
        //查询已存在的热门商品
        String[] urls =isolationList();
        RespResult<List<Map<String, String>>> listRespResult = hotGoodsFeign.searchHotGoods(size, hour, max, urls);
        List<Map<String, String>> data = listRespResult.getData();

        //Map数据 uri
        for (Map<String, String> dataMap : data) {
            // /msitemslog/123.html
            seckillGoodsService.isolation(uriReplace(1,dataMap.get("uri")));
        }
    }

    /***
     * 查询已存在的所有热门商品ID集合
     */
    public String[] isolationList(){
        Set<String> ids = redisTemplate.boundHashOps("HotSeckillGoods").keys();
        //转成字符串数组
        String[] allIds = new String[ids.size()];
        ids.toArray(allIds);

        //将id转成url
        for (int i = 0; i < allIds.length; i++) {
            allIds[i] = uriReplace(2,allIds[i]);
        }

        return allIds;
    }

    /***
     * URI地址处理
     * @param type：1传入的是uri  2传入的商品Id
     * @param uri
     * @return
     */
    public String uriReplace(Integer type,String uri){
        switch (type){
            case 1:
                uri=uri.replace("/msitems/","").replace(".html","");
                break;
            case 2:
                uri="/msitems/"+uri+".html";
                break;
            default:
                uri="/msitems/"+uri+".html";
        }
        return uri;
    }
}
