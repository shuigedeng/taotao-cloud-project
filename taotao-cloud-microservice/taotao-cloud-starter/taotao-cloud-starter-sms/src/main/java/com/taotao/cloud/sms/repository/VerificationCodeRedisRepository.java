/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.sms.model.VerificationCode;
import com.taotao.cloud.sms.utils.StringUtils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;
import org.springframework.lang.Nullable;

/**
 * 验证码redis储存实现
 *
 * @author shuigedeng
 */
public class VerificationCodeRedisRepository implements VerificationCodeRepository {

	private final RedisRepository redisRepository;

	private final ObjectMapper objectMapper;

	public VerificationCodeRedisRepository(
		RedisRepository redisRepository,
		ObjectMapper objectMapper) {
		this.redisRepository = redisRepository;
		this.objectMapper = objectMapper;
	}

	@Override
	public VerificationCode findOne(String phone, @Nullable String identificationCode) {
		String key = key(phone, identificationCode);
		String data = (String) redisRepository.get(key);

		if (StringUtils.isBlank(data)) {
			LogUtil.debug("json data is empty for key: {}", key);
			return null;
		}

		try {
			return objectMapper.readValue(data, VerificationCode.class);
		} catch (Exception e) {
			LogUtil.debug(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void save(VerificationCode verificationCode) {
		String key = key(verificationCode.getPhone(), verificationCode.getIdentificationCode());
		LocalDateTime expirationTime = verificationCode.getExpirationTime();
		String value;

		try {
			value = objectMapper.writeValueAsString(verificationCode);
		} catch (Exception e) {
			LogUtil.debug(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		if (expirationTime == null) {
			redisRepository.set(key, value);
		} else {
			long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
			long end = expirationTime.toEpochSecond(ZoneOffset.UTC);
			long timeout = end - now;

			redisRepository.setExpire(key, value, timeout, TimeUnit.SECONDS);
		}
	}

	@Override
	public void delete(String phone, @Nullable String identificationCode) {
		redisRepository.del(key(phone, identificationCode));
	}

	private String key(String phone, @Nullable String identificationCode) {
		String tempIdentificationCode = StringUtils.trimToNull(identificationCode);

		StringBuilder keyBuilder = new StringBuilder();

		keyBuilder.append(RedisConstant.SMS_VERIFICATION_CODE_KEY_PREFIX);
		keyBuilder.append(StringUtils.trimToNull(phone));

		if (tempIdentificationCode != null) {
			keyBuilder.append(":");
			keyBuilder.append(tempIdentificationCode);
		}

		return keyBuilder.toString();
	}

}
