package com.taotao.cloud.common.utils.pinyin.support.segment;


import com.taotao.cloud.common.utils.pinyin.spi.IPinyinSegment;
import java.util.Collections;
import java.util.List;

/**
 * 单个字符的分词实现
 *
 * （1）为了保证 api 的统一性
 */
public class SinglePinyinSegment implements IPinyinSegment {

    @Override
    public List<String> segment(String string) {
        return Collections.singletonList(string);
    }

}
