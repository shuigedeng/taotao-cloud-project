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

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import com.taotao.cloud.crypto.ext.processor.AsymmetricCryptoProcessor;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 国密 SM2 算法处理 </p>
 * <p>
 * 主要用于前后端数据加密处理，与 sm-crypto 交互
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:30:05
 */
public class SM2CryptoProcessor implements AsymmetricCryptoProcessor {

    private static final Logger log = LoggerFactory.getLogger(SM2CryptoProcessor.class);

    private static final String FLAG = "04";

    @Override
    public SecretKey createSecretKey() {
        // 随机生成秘钥
        SM2 sm2 = SmUtil.sm2();
        // sm2的加解密时有两种方式即 C1C2C3、 C1C3C2，
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        // 生成私钥
        String privateKey = HexUtil.encodeHexStr(BCUtil.encodeECPrivateKey(sm2.getPrivateKey()));
        // 生成公钥
        String publicKey = HexUtil.encodeHexStr(((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false));

        SecretKey secretKey = new SecretKey();
        secretKey.setPrivateKey(privateKey);
        secretKey.setPublicKey(publicKey);
        return secretKey;
    }

    @Override
    public String decrypt(String content, String privateKey) {
        // 可用的 Hutool SM2 解密
        SM2 sm2 = SmUtil.sm2(privateKey, null);
        sm2.setMode(SM2Engine.Mode.C1C3C2);

        String result = StrUtil.utf8Str(sm2.decryptFromBcd(content, KeyType.PrivateKey));
        log.debug("SM2 crypto decrypt data, value is : [{}]", result);

        return result;
    }

    @Override
    public String encrypt(String content, String publicKey) {
        SM2 sm2 = SmUtil.sm2(null, publicKey);

        String result = sm2.encryptBcd(content, KeyType.PublicKey);
        log.debug("SM2 crypto encrypt data, value is : [{}]", result);
        return result;
    }

}
