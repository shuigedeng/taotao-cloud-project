package com.taotao.cloud.payment.biz.jeepay.jeepay.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import static java.util.Objects.requireNonNull;

/**
 * 流工具类
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:00
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
