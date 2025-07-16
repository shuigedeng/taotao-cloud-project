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

package com.taotao.cloud.auth.biz.authentication.processor;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.captcha.support.core.definition.AbstractRenderer;
import com.taotao.boot.captcha.support.core.definition.domain.Metadata;
import com.taotao.boot.captcha.support.core.dto.Captcha;
import com.taotao.boot.captcha.support.core.dto.Verification;
import com.taotao.cloud.auth.biz.exception.SessionInvalidException;
import com.taotao.cloud.auth.biz.exception.StampHasExpiredException;
import com.taotao.cloud.auth.biz.management.entity.SecretKey;
import java.time.Duration;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hutool.core.data.id.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>接口加密解密处理器 </p>
 *
 *
 * @since : 2021/10/4 14:29
 */
public class HttpCryptoProcessor extends AbstractRenderer {

    private static final Logger log = LoggerFactory.getLogger(HttpCryptoProcessor.class);

    private final AsymmetricCryptoProcessor asymmetricCryptoProcessor;

    private final SymmetricCryptoProcessor symmetricCryptoProcessor;

    public HttpCryptoProcessor(
            RedisRepository redisRepository,
            AsymmetricCryptoProcessor asymmetricCryptoProcessor,
            SymmetricCryptoProcessor symmetricCryptoProcessor) {
        super(redisRepository, RestConstants.CACHE_NAME_TOKEN_SECURE_KEY);
        this.asymmetricCryptoProcessor = asymmetricCryptoProcessor;
        this.symmetricCryptoProcessor = symmetricCryptoProcessor;
    }

    public HttpCryptoProcessor(
            RedisRepository redisRepository,
            Duration expire,
            AsymmetricCryptoProcessor asymmetricCryptoProcessor,
            SymmetricCryptoProcessor symmetricCryptoProcessor) {
        super(redisRepository, RestConstants.CACHE_NAME_TOKEN_SECURE_KEY, expire);
        this.asymmetricCryptoProcessor = asymmetricCryptoProcessor;
        this.symmetricCryptoProcessor = symmetricCryptoProcessor;
    }

    public String encrypt(String identity, String content) {
        try {
            SecretKey secretKey = getSecretKey(identity);
            String result = symmetricCryptoProcessor.encrypt(content, secretKey.getSymmetricKey());
            log.info("Encrypt content from [{}] to [{}].", content, result);
            return result;
        } catch (StampHasExpiredException e) {
            log.info("Session has expired, need recreate.");
            throw new SessionInvalidException();
        } catch (Exception e) {
            log.info("Symmetric can not Encrypt content [{}], Skip!", content);
            return content;
        }
    }

    public String decrypt(String identity, String content) {
        try {
            SecretKey secretKey = getSecretKey(identity);

            String result = symmetricCryptoProcessor.decrypt(content, secretKey.getSymmetricKey());
            log.info("Decrypt content from [{}] to [{}].", content, result);
            return result;
        } catch (StampHasExpiredException e) {
            log.info("Session has expired, need recreate.");
            throw new SessionInvalidException();
        } catch (Exception e) {
            log.info("Symmetric can not Decrypt content [{}], Skip!", content);
            return content;
        }
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
        return (SecretKey) this.create(identity, expire);
    }

    @Override
    public Metadata draw() {
        return null;
    }

    @Override
    public Captcha getCapcha(String key) {
        return null;
    }

    @Override
    public boolean verify(Verification verification) {
        return false;
    }

    @Override
    public String getCategory() {
        return null;
    }

    public SecretKey nextStamp(String key) {
        SecretKey secretKey = asymmetricCryptoProcessor.createSecretKey();
        String symmetricKey = symmetricCryptoProcessor.createKey();
        secretKey.setSymmetricKey(symmetricKey);
        secretKey.setIdentity(key);
        secretKey.setState(IdUtil.fastUUID());

        log.info("Generate secret key, value is : [{}]", secretKey);
        return secretKey;
    }

    private boolean isSessionValid(String identity) {
        return this.containKey(identity);
    }

    private SecretKey getSecretKey(String identity) {
        if (isSessionValid(identity)) {
            SecretKey secretKey = (SecretKey) this.get(identity);
            if (ObjectUtils.isNotEmpty(secretKey)) {
                log.info(
                        "Decrypt Or Encrypt content use param identity [{}], cached identity is [{}].",
                        identity,
                        secretKey.getIdentity());
                return secretKey;
            }
        }

        throw new StampHasExpiredException("SecretKey key is expired!");
    }

    private Duration getExpire(Duration accessTokenValiditySeconds) {
        if (ObjectUtils.isEmpty(accessTokenValiditySeconds)
                || accessTokenValiditySeconds.isZero()) {
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
        log.info("Decrypt frontend public key, value is : [{}]", frontendPublicKey);
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
        log.info("Encrypt symmetric key use frontend public key, value is : [{}]", encryptedAesKey);
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
    public String exchange(String identity, String confidential) {
        SecretKey secretKey = getSecretKey(identity);
        String frontendPublicKey =
                decryptFrontendPublicKey(confidential, secretKey.getPrivateKey());
        return encryptBackendKey(secretKey.getSymmetricKey(), frontendPublicKey);
    }

    public void afterPropertiesSet() throws Exception {}
}
