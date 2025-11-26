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

package com.taotao.cloud.shortlink.biz.web.utils;

import com.google.common.hash.Hashing;
import com.taotao.boot.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import com.taotao.boot.common.utils.lang.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Slf4j
public class CommonBizUtil {

    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String CHARS_OUT_ORDER = "qwertyuiopmnbvcxzlkjhgfdsaMNBVCXZASDFGHJKLPOIUYTREWQ0987654321";

    /** MD5加密 */
    public static Optional<String> MD5(String str) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }

            return Optional.of(sb.toString().toUpperCase());
        } catch (Exception e) {
            log.warn("MD5 error: {}", e.toString());
        }

        return Optional.empty();
    }

    /**
     * 判断 - CommonResponse是否success
     *
     * @param response @{@link CommonResponse}
     * @return 布尔值
     */
    public static boolean isSuccessResponse(CommonResponse<?> response) {
        return Objects.nonNull(response) && response.isSuccess() && Objects.equals(response.getCode(), 200);
    }

    public static void throwBizError(ErrorCode errorCode, String msg) {
        String msgExt = StringUtils.trimToNull(msg) == null ? errorCode.getMsg() : errorCode.getMsg() + ":" + msg;
        throw new BusinessException(errorCode.getCode(), msgExt);
    }

    public static void throwBizError(ErrorCode errorCode) {
        throw new BusinessException(errorCode.getCode(), errorCode.getMsg());
    }

    public static long murmurHash32(String param) {
        return Hashing.murmur3_32_fixed().hashUnencodedChars(param).padToLong();
    }

    public static String encodeToBase62(long num) {
        // TODO StringBuilder导致线程安全问题。从而code冲突
        StringBuffer stringBuffer = new StringBuffer();
        do {
            int i = (int) (num % 62);
            stringBuffer.append(CHARS.charAt(i));
            num = num / 32;
        } while (num > 0);

        return stringBuffer.toString();
    }

    public static String encodeToBase62OutOrder(long num) {
        StringBuilder stringBuilder = new StringBuilder();
        do {
            int i = (int) (num % 62);
            stringBuilder.append(CHARS_OUT_ORDER.charAt(i));
            num = num / 32;
        } while (num > 0);

        return stringBuilder.toString();
    }

    /**
     * 确保list不为null
     *
     * @param list @{@link List}
     * @param <T> 泛型
     * @return @{@link List}
     */
    public static <T> List<T> notNullList(List<T> list) {
        return Optional.ofNullable(list).orElse(Collections.emptyList());
    }

    public static void main(String[] args) {
        long l = murmurHash32("/origin/userId=12");
        LogUtils.info(l);
    }

    private CommonBizUtil() {}
}
