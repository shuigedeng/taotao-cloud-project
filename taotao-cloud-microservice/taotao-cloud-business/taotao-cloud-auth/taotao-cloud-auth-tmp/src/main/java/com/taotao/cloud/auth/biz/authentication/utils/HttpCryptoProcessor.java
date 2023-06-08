/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.utils;

import com.taotao.cloud.auth.biz.management.entity.SecretKey;
import com.taotao.cloud.auth.biz.utils.SessionInvalidException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.data.id.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * <p>Description: 接口加密解密处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/10/4 14:29
 */
public class HttpCryptoProcessor {

	private static final Logger log = LoggerFactory.getLogger(HttpCryptoProcessor.class);

	private final AsymmetricCryptoProcessor asymmetricCryptoProcessor;

	private final SymmetricCryptoProcessor symmetricCryptoProcessor;

	public HttpCryptoProcessor(AsymmetricCryptoProcessor asymmetricCryptoProcessor, SymmetricCryptoProcessor symmetricCryptoProcessor) {
//		super(RestConstants.CACHE_NAME_TOKEN_SECURE_KEY);
		this.asymmetricCryptoProcessor = asymmetricCryptoProcessor;
		this.symmetricCryptoProcessor = symmetricCryptoProcessor;
	}

	public String encrypt(String identity, String content)  {
//		try {
//			SecretKey secretKey = getSecretKey(identity);
//			String result = symmetricCryptoProcessor.encrypt(content, secretKey.getSymmetricKey());
//			log.debug("[Herodotus] |- Encrypt content from [{}] to [{}].", content, result);
//			return result;
//		} catch (StampHasExpiredException e) {
//			log.warn("[Herodotus] |- Session has expired, need recreate.");
//			throw new SessionInvalidException();
//		} catch (Exception e) {
//			log.warn("[Herodotus] |- Symmetric can not Encrypt content [{}], Skip!", content);
//			return content;
//		}
		return null;
	}

	public String decrypt(String identity, String content)  {
//		try {
//			SecretKey secretKey = getSecretKey(identity);
//
//			String result = symmetricCryptoProcessor.decrypt(content, secretKey.getSymmetricKey());
//			log.debug("[Herodotus] |- Decrypt content from [{}] to [{}].", content, result);
//			return result;
//		} catch (StampHasExpiredException e) {
//			log.warn("[Herodotus] |- Session has expired, need recreate.");
//			throw new SessionInvalidException();
//		} catch (Exception e) {
//			log.warn("[Herodotus] |- Symmetric can not Decrypt content [{}], Skip!", content);
//			return content;
//		}
		return null;
	}

	/**
	 * 根据SessionId创建SecretKey {@link SecretKey}。如果前端有可以唯一确定的SessionId，并且使用该值，则用该值创建SecretKey。否则就由后端动态生成一个SessionId。
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

		// 根据Token的有效时间设置
		Duration expire = getExpire(accessTokenValiditySeconds);
//		return this.create(identity, expire);
		return null;
	}

	public SecretKey nextStamp(String key) {
		SecretKey secretKey = asymmetricCryptoProcessor.createSecretKey();
		String symmetricKey = symmetricCryptoProcessor.createKey();
		secretKey.setSymmetricKey(symmetricKey);
		secretKey.setIdentity(key);
		secretKey.setState(IdUtil.fastUUID());

		log.debug("[Herodotus] |- Generate secret key, value is : [{}]", secretKey);
		return secretKey;
	}

	private boolean isSessionValid(String identity) {
//		return this.containKey(identity);
		return false;
	}

	private SecretKey getSecretKey(String identity)  {
//		if (isSessionValid(identity)) {
//			SecretKey secretKey = this.get(identity);
//			if (ObjectUtils.isNotEmpty(secretKey)) {
//				log.trace("[Herodotus] |- Decrypt Or Encrypt content use param identity [{}], cached identity is [{}].", identity, secretKey.getIdentity());
//				return secretKey;
//			}
//		}
//
//		throw new StampHasExpiredException("SecretKey key is expired!");
		return null;
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
		log.debug("[Herodotus] |- Decrypt frontend public key, value is : [{}]", frontendPublicKey);
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
		log.debug("[Herodotus] |- Encrypt symmetric key use frontend public key, value is : [{}]", encryptedAesKey);
		return encryptedAesKey;
	}

	/**
	 * 前端获取后端生成 AES Key
	 *
	 * @param identity     Session ID
	 * @param confidential 前端和后端加解密结果都
	 * @return 前端 PublicKey 加密后的 AES KEY
	 * @throws SessionInvalidException sessionId不可用，无法从缓存中找到对应的值
	 */
	public String exchange(String identity, String confidential)  {
		SecretKey secretKey = getSecretKey(identity);
		String frontendPublicKey = decryptFrontendPublicKey(confidential, secretKey.getPrivateKey());
		return encryptBackendKey(secretKey.getSymmetricKey(), frontendPublicKey);
	}

	public void afterPropertiesSet() throws Exception {

	}
}
