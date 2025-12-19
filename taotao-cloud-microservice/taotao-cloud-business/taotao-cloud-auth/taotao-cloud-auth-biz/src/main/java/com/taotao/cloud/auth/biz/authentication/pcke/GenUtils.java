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

package com.taotao.cloud.auth.biz.authentication.pcke;

import com.taotao.boot.common.utils.log.LogUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

/**
 * GenUtils
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class GenUtils {

    public static String genCode() throws NoSuchAlgorithmException {
        // 生成随机字符串
        StringKeyGenerator authorizationCodeGenerator =
                new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);
        String codeVerifier = authorizationCodeGenerator.generateKey();
        LogUtils.info(codeVerifier);
        LogUtils.info("length: {}", codeVerifier.length());
        // 加密并再次编码
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
        String codeChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        LogUtils.info(codeChallenge);
        return codeChallenge;
    }

    public static void main( String[] args ) throws NoSuchAlgorithmException {
        genCode();
    }
}
