package com.taotao.cloud.common.utils.pinyin.support.segment;


import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinSegment;

/**
 *
 */
public final class PinyinSegments {

    private PinyinSegments(){}

    /**
     * chars 分词
     * @return 实现
     */
    public static IPinyinSegment chars() {
        return Instances.singleton(CharPinyinSegment.class);
    }

    /**
     * 默认分词
     * @return 实现
     */
    public static IPinyinSegment defaults() {
        return Instances.singleton(DefaultPinyinSegment.class);
    }

    /**
     * 单个分词
     * @return 实现
     */
    public static IPinyinSegment single() {
        return Instances.singleton(SinglePinyinSegment.class);
    }
}
