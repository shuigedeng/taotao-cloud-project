package com.taotao.cloud.common.utils.net;


import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.common.ArgUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * URL 工具类
 */
public final class URLUtil {

    private URLUtil(){}

    /**
     * 读取每一行的内容
     * @param url url 信息
     * @return 结果
     * @since 0.1.108
     */
    public static List<String> readAllLines(final URL url) {
        return readAllLines(url, CommonConstant.UTF8);
    }

    /**
     * 读取每一行的内容
     * @param url url 信息
     * @param charset 文件编码
     * @return 结果
     * @since 0.1.108
     */
    public static List<String> readAllLines(final URL url,
                                     final String charset) {
        ArgUtil.notNull(url, "url");
        ArgUtil.notEmpty(charset, "charset");

        List<String> resultList = new ArrayList<>();

        try(InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName(charset)))) {
            // 按行读取信息
            String line;
            while ((line = br.readLine()) != null) {
                resultList.add(line);
            }
        } catch (IOException e) {
            throw new CommonRuntimeException(e);
        }
        return resultList;
    }

}
