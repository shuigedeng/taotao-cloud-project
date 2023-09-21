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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * 流工具类
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class StreamUtils {
    private static final int DEFAULT_BUF_SIZE = 1024;

    public static String readToEnd(InputStream stream, Charset charset) throws IOException {
        requireNonNull(stream);
        requireNonNull(charset);

        final StringBuilder sb = new StringBuilder();
        final char[] buffer = new char[DEFAULT_BUF_SIZE];

        try (Reader in = new InputStreamReader(stream, charset)) {
            int charsRead = 0;
            while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
                sb.append(buffer, 0, charsRead);
            }
        }

        return sb.toString();
    }
}
