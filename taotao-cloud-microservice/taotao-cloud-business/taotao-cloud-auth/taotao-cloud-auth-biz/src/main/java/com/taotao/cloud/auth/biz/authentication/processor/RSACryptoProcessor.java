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

import com.taotao.boot.security.spring.constants.SymbolConstants;
import com.taotao.cloud.auth.biz.management.entity.SecretKey;
import org.apache.commons.lang3.ArrayUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>RSA 加密算法处理器 </p>
 *
 *
 * @since : 2022/5/1 19:31
 */
public class RSACryptoProcessor implements AsymmetricCryptoProcessor {

    private static final Logger log = LoggerFactory.getLogger(RSACryptoProcessor.class);

    private static final String PKCS8_PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";
    private static final String PKCS8_PUBLIC_KEY_END = "-----END PUBLIC KEY-----";

    @Override
    public SecretKey createSecretKey() {
        RSA rsa = SecureUtil.rsa();
        SecretKey secretKey = new SecretKey();
        secretKey.setPrivateKey(rsa.getPrivateKeyBase64());
        secretKey.setPublicKey(appendPkcs8Padding(rsa.getPublicKeyBase64()));
        return secretKey;
    }

    /**
     * 去除 RSA Pkcs8Padding 中的标记格式 '-----BEGIN PUBLIC KEY-----' 和 '-----END PUBLIC KEY-----'，以及 '\n'
     *
     * @param key RSA Key
     * @return 清楚格式后的 RSA KEY
     */
    private String removePkcs8Padding(String key) {
        String result = StringUtils.replace(key, SymbolConstants.NEW_LINE, SymbolConstants.BLANK);
        String[] values = StringUtils.split(result, "-----");
        if (ArrayUtils.isNotEmpty(values)) {
            return values[1];
        }
        return key;
    }

    /**
     * 将 RSA PublicKey 转换为 Pkcs8Padding 格式。
     *
     * @param key RSA PublicKey
     * @return 转换为 Pkcs8Padding 格式的 RSA PublicKey
     */
    public String appendPkcs8Padding(String key) {
        return PKCS8_PUBLIC_KEY_BEGIN
                + SymbolConstants.NEW_LINE
                + key
                + SymbolConstants.NEW_LINE
                + PKCS8_PUBLIC_KEY_END;
    }

    @Override
    public String decrypt(String content, String privateKey) {
        byte[] base64Data = Base64.decode(content);
        RSA rsa = SecureUtil.rsa(privateKey, null);
        String result = StrUtil.utf8Str(rsa.decrypt(base64Data, KeyType.PrivateKey));
        log.info("RSA crypto decrypt data, value is : [{}]", result);
        return result;
    }

    @Override
    public String encrypt(String content, String publicKey) {
        // 去除前端 RSA PublicKey中的 '-----BEGIN PUBLIC KEY-----'格式
        String key = removePkcs8Padding(publicKey);
        RSA rsa = SecureUtil.rsa(null, key);
        byte[] encryptedData = rsa.encrypt(content, KeyType.PublicKey);
        String result = Base64.encode(encryptedData);
        log.info("RSA crypto decrypt data, value is : [{}]", result);
        return result;
    }
}
