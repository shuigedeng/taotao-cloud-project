package com.taotao.cloud.common.support.reader.impl;


import com.taotao.cloud.common.support.reader.IReader;

/**
 * 字符串读取类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:11:33
 */
public class StringReader implements IReader {

    /**
     * 字符串内容
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
