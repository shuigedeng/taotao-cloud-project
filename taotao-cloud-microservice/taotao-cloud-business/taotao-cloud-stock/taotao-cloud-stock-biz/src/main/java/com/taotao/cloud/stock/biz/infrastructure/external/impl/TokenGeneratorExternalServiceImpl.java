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

package com.taotao.cloud.stock.biz.infrastructure.external.impl;

import com.taotao.cloud.stock.biz.infrastructure.external.TokenGeneratorExternalService;
import com.xtoon.boot.sys.domain.external.TokenGeneratorExternalService;
import java.security.MessageDigest;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * 生成Token外部实现类
 *
 * @author shuigedeng
 * @since 2021-04-23
 */
@Component
public class TokenGeneratorExternalServiceImpl implements TokenGeneratorExternalService {

    @Override
    public String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            throw new RuntimeException("生成Token失败", e);
        }
    }
}
