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
package com.taotao.cloud.sms.properties;

import com.taotao.cloud.sms.enums.RepositoryType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 验证码配置
 *
 * @author shuigedeng
 */
@ConfigurationProperties(prefix = VerificationCodeProperties.PREFIX)
public class VerificationCodeProperties {

	public static final String PREFIX = "taotao.cloud.sms.verification-code";

	/**
	 * 默认验证码业务所使用的类型
	 */
	public static final String DEFAULT_TYPE = "VerificationCode";

	/**
	 * 验证码业务所使用的类型
	 */
	private String type = DEFAULT_TYPE;

	/**
	 * 验证码业务所使用的类型
	 */
	private RepositoryType repository = RepositoryType.REDIS;

	/**
	 * 验证码过期时间,小于等于0表示不过期,单位秒
	 */
	private Long expirationTime;

	/**
	 * 重新发送验证码间隔时间,小于等于0表示不启用,单位秒
	 */
	private Long retryIntervalTime;

	/**
	 * 验证码长度
	 */
	private int codeLength = 6;

	/**
	 * 是否使用识别码
	 */
	private boolean useIdentificationCode = false;

	/**
	 * 识别码长度
	 */
	private int identificationCodeLength = 3;

	/**
	 * 验证成功是否删除验证码
	 */
	private boolean deleteByVerifySucceed = true;

	/**
	 * 验证失败是否删除验证码
	 */
	private boolean deleteByVerifyFail = false;

	/**
	 * 模板中是否包含过期时间
	 */
	private boolean templateHasExpirationTime = false;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Long getRetryIntervalTime() {
		return retryIntervalTime;
	}

	public void setRetryIntervalTime(Long retryIntervalTime) {
		this.retryIntervalTime = retryIntervalTime;
	}

	public int getCodeLength() {
		return codeLength;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

	public boolean isUseIdentificationCode() {
		return useIdentificationCode;
	}

	public void setUseIdentificationCode(boolean useIdentificationCode) {
		this.useIdentificationCode = useIdentificationCode;
	}

	public int getIdentificationCodeLength() {
		return identificationCodeLength;
	}

	public void setIdentificationCodeLength(int identificationCodeLength) {
		this.identificationCodeLength = identificationCodeLength;
	}

	public boolean isDeleteByVerifySucceed() {
		return deleteByVerifySucceed;
	}

	public void setDeleteByVerifySucceed(boolean deleteByVerifySucceed) {
		this.deleteByVerifySucceed = deleteByVerifySucceed;
	}

	public boolean isDeleteByVerifyFail() {
		return deleteByVerifyFail;
	}

	public void setDeleteByVerifyFail(boolean deleteByVerifyFail) {
		this.deleteByVerifyFail = deleteByVerifyFail;
	}

	public boolean isTemplateHasExpirationTime() {
		return templateHasExpirationTime;
	}

	public void setTemplateHasExpirationTime(boolean templateHasExpirationTime) {
		this.templateHasExpirationTime = templateHasExpirationTime;
	}

	public RepositoryType getRepository() {
		return repository;
	}

	public void setRepository(RepositoryType repository) {
		this.repository = repository;
	}
}
