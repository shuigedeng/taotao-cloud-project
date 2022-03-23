package com.taotao.cloud.common.support.reader.impl;

import com.taotao.cloud.common.support.reader.IReader;
import java.io.File;

/**
 * 读取工具类
 */
public final class Readers {

    private Readers(){}

    /**
     * 获取文本字符串
     * @param string 文本字符串
     * @return 实现
     */
    public static IReader string(final String string) {
        return new StringReader(string);
    }

    /**
     * 获取文本字符串
     * @param filePath 文件路径
     * @param charset 文件编码
     * @return 实现
     */
    public static IReader filePath(final String filePath,
                                   final String charset) {
        return new FilePathReader(filePath, charset);
    }

    /**
     * 获取文本字符串
     * @param file 文件
     * @param charset 文件编码
     * @return 实现
     */
    public static IReader file(final File file,
                                   final String charset) {
        return new FileReader(file, charset);
    }

}
