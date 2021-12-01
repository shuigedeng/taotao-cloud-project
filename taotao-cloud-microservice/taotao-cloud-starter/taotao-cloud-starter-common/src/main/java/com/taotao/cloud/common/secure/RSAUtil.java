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
package com.taotao.cloud.common.secure;

import com.taotao.cloud.common.tuple.KeyPair;
import com.taotao.cloud.common.utils.Exceptions;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

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
			java.security.KeyPair keyPair = keyPairGen.generateKeyPair();
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

	/**
	 * 数字签名，密钥算法
	 */
	public static final String RSA_ALGORITHM = "RSA";
	public static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";

	/**
	 * 获取 KeyPair
	 *
	 * @return KeyPair
	 */
	public static KeyPair genKeyPair() {
		return genKeyPair(1024);
	}

	/**
	 * 获取 KeyPair
	 *
	 * @param keySize key size
	 * @return KeyPair
	 */
	public static KeyPair genKeyPair(int keySize) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
			// 密钥位数
			keyPairGen.initialize(keySize);
			// 密钥对
			return new KeyPair(keyPairGen.generateKeyPair());
		} catch (NoSuchAlgorithmException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 生成RSA私钥
	 *
	 * @param modulus  N特征值
	 * @param exponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(String modulus, String exponent) {
		return generatePrivateKey(new BigInteger(modulus), new BigInteger(exponent));
	}

	/**
	 * 生成RSA私钥
	 *
	 * @param modulus  N特征值
	 * @param exponent d特征值
	 * @return {@link PrivateKey}
	 */
	public static PrivateKey generatePrivateKey(BigInteger modulus, BigInteger exponent) {
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 生成RSA公钥
	 *
	 * @param modulus  N特征值
	 * @param exponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(String modulus, String exponent) {
		return generatePublicKey(new BigInteger(modulus), new BigInteger(exponent));
	}

	/**
	 * 生成RSA公钥
	 *
	 * @param modulus  N特征值
	 * @param exponent e特征值
	 * @return {@link PublicKey}
	 */
	public static PublicKey generatePublicKey(BigInteger modulus, BigInteger exponent) {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 得到公钥
	 *
	 * @param base64PubKey 密钥字符串（经过base64编码）
	 * @return PublicKey
	 */
	public static PublicKey getPublicKey(String base64PubKey) {
		Objects.requireNonNull(base64PubKey, "base64 public key is null.");
		byte[] keyBytes = Base64Utils.decodeFromString(base64PubKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 得到公钥字符串
	 *
	 * @param base64PubKey 密钥字符串（经过base64编码）
	 * @return PublicKey String
	 */
	public static String getPublicKeyToBase64(String base64PubKey) {
		PublicKey publicKey = getPublicKey(base64PubKey);
		return getKeyString(publicKey);
	}

	/**
	 * 得到私钥
	 *
	 * @param base64PriKey 密钥字符串（经过base64编码）
	 * @return PrivateKey
	 */
	public static PrivateKey getPrivateKey(String base64PriKey) {
		Objects.requireNonNull(base64PriKey, "base64 private key is null.");
		byte[] keyBytes = Base64Utils.decodeFromString(base64PriKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 得到密钥字符串（经过base64编码）
	 *
	 * @param key key
	 * @return base 64 编码后的 key
	 */
	public static String getKeyString(Key key) {
		return Base64Utils.encodeToString(key.getEncoded());
	}

	/**
	 * 得到私钥 base64
	 *
	 * @param base64PriKey 密钥字符串（经过base64编码）
	 * @return PrivateKey String
	 */
	public static String getPrivateKeyToBase64(String base64PriKey) {
		PrivateKey privateKey = getPrivateKey(base64PriKey);
		return getKeyString(privateKey);
	}

	/**
	 * 共要加密
	 *
	 * @param base64PublicKey base64 的公钥
	 * @param data            待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encrypt(String base64PublicKey, byte[] data) {
		return encrypt(getPublicKey(base64PublicKey), data);
	}

	/**
	 * 共要加密
	 *
	 * @param publicKey 公钥
	 * @param data      待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		return rsa(publicKey, data, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 私钥加密，用于 qpp 内，公钥解密
	 *
	 * @param base64PrivateKey base64 的私钥
	 * @param data             待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encryptByPrivateKey(String base64PrivateKey, byte[] data) {
		return encryptByPrivateKey(getPrivateKey(base64PrivateKey), data);
	}

	/**
	 * 私钥加密，加密成 base64 字符串，用于 qpp 内，公钥解密
	 *
	 * @param base64PrivateKey base64 的私钥
	 * @param data             待加密的内容
	 * @return 加密后的内容
	 */
	public static String encryptByPrivateKeyToBase64(String base64PrivateKey, byte[] data) {
		return Base64Util.encodeToString(encryptByPrivateKey(base64PrivateKey, data));
	}

	/**
	 * 私钥加密，用于 qpp 内，公钥解密
	 *
	 * @param privateKey 私钥
	 * @param data       待加密的内容
	 * @return 加密后的内容
	 */
	public static byte[] encryptByPrivateKey(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, data, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 公钥加密
	 *
	 * @param publicKey PublicKey
	 * @param data      待加密的内容
	 * @return 加密后的内容
	 */
	@Nullable
	public static String encryptToBase64(PublicKey publicKey, @Nullable String data) {
		if (StringUtil.isBlank(data)) {
			return null;
		}
		return Base64Utils.encodeToString(encrypt(publicKey, data.getBytes(Charsets.UTF_8)));
	}

	/**
	 * 公钥加密
	 *
	 * @param base64PublicKey base64 公钥
	 * @param data            待加密的内容
	 * @return 加密后的内容
	 */
	@Nullable
	public static String encryptToBase64(String base64PublicKey, @Nullable String data) {
		return encryptToBase64(getPublicKey(base64PublicKey), data);
	}

	/**
	 * 解密
	 *
	 * @param base64PrivateKey base64 私钥
	 * @param data             数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(String base64PrivateKey, byte[] data) {
		return decrypt(getPrivateKey(base64PrivateKey), data);
	}

	/**
	 * 解密
	 *
	 * @param base64publicKey base64 公钥
	 * @param data            数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKey(String base64publicKey, byte[] data) {
		return decryptByPublicKey(getPublicKey(base64publicKey), data);
	}

	/**
	 * 解密
	 *
	 * @param privateKey privateKey
	 * @param data       数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		return rsa(privateKey, data, Cipher.DECRYPT_MODE);
	}

	/**
	 * 解密
	 *
	 * @param publicKey PublicKey
	 * @param data      数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKey(PublicKey publicKey, byte[] data) {
		return rsa(publicKey, data, Cipher.DECRYPT_MODE);
	}

	/**
	 * rsa 加、解密
	 *
	 * @param key  key
	 * @param data 数据
	 * @param mode 模式
	 * @return 解密后的数据
	 */
	private static byte[] rsa(Key key, byte[] data, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_PADDING);
			cipher.init(mode, key);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * base64 数据解密
	 *
	 * @param publicKey  PublicKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKeyFromBase64(PublicKey publicKey, byte[] base64Data) {
		return decryptByPublicKey(publicKey, Base64Utils.decode(base64Data));
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PublicKey base64 公钥
	 * @param base64Data      base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptByPublicKeyFromBase64(String base64PublicKey, byte[] base64Data) {
		return decryptByPublicKeyFromBase64(getPublicKey(base64PublicKey), base64Data);
	}

	/**
	 * base64 数据解密
	 *
	 * @param privateKey PrivateKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptFromBase64(PrivateKey privateKey, @Nullable String base64Data) {
		if (StringUtil.isBlank(base64Data)) {
			return null;
		}
		return new String(decrypt(privateKey, Base64Utils.decodeFromString(base64Data)),
			Charsets.UTF_8);
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PrivateKey base64 私钥
	 * @param base64Data       base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptFromBase64(String base64PrivateKey, @Nullable String base64Data) {
		return decryptFromBase64(getPrivateKey(base64PrivateKey), base64Data);
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PrivateKey base64 私钥
	 * @param base64Data       base64数据
	 * @return 解密后的数据
	 */
	public static byte[] decryptFromBase64(String base64PrivateKey, byte[] base64Data) {
		return decrypt(base64PrivateKey, Base64Utils.decode(base64Data));
	}

	/**
	 * base64 数据解密
	 *
	 * @param publicKey  PublicKey
	 * @param base64Data base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptByPublicKeyFromBase64(PublicKey publicKey,
		@Nullable String base64Data) {
		if (StringUtil.isBlank(base64Data)) {
			return null;
		}
		return new String(decryptByPublicKey(publicKey, Base64Utils.decodeFromString(base64Data)),
			Charsets.UTF_8);
	}

	/**
	 * base64 数据解密
	 *
	 * @param base64PublicKey base64 公钥
	 * @param base64Data      base64数据
	 * @return 解密后的数据
	 */
	@Nullable
	public static String decryptByPublicKeyFromBase64(String base64PublicKey,
		@Nullable String base64Data) {
		return decryptByPublicKeyFromBase64(getPublicKey(base64PublicKey), base64Data);
	}
}
