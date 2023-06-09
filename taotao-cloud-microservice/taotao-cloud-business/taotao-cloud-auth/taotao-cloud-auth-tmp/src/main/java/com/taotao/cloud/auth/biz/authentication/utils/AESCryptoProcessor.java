/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
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
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.utils;

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.dromara.hutool.crypto.symmetric.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: AES 加密算法处理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/5/2 16:56
 */
public class AESCryptoProcessor implements SymmetricCryptoProcessor {

    private static final Logger log = LoggerFactory.getLogger(AESCryptoProcessor.class);

    @Override
    public String createKey() {
        return RandomUtil.randomStringUpper(16);
    }

    @Override
    public String decrypt(String data, String key) {
        AES aes = SecureUtil.aes(ByteUtil.toUtf8Bytes(key));
        byte[] result = aes.decrypt(Base64.decode(ByteUtil.toUtf8Bytes(data)));
        log.trace("[Herodotus] |- AES crypto decrypt data, value is : [{}]", result);
        return StrUtil.utf8Str(result);
    }

    @Override
    public String encrypt(String data, String key) {
        AES aes = SecureUtil.aes(ByteUtil.toUtf8Bytes(key));
        byte[] result = aes.encrypt(ByteUtil.toUtf8Bytes(data));
        log.trace("[Herodotus] |- AES crypto encrypt data, value is : [{}]", result);
        return StrUtil.utf8Str(result);
    }
}
