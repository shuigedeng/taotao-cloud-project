package com.taotao.cloud.goods.biz.redisSearch;

import com.redis.om.spring.repository.RedisEnhancedRepository;

public interface ProductRepository extends RedisEnhancedRepository<Product, String> {
}
