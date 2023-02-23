package com.taotao.cloud.auth.biz.authentication.accountVerification.service;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultAccountVerificationService implements AccountVerificationService {
	@Autowired
	private RedisRepository repository;

	@Override
	public boolean verifyCaptcha(String verificationCode) {
		return false;
	}
}
