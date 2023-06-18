package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.service;

import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.entity.User;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.utils.CommonUtil;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

	@Autowired
	private RedisRepository cacheStore;

	public User getCurrentUser(String userId) {
		String userKey = CommonUtil.buildUserKey(userId);
		return (User) cacheStore.get(userKey);
	}
}
