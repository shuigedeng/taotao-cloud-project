package com.taotao.cloud.goods.biz.repository;

import com.taotao.cloud.goods.biz.entity.EsGoodsIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 商品索引
 **/
public interface EsGoodsIndexRepository extends ElasticsearchRepository<EsGoodsIndex, String> {

}
