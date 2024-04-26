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

import java.util.Map;
import java.util.TreeMap;

public class SignUtil {
    private static String charset = "utf8";

    public static String getSign(Map<String, Object> data, String secret) {
        if (data == null) {
            return null;
        }
        // 排序参数
        Map<String, Object> mappingList = new TreeMap<>(data);
        StringBuilder plainText = new StringBuilder();
        mappingList.forEach((k, v) -> {
            if (!"sign".equals(k) && !BaseUtils.isEmpty(v)) {
                plainText.append(String.format("%s=%s&", k, v));
            }
        });
        String substring = plainText.substring(0, plainText.length() - 1);
        return Md5Util.MD5Encode(substring + secret, charset);
    }
}
