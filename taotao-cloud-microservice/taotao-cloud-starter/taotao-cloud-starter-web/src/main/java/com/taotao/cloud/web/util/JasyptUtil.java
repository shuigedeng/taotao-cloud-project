/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.web.util;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/13 09:25
 */
public class JasyptUtil {

	private static StringEncryptor stringEncryptor;

	static {
		stringEncryptor = ContextUtil.getBean(StringEncryptor.class, true);

		if (stringEncryptor == null) {
			try {
				String password = ContextUtil.getApplicationContext().getEnvironment()
					.getProperty("jasypt.encryptor.password", "taotao-cloud");
				stringEncryptor = getInstance(password);
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}

	public String encrypt(String content) {
		return stringEncryptor.encrypt(content);
	}

	public String decrypt(String content) {
		return stringEncryptor.decrypt(content);
	}

	public static StringEncryptor getInstance(String password) throws Exception {
		if (password == null || password.trim().equals("")) {
			System.out.println("秘钥不能为空！");
			throw new Exception("org.jasypt.encryption.StringEncryptor秘钥不能为空！");
		}

		if (stringEncryptor == null) {
			PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
			SimpleStringPBEConfig config = new SimpleStringPBEConfig();
			config.setPassword(password);
			config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
			config.setKeyObtentionIterations("1000");
			config.setPoolSize("1");
			config.setProviderName("SunJCE");
			config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
			config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
			config.setStringOutputType("base64");
			encryptor.setConfig(config);

			stringEncryptor = encryptor;
		}
		return stringEncryptor;
	}
}
