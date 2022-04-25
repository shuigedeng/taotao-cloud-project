package com.taotao.cloud.goods.biz.repository;

import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 商品索引
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:27:45
 */
public interface EsGoodsIndexRepository extends ElasticsearchRepository<EsGoodsIndex, Long> {

}
