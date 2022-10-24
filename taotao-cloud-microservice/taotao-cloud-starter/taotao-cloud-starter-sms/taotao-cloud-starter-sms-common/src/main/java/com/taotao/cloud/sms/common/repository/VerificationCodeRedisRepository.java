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
package com.taotao.cloud.sms.common.repository;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.constant.RedisConstant;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sms.common.model.VerificationCode;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * 验证码redis储存实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:16
 */
public class VerificationCodeRedisRepository implements VerificationCodeRepository {

	private final RedisRepository redisRepository;

	public VerificationCodeRedisRepository(RedisRepository redisRepository) {
		this.redisRepository = redisRepository;
	}

	@Override
	public VerificationCode findOne(String phone, @Nullable String identificationCode) {
		String key = key(phone, identificationCode);
		String value = (String) redisRepository.get(key);

		if (StringUtils.isBlank(value)) {
			LogUtils.debug("json data is empty for key: {}", key);
			return null;
		}

		return JsonUtils.toObject(value, VerificationCode.class);
	}

	@Override
	public void save(VerificationCode verificationCode) {
		String key = key(verificationCode.getPhone(), verificationCode.getIdentificationCode());
		LocalDateTime expirationTime = verificationCode.getExpirationTime();

		String value = JsonUtils.toJSONString(verificationCode);

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
		assert identificationCode != null;
		String tempIdentificationCode = StringUtils.trimToNull(identificationCode);

		return RedisConstant.SMS_VERIFICATION_CODE_KEY_PREFIX +
			StringUtils.trimToNull(phone) +
			":" +
			tempIdentificationCode;
	}

}
