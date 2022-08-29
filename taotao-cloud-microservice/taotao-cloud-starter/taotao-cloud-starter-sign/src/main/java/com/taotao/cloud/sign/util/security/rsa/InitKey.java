package com.taotao.cloud.sign.util.security.rsa;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sign.exception.EncryptDtguaiException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 初始化rsa key
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:43:47
 */
public class InitKey {

	/**
	 * 公钥
	 */
	public static final String PUBLIC_KEY = "pub";

	/**
	 * 私钥
	 */
	public static final String PRIVATE_KEY = "pri";

	/**
	 * 初始化密钥对
	 *
	 * @return Map 密钥初始化
	 */
	public static Map<String, Object> initKey(String rsaKey) {
		//实例化密钥生成器
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(RsaUtil.KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			LogUtils.error("rsa初始化密钥对异常", e);
			throw new EncryptDtguaiException("rsa初始化密钥对异常");
		}
		//初始化密钥生成器
		SecureRandom secureRandom = new SecureRandom(
			Optional.ofNullable(rsaKey)
				.map(String::getBytes)
				.orElseThrow(() -> new EncryptDtguaiException("rsa秘钥初始化失败,rsaKey:" + rsaKey))
		);
		keyPairGenerator.initialize(RsaUtil.KEY_SIZE, secureRandom);
		//生成密钥对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		//甲方公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		//甲方私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		//将密钥存储在map中
		Map<String, Object> keyMap = new HashMap<>(4);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;

	}

	/**
	 * 取得私钥
	 *
	 * @param keyMap 密钥map
	 * @return byte[] 私钥
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/**
	 * 取得公钥
	 *
	 * @param keyMap 密钥map
	 * @return byte[] 公钥
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}
}
