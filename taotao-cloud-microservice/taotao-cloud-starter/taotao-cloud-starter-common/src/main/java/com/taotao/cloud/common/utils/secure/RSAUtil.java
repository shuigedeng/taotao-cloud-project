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
package com.taotao.cloud.common.utils.secure;

import com.taotao.cloud.common.utils.LogUtil;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.lang3.StringUtils;

/**
 * RSAUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:55:01
 */
public class RSAUtil {

	private RSAUtil() {
	}

	/**
	 * 加密、解密方式
	 */
	private static final String RSA = "RSA";

	/**
	 * 密钥大小为96-1024位
	 */
	private static final int KEY_SIZE = 1024;

	/**
	 * 随机生成密钥对（公钥、私钥）
	 *
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-02 17:55:15
	 */
	public static Map<RSAKey, String> generatorPairKey() {
		try {
			// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA);
			// 初始化密钥对生成器，密钥大小为 96-1024 位
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			// 生成一个密钥对，保存在keyPair中
			KeyPair keyPair = keyPairGen.generateKeyPair();
			// 得到公钥
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			// 得到私钥
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

			String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
			// 得到私钥字符串
			String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
			// 将随机生成的公钥和私钥保存到Map
			Map<RSAKey, String> pairKeyMap = new EnumMap<>(RSAKey.class);
			// 0、表示公钥
			pairKeyMap.put(RSAKey.PUBLIC, publicKeyString);
			// 1、表示私钥
			pairKeyMap.put(RSAKey.PRIVATE, privateKeyString);
			return pairKeyMap;
		} catch (NoSuchAlgorithmException e) {
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * RSA 加密
	 *
	 * @param text      明文
	 * @param publicKey 公钥
	 * @return 密文
	 * @author shuigedeng
	 * @since 2021-09-02 17:55:15
	 */
	public static String encrypt(String text, String publicKey) {
		if (StringUtils.isAnyBlank(text, publicKey)) {
			return null;
		}
		try {
			// base64编码的公钥
			byte[] decoded = Base64.getDecoder().decode(publicKey);
			RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance(RSA)
				.generatePublic(new X509EncodedKeySpec(decoded));
			// RSA加密
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
			return Base64.getEncoder()
				.encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * RSA 解密
	 *
	 * @param ciphertext 密文
	 * @param privateKey 私钥
	 * @return 明文
	 * @author shuigedeng
	 * @since 2021-09-02 17:55:15
	 */
	public static String decrypt(String ciphertext, String privateKey) {
		if (StringUtils.isAnyBlank(ciphertext, privateKey)) {
			return null;
		}
		try {
			//64位解码加密后的字符串
			final byte[] bytes = Base64.getDecoder()
				.decode(ciphertext.getBytes(StandardCharsets.UTF_8));
			//base64编码的私钥
			byte[] decoded = Base64.getDecoder().decode(privateKey);
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance(RSA)
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
			//RSA解密
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
			return new String(cipher.doFinal(bytes));
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}
}
