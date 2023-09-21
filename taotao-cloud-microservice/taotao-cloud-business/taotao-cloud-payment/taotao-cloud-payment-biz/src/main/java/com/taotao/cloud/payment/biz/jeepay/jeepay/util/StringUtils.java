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

package com.taotao.cloud.payment.biz.jeepay.jeepay.util;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class StringUtils {

    private static Pattern whitespacePattern = Pattern.compile("\\s");

    public static boolean containsWhitespace(String str) {
        requireNonNull(str);
        return whitespacePattern.matcher(str).find();
    }

    public static String join(String separator, List<String> input) {

        if (input == null || input.size() <= 0) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));

            // if not the last item
            if (i != input.size() - 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String genUrl(String url, String uri) {
        if (!url.endsWith("/")) url += "/";
        return url += uri;
    }

    public static Boolean isEmpty(String str) {
        if (str == null) return true;
        return "".equals(str.trim());
    }
}
