package com.taotao.cloud.common.support.reader.impl;


import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.support.reader.IReader;
import com.taotao.cloud.common.utils.io.FileUtil;

/**
 * 文件路径阅读者
 */
public class FilePathReader implements IReader {

    /**
     * 字符串内容
     * @since 0.1.94
     */
    private final String path;

    /**
     * 文件编码
     * @since 0.1.94
     */
    private final String charset;

    public FilePathReader(String path, String charset) {
        this.path = path;
        this.charset = charset;
    }

    public FilePathReader(String path) {
        this(path, CommonConstant.UTF8);
    }

    @Override
    public String read() {
        return FileUtil.getFileContent(path, charset);
    }

}
