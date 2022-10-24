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
package com.taotao.cloud.crypto.ext.processor;

import cn.hutool.core.util.IdUtil;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

/**
 * <p>Description: 接口加密解密处理器 </p>
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:29:58
 */
public class HttpCryptoProcessor {

	private static final Logger log = LoggerFactory.getLogger(HttpCryptoProcessor.class);

	private final AsymmetricCryptoProcessor asymmetricCryptoProcessor;

	private final SymmetricCryptoProcessor symmetricCryptoProcessor;
	@Autowired
	private RedisRepository redisRepository;

	public HttpCryptoProcessor(AsymmetricCryptoProcessor asymmetricCryptoProcessor,
							   SymmetricCryptoProcessor symmetricCryptoProcessor) {
		this.asymmetricCryptoProcessor = asymmetricCryptoProcessor;
		this.symmetricCryptoProcessor = symmetricCryptoProcessor;
	}

	public String encrypt(String identity, String content) {
		SecretKey secretKey = getSecretKey(identity);
		String result = symmetricCryptoProcessor.encrypt(content, secretKey.getSymmetricKey());
		log.debug("Encrypt content from [{}] to [{}].", content, result);
		return result;

	}

	public String decrypt(String identity, String content) {
		SecretKey secretKey = getSecretKey(identity);

		String result = symmetricCryptoProcessor.decrypt(content, secretKey.getSymmetricKey());
		log.debug("Decrypt content from [{}] to [{}].", content, result);
		return result;

	}

	/**
	 * 根据SessionId创建SecretKey
	 * {@link SecretKey}。如果前端有可以唯一确定的SessionId，并且使用该值，则用该值创建SecretKey。否则就由后端动态生成一个SessionId。
	 *
	 * @param identity                   SessionId，可以为空。
	 * @param accessTokenValiditySeconds Session过期时间，单位秒
	 * @return {@link SecretKey}
	 */
	public SecretKey createSecretKey(String identity, Duration accessTokenValiditySeconds) {
		// 前端如果设置sessionId，则由后端生成
		if (StringUtils.isBlank(identity)) {
			identity = IdUtil.fastUUID();
		}

		SecretKey secretKey = asymmetricCryptoProcessor.createSecretKey();
		String symmetricKey = symmetricCryptoProcessor.createKey();
		secretKey.setSymmetricKey(symmetricKey);
		secretKey.setIdentity(identity);

		log.debug("Generate secret key, value is : [{}]", secretKey);

		// 根据Token的有效时间设置
		Duration expire = getExpire(accessTokenValiditySeconds);
		redisRepository.insert(identity, expire.getSeconds(), secretKey);
		return secretKey;
	}

	private boolean isSessionValid(String identity) {
		return this.redisRepository.exists(identity);
	}

	private SecretKey getSecretKey(String identity) {
		if (isSessionValid(identity)) {
			SecretKey secretKey = (SecretKey) redisRepository.get(identity);
			if (ObjectUtils.isNotEmpty(secretKey)) {
				log.trace(
					"Decrypt Or Encrypt content use param identity [{}], cached identity is [{}].",
					identity, secretKey.getIdentity());
				return secretKey;
			}
		}

		throw new RuntimeException("SecretKey key is expired!");
	}

	private Duration getExpire(Duration accessTokenValiditySeconds) {
		if (ObjectUtils.isEmpty(accessTokenValiditySeconds) || accessTokenValiditySeconds.isZero()) {
			return Duration.ofHours(2L);
		} else {
			return accessTokenValiditySeconds;
		}
	}

	/**
	 * 用后端非对称加密算法私钥，解密前端传递过来的、用后端非对称加密算法公钥加密的前端非对称加密算法公钥
	 *
	 * @param privateKey 后端非对称加密算法私钥
	 * @param content    传回的已加密前端非对称加密算法公钥
	 * @return 前端非对称加密算法公钥
	 */
	private String decryptFrontendPublicKey(String content, String privateKey) {
		String frontendPublicKey = asymmetricCryptoProcessor.decrypt(content, privateKey);
		log.debug("Decrypt frontend public key, value is : [{}]", frontendPublicKey);
		return frontendPublicKey;
	}

	/**
	 * 用前端非对称加密算法公钥加密后端生成的对称加密算法 Key
	 *
	 * @param symmetricKey 对称算法秘钥
	 * @param publicKey    前端非对称加密算法公钥
	 * @return 用前端前端非对称加密算法公钥加密后的对称算法秘钥
	 */
	private String encryptBackendKey(String symmetricKey, String publicKey) {
		String encryptedAesKey = asymmetricCryptoProcessor.encrypt(symmetricKey, publicKey);
		log.debug("Encrypt symmetric key use frontend public key, value is : [{}]",
			encryptedAesKey);
		return encryptedAesKey;
	}

	/**
	 * 前端获取后端生成 AES Key
	 *
	 * @param identity     Session ID
	 * @param confidential 前端和后端加解密结果都
	 * @return 前端 PublicKey 加密后的 AES KEY
	 */
	public String exchange(String identity, String confidential) {
		SecretKey secretKey = getSecretKey(identity);
		String frontendPublicKey = decryptFrontendPublicKey(confidential,
			secretKey.getPrivateKey());
		return encryptBackendKey(secretKey.getSymmetricKey(), frontendPublicKey);
	}

}
