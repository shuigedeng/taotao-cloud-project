package com.taotao.cloud.goods.biz.infrastructure.redisSearch;

import com.redis.om.spring.repository.RedisEnhancedRepository;

public interface ProductRepository extends RedisEnhancedRepository<Product, String> {
}
