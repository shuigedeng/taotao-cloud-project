package com.taotao.cloud.pinyin.spi;

import java.util.List;

/**
 * 拼音分词接口
 */
public interface IPinyinSegment {

    /**
     * 对字符串执行分词
     * @param string 原始字符串
     * @return 分词结果
     */
    List<String> segment(final String string);

}
