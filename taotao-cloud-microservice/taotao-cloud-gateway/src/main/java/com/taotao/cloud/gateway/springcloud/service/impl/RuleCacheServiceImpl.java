package com.taotao.cloud.gateway.springcloud.service.impl;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.gateway.springcloud.rule.BlackList;
import com.taotao.cloud.gateway.springcloud.rule.RuleConstant;
import com.taotao.cloud.gateway.springcloud.service.IRuleCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 规则缓存实现业务类
 */
@Service
public class RuleCacheServiceImpl implements IRuleCacheService {

	@Autowired
	private RedisRepository repository;

	@Override
	public Set<Object> getBlackList(String ip) {
		return repository.sGet(RuleConstant.getBlackListCacheKey(ip));
	}

	@Override
	public Set<Object> getBlackList() {
		return repository.sGet(RuleConstant.getBlackListCacheKey());
	}

	@Override
	public void setBlackList(BlackList blackList) {
		String key = StringUtils.isNotBlank(blackList.getIp()) ? RuleConstant
			.getBlackListCacheKey(blackList.getIp())
			: RuleConstant.getBlackListCacheKey();
		repository.sSet(key, JsonUtils.toJSONString(blackList));
	}

	@Override
	public void deleteBlackList(BlackList blackList) {
		String key = StringUtils.isNotBlank(blackList.getIp()) ? RuleConstant
			.getBlackListCacheKey(blackList.getIp())
			: RuleConstant.getBlackListCacheKey();
		repository.setRemove(key, JsonUtils.toJSONString(blackList));
	}
}
