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

package com.taotao.cloud.auth.infrastructure.extension.oneClick.mobtech.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public abstract class Base64Utils {
    private static final Charset DEFAULT_CHARSET;

    public Base64Utils() {}

    public static byte[] encode(byte[] src) {
        return src.length == 0 ? src : Base64.getEncoder().encode(src);
    }

    public static byte[] decode(byte[] src) {
        return src.length == 0 ? src : Base64.getDecoder().decode(src);
    }

    public static byte[] encodeUrlSafe(byte[] src) {
        return src.length == 0 ? src : Base64.getUrlEncoder().encode(src);
    }

    public static byte[] decodeUrlSafe(byte[] src) {
        return src.length == 0 ? src : Base64.getUrlDecoder().decode(src);
    }

    public static String encodeToString(byte[] src) {
        return src.length == 0 ? "" : new String(encode(src), DEFAULT_CHARSET);
    }

    public static byte[] decodeFromString(String src) {
        return src.isEmpty() ? new byte[0] : decode(src.getBytes(DEFAULT_CHARSET));
    }

    public static String encodeToUrlSafeString(byte[] src) {
        return new String(encodeUrlSafe(src), DEFAULT_CHARSET);
    }

    public static byte[] decodeFromUrlSafeString(String src) {
        return decodeUrlSafe(src.getBytes(DEFAULT_CHARSET));
    }

    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
    }
}
