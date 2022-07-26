package com.taotao.cloud.open.common.util;

import cn.hutool.crypto.SecureUtil;
import com.taotao.cloud.open.common.model.KeyPair;


/**
 * 非对称加密工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:21
 */
public class AsymmetricCryUtil {

	/**
	 * 生成SM2的公私密钥
	 *
	 * @return 密钥对
	 */
	public static KeyPair generateSM2Keys() {
		java.security.KeyPair pair = SecureUtil.generateKeyPair("SM2");
		String privateKey = Base64Util.bytesToBase64(pair.getPrivate().getEncoded());
		String publicKey = Base64Util.bytesToBase64(pair.getPublic().getEncoded());
		return new KeyPair(privateKey, publicKey);
	}

	/**
	 * 生成RSA的公私密钥
	 *
	 * @return 密钥对
	 */
	public static KeyPair generateRSAKeys() {
		//生成公私钥对
		java.security.KeyPair pair = SecureUtil.generateKeyPair("RSA");
		String privateKey = Base64Util.bytesToBase64(pair.getPrivate().getEncoded());
		String publicKey = Base64Util.bytesToBase64(pair.getPublic().getEncoded());
		return new KeyPair(privateKey, publicKey);
	}
}
