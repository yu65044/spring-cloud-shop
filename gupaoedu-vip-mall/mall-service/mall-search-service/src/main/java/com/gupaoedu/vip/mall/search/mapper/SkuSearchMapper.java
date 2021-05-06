package com.gupaoedu.vip.mall.search.mapper;

import com.gupaoedu.vip.mall.search.model.SkuEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/*****
 * @Author:
 * @Description:
 ****/
public interface SkuSearchMapper extends ElasticsearchRepository<SkuEs,String> {
}
