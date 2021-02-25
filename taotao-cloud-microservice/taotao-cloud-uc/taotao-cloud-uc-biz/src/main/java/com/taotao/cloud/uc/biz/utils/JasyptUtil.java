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
package com.taotao.cloud.uc.biz.utils;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * @author dengtao
 * @date 2020/11/13 09:25
 * @since v1.0
 */
public class JasyptUtil {


	// public static void main(final String[] args) {
	// 	 String password = "taotao-cloud";
	// 	 String input = "123456";
	// 	try {
	// 		StringEncryptor stringEncryptor = JasyptUtil.getInstance(password);
	// 		String mima = stringEncryptor.encrypt(input);
	// 		System.out.println("【" + input + "】被加密成【" + mima + "】");

	// 		String jiemi = stringEncryptor.decrypt(mima);
	// 		System.out.println("【" + mima + "】被解密成【" + jiemi + "】");

	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// }

	private static StringEncryptor stringEncryptor = null;

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
