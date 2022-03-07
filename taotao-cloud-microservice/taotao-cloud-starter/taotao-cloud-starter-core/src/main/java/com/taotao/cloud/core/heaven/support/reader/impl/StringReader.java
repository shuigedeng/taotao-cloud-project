package com.taotao.cloud.core.heaven.support.reader.impl;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.support.reader.IReader;

/**
 * 字符串读取类
 *
 * <p> project: heaven-StringReader </p>
 * <p> create on 2020/3/19 21:43 </p>
 *
 */
@ThreadSafe
public class StringReader implements IReader {

    /**
     * 字符串内容
     * @since 0.1.94
     */
    private final String string;

    public StringReader(String string) {
        this.string = string;
    }


    @Override
    public String read() {
        return this.string;
    }

}
