/*
 * Copyright 2018-2022 the original author or authors.
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
package com.taotao.cloud.sms.service.impl;

import com.taotao.cloud.sms.service.CodeGenerate;
import com.taotao.cloud.sms.properties.VerificationCodeProperties;
import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 默认验证码生成
 *
 * @author shuigedeng
 */
public class DefaultCodeGenerate implements CodeGenerate {

	private final VerificationCodeProperties config;

	public DefaultCodeGenerate(VerificationCodeProperties config) {
		this.config = config;
	}

	@Override
	public String generate() {
		int codeLength = config.getCodeLength();

		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		format.setMaximumIntegerDigits(codeLength);
		format.setMinimumIntegerDigits(codeLength);

		return format.format(ThreadLocalRandom.current().nextInt((int) Math.pow(10, codeLength)));
	}

}
