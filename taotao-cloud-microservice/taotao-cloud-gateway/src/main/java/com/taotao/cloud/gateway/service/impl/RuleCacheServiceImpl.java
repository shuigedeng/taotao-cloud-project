package com.taotao.cloud.gateway.service.impl;

import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.gateway.rule.BlackList;
import com.taotao.cloud.gateway.rule.RuleConstant;
import com.taotao.cloud.gateway.service.IRuleCacheService;
import com.taotao.cloud.redis.repository.RedisRepository;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		repository.sSet(key, JsonUtil.toJSONString(blackList));
	}

	@Override
	public void deleteBlackList(BlackList blackList) {
		String key = StringUtils.isNotBlank(blackList.getIp()) ? RuleConstant
			.getBlackListCacheKey(blackList.getIp())
			: RuleConstant.getBlackListCacheKey();
		repository.setRemove(key, JsonUtil.toJSONString(blackList));
	}
}
