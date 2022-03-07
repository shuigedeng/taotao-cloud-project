package com.taotao.cloud.core.heaven.support.reader.impl;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.constant.CharsetConst;
import com.taotao.cloud.core.heaven.support.reader.IReader;
import com.taotao.cloud.core.heaven.util.io.FileUtil;

/**
 * 文件路径阅读者
 * <p> project: heaven-StringReader </p>
 * <p> create on 2020/3/19 21:43 </p>
 *
 */
@ThreadSafe
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
        this(path, CharsetConst.UTF8);
    }

    @Override
    public String read() {
        return FileUtil.getFileContent(path, charset);
    }

}
