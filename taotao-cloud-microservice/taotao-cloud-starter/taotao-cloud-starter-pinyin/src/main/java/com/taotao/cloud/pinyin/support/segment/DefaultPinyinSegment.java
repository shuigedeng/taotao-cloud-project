package com.taotao.cloud.pinyin.support.segment;


import com.taotao.cloud.pinyin.spi.IPinyinSegment;
import java.util.List;

/**
 *
 * 默认的中文繁简体转换
 */
public class DefaultPinyinSegment implements IPinyinSegment {

    /**
     * 默认分词实现
     */
    //private static final ICommonSegment COMMON_SEGMENT = CommonSegments.fastForward(new DefaultPinyinTrieTreeMap());

    @Override
    public List<String> segment(String string) {
        //return COMMON_SEGMENT.segment(string);
	    return null;
    }

}
