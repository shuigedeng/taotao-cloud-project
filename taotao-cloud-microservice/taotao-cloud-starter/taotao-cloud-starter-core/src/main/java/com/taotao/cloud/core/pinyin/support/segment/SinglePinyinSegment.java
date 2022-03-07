package com.taotao.cloud.core.pinyin.support.segment;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.pinyin.spi.IPinyinSegment;
import java.util.Collections;
import java.util.List;

/**
 * 单个字符的分词实现
 *
 * （1）为了保证 api 的统一性
 */
@ThreadSafe
public class SinglePinyinSegment implements IPinyinSegment {

    @Override
    public List<String> segment(String string) {
        return Collections.singletonList(string);
    }

}
