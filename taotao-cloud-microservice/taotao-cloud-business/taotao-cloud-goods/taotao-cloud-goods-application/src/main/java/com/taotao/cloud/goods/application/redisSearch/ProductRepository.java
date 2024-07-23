package com.taotao.cloud.goods.application.redisSearch;

import com.redis.om.spring.repository.RedisEnhancedRepository;

public interface ProductRepository extends RedisEnhancedRepository<Product, String> {
}
