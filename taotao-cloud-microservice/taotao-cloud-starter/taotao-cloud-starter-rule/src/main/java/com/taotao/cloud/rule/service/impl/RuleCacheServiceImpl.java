package com.taotao.cloud.rule.service.impl;

import cn.hutool.json.JSONObject;
import com.taotao.cloud.rule.constant.RuleConstant;
import com.taotao.cloud.rule.entity.BlackList;
import com.taotao.cloud.rule.service.IRuleCacheService;
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
	private RedisService redisService;

	@Override
	public Set<Object> getBlackList(String ip) {
		return redisService.sGet(RuleConstant.getBlackListCacheKey(ip));
	}

	@Override
	public Set<Object> getBlackList() {
		return redisService.sGet(RuleConstant.getBlackListCacheKey());
	}

	@Override
	public void setBlackList(BlackList blackList) {
		String key = StringUtils.isNotBlank(blackList.getIp()) ? RuleConstant
			.getBlackListCacheKey(blackList.getIp())
			: RuleConstant.getBlackListCacheKey();
		redisService.sSet(key, JSONObject.toJSONString(blackList));
	}

	@Override
	public void deleteBlackList(BlackList blackList) {
		String key = StringUtils.isNotBlank(blackList.getIp()) ? RuleConstant
			.getBlackListCacheKey(blackList.getIp())
			: RuleConstant.getBlackListCacheKey();
		redisService.setRemove(key, JSONObject.toJSONString(blackList));
	}
}
