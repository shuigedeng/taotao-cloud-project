/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.data.mybatis.plus.cipher.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.util.Objects;

/**
 * TestEncryptCoderDemoUtil
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 14:04:55
 */
public class EncryptCodeUtil {
	/**
	 * 公元前提供者名称
	 */
	public static final String BC_PROVIDER_NAME = "BC";
	/**
	 * 公元前提供者
	 */
	public static final Provider BC_PROVIDER = new BouncyCastleProvider();
	/**
	 * 关键
	 */
	public static final String KEY = "RoGYxGHUmg+hGgtSaN0O2w==";
	/**
	 * 时间
	 */
	public static final int TIME = 2;
	/**
	 * aes算法
	 */
	public static final String AES_ALGORITHM = "AES";

	static {
		// 添加provider
		Security.addProvider(BC_PROVIDER);
	}

	/**
	 * 获取解密后数据
	 *
	 * @param encryptData 密文
	 * @return {@link String }
	 * @since 2022-07-12 14:04:55
	 */
	public static String decryptData(String encryptData) {
		if (encryptData == null) {
			return null;
		}
		return decryptAirth(encryptData, KEY, TIME);
	}

	/**
	 * 获取解密后的密文
	 *
	 * @param decryptdata
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String encryptData(String decryptdata) {
		if (decryptdata == null) {
			return null;
		}
		return encryptAirth(decryptdata, KEY, TIME);
	}

	/**
	 * 数据加密
	 *
	 * @param data            待加密字符串数据
	 * @param base64StringKey base64编码字符串密钥
	 * @param encryptTime     加密次数
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String encryptAirth(String data, String base64StringKey, int encryptTime) {
		String str = data;
		if (encryptTime == 0) {
			return str;
		}
		for (int i = 0; i < encryptTime; i++) {
			str = EncryptCodeUtil.encrypt(str, base64StringKey);
		}
		return str;
	}

	/**
	 * 数据解密
	 *
	 * @param encryptData     密文
	 * @param base64StringKey base64编码字符串密钥
	 * @param encryptTime     解密次数
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String decryptAirth(String encryptData, String base64StringKey, int encryptTime) {
		String str = encryptData;
		if (encryptTime == 0) {
			return str;
		}
		for (int i = 0; i < encryptTime; i++) {
			str = new String(Objects.requireNonNull(EncryptCodeUtil.decrypt(Base64.decode(str), base64StringKey)));
		}
		return str;
	}

	/**
	 * 数据加密
	 *
	 * @param data            待加密字符串数据
	 * @param base64StringKey base64编码字符串密钥
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String encrypt(String data, String base64StringKey) {
		byte[] encryptData = encrypt(data.getBytes(), base64StringKey);
		assert encryptData != null;
		return Base64.toBase64String(encryptData);
	}

	/**
	 * 数据加密
	 *
	 * @param data            待加密二进制数据
	 * @param base64StringKey base64编码字符串密钥
	 * @return {@link byte[] }
	 * @since 2022-07-12 14:04:56
	 */
	public static byte[] encrypt(byte[] data, String base64StringKey) {
		try {
			return encrypt(data, Base64.decode(base64StringKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数据加密
	 *
	 * @param data 待加密二进制数据
	 * @param key  二进制密钥
	 * @return {@link byte[] }
	 * @since 2022-07-12 14:04:56
	 */
	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 构建密钥
		Key k = new SecretKeySpec(key, AES_ALGORITHM);
		// 构建密码器
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM, BC_PROVIDER_NAME);
		// 设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, k);
		// 执行加密操作
		return cipher.doFinal(data);
	}

	/**
	 * 数据解密
	 *
	 * @param encryptData     密文
	 * @param base64StringKey base64编码字符串密钥
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String decrypt(String encryptData, String base64StringKey) {
		byte[] decryptData = decrypt(Base64.decode(encryptData), base64StringKey);
		assert decryptData != null;
		return new String(decryptData);
	}

	/**
	 * 数据解密
	 *
	 * @param data            待解密二进制数据
	 * @param base64StringKey base64编码字符串密钥
	 * @return {@link byte[] }
	 * @since 2022-07-12 14:04:56
	 */
	public static byte[] decrypt(byte[] data, String base64StringKey) {
		try {
			return decrypt(data, Base64.decode(base64StringKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数据解密
	 *
	 * @param data 待解密二进制数据
	 * @param key  二进制密钥
	 * @return {@link byte[] }
	 * @since 2022-07-12 14:04:56
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 构建密钥
		Key k = new SecretKeySpec(key, AES_ALGORITHM);
		// 构建密码器
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM, BC_PROVIDER_NAME);
		// 设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		// 执行解密操作
		return cipher.doFinal(data);
	}

	/**
	 * 生成Base64编码字符串密钥
	 *
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String initBase64StringKey() {
		try {
			return Base64.toBase64String(initKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成二进制密钥
	 *
	 * @return {@link byte[] }
	 * @since 2022-07-12 14:04:56
	 */
	public static byte[] initKey() throws Exception {
		// 根据算法获取密钥生成器
		KeyGenerator kg = KeyGenerator.getInstance(AES_ALGORITHM, BC_PROVIDER_NAME);
		// 初始化密钥位数
		kg.init(256);
		// 生成密钥
		SecretKey secretKey = kg.generateKey();
		// 获取密钥的二进制编码形式
		return secretKey.getEncoded();
	}

	/**
	 * 加密数字
	 *
	 * @param number
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String encryptNumber(BigDecimal number) {
		return encryptString(number.toString());
	}

	/**
	 * 加密字符串
	 *
	 * @param str
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String encryptString(String str) {
		return EncryptCodeUtil.encryptAirth(str, KEY, TIME);
	}

	/**
	 * 解密字符串
	 *
	 * @param encryptStr
	 * @return {@link String }
	 * @since 2022-07-12 14:04:56
	 */
	public static String decryptString(String encryptStr) {
		return EncryptCodeUtil.decryptAirth(encryptStr, KEY, TIME);
	}

	/**
	 * 解密数字
	 *
	 * @param encryptNumber
	 * @return {@link BigDecimal }
	 * @since 2022-07-12 14:04:56
	 */
	public static BigDecimal decryptNumber(String encryptNumber) {
		return new BigDecimal(decryptString(encryptNumber));
	}
}
