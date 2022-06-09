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
package com.taotao.cloud.sms.common.service.impl;

import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.sms.common.exception.PhoneIsNullException;
import com.taotao.cloud.sms.common.exception.RetryTimeShortException;
import com.taotao.cloud.sms.common.exception.TypeIsNullException;
import com.taotao.cloud.sms.common.model.NoticeData;
import com.taotao.cloud.sms.common.model.VerificationCode;
import com.taotao.cloud.sms.common.model.VerificationCodeTypeGenerate;
import com.taotao.cloud.sms.common.properties.VerificationCodeProperties;
import com.taotao.cloud.sms.common.repository.VerificationCodeRepository;
import com.taotao.cloud.sms.common.service.CodeGenerate;
import com.taotao.cloud.sms.common.service.NoticeService;
import com.taotao.cloud.sms.common.service.VerificationCodeService;
import com.taotao.cloud.sms.common.utils.RandomUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 手机验证码服务实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:49:44
 */
@Service
public class DefaultVerificationCodeService implements VerificationCodeService {

	private final VerificationCodeRepository repository;

	private final VerificationCodeProperties config;

	private final NoticeService noticeService;

	private final CodeGenerate codeGenerate;

	private final VerificationCodeTypeGenerate verificationCodeTypeGenerate;

	public DefaultVerificationCodeService(VerificationCodeRepository repository,
		VerificationCodeProperties config,
		NoticeService noticeService, CodeGenerate codeGenerate,
		@Nullable VerificationCodeTypeGenerate verificationCodeTypeGenerate) {
		this.repository = repository;
		this.config = config;
		this.noticeService = noticeService;
		this.codeGenerate = codeGenerate;
		this.verificationCodeTypeGenerate = verificationCodeTypeGenerate;
	}

	@Override
	public String find(String phone, String identificationCode) {
		if (StringUtil.isBlank(phone)) {
			return null;
		}

		phoneValidation(phone);

		VerificationCode verificationCode = repository.findOne(phone, identificationCode);

		return verificationCode == null ? null : verificationCode.getCode();
	}

	@Nullable
	private String createIdentificationCode() {
		if (!config.isUseIdentificationCode()) {
			return null;
		}

		return RandomUtils.nextString(config.getIdentificationCodeLength());
	}

	@Override
	public void send(String tempPhone, @Nullable String type) {
		String phone = StringUtil.trimToNull(tempPhone);

		if (phone == null) {
			throw new PhoneIsNullException();
		}

		phoneValidation(phone);

		String identificationCode = createIdentificationCode();
		VerificationCodeCheckResult verificationCodeCheckResult = verificationCodeCheck(phone,
			identificationCode);
		VerificationCode verificationCode = verificationCodeCheckResult.verificationCode;

		Map<String, String> params = buildSendParams(verificationCode);

		if (type == null && verificationCodeTypeGenerate != null) {
			type = verificationCodeTypeGenerate.getType(phone, params);
		}
		if (type == null) {
			type = config.getType();
		}
		if (type == null) {
			throw new TypeIsNullException();
		}

		NoticeData notice = new NoticeData();
		notice.setType(type);
		notice.setParams(params);

		if (noticeService.send(notice, phone) && verificationCodeCheckResult.newVerificationCode) {
			repository.save(verificationCode);
		}
	}

	private VerificationCodeCheckResult verificationCodeCheck(String phone,
		@Nullable String identificationCode) {
		VerificationCode verificationCode = repository.findOne(phone, identificationCode);
		Long expirationTime = config.getExpirationTime();

		boolean newVerificationCode = false;
		if (verificationCode == null) {
			verificationCode = new VerificationCode();
			verificationCode.setPhone(phone);
			verificationCode.setIdentificationCode(identificationCode);

			Long retryIntervalTime = config.getRetryIntervalTime();

			if (expirationTime != null && expirationTime > 0) {
				verificationCode.setExpirationTime(LocalDateTime.now().plusSeconds(expirationTime));
			}
			if (retryIntervalTime != null && retryIntervalTime > 0) {
				verificationCode.setRetryTime(LocalDateTime.now().plusSeconds(retryIntervalTime));
			}

			verificationCode.setCode(codeGenerate.generate());
			newVerificationCode = true;
		} else {
			LocalDateTime retryTime = verificationCode.getRetryTime();

			if (retryTime != null) {
				long surplus = Duration.between(LocalDateTime.now(), retryTime).getSeconds();
				if (surplus > 0) {
					throw new RetryTimeShortException(surplus);
				}
			}
		}

		VerificationCodeCheckResult result = new VerificationCodeCheckResult();
		result.verificationCode = verificationCode;
		result.newVerificationCode = newVerificationCode;

		return result;
	}

	private Map<String, String> buildSendParams(VerificationCode verificationCode) {
		Long expirationTime = config.getExpirationTime();
		Map<String, String> params = new HashMap<>(4);
		params.put(MSG_KEY_CODE, verificationCode.getCode());
		if (verificationCode.getIdentificationCode() != null) {
			params.put(MSG_KEY_IDENTIFICATION_CODE, verificationCode.getIdentificationCode());
		}
		if (config.isTemplateHasExpirationTime() && expirationTime != null && expirationTime > 0) {
			params.put(MSG_KEY_EXPIRATION_TIME_OF_SECONDS, String.valueOf(expirationTime));
			params.put(MSG_KEY_EXPIRATION_TIME_OF_MINUTES, String.valueOf(expirationTime / 60));
		}
		return params;
	}

	private static class VerificationCodeCheckResult {

		VerificationCode verificationCode;

		boolean newVerificationCode;
	}

	@Override
	public boolean verify(String phone, String code, @Nullable String identificationCode) {
		if (StringUtil.isAnyBlank(phone, code)) {
			return false;
		}

		phoneValidation(phone);

		VerificationCode verificationCode = repository.findOne(phone, identificationCode);

		if (verificationCode == null) {
			return false;
		}

		boolean verifyData = Objects.equals(verificationCode.getCode(), code);

		if (verifyData && config.isDeleteByVerifySucceed()) {
			repository.delete(phone, identificationCode);
		}

		if (!verifyData && config.isDeleteByVerifyFail()) {
			repository.delete(phone, identificationCode);
		}

		return verifyData;
	}

	private void phoneValidation(String phone) {
		if (!noticeService.phoneRegValidation(phone)) {
			throw new PhoneIsNullException();
		}
	}

}
