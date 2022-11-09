package com.taotao.cloud.auth.biz.authentication.miniapp.service;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * // 小程序sessionkey缓存 过期时间应该小于微信官方文档的声明   1 在此处配置 优先级最高 2 注册为Spring Bean 可以免配置
 */
@Service
public class DefaultMiniAppSessionKeyCacheService implements MiniAppSessionKeyCacheService {

	@Autowired
	private RedisRepository redisRepository;

	@Override
	public String put(String cacheKey, String sessionKey) {
		redisRepository.set(cacheKey, sessionKey);
		return sessionKey;
	}

	@Override
	public String get(String cacheKey) {
		// 模拟 sessionkey 缓存
		return (String) redisRepository.get(cacheKey);
	}
}
