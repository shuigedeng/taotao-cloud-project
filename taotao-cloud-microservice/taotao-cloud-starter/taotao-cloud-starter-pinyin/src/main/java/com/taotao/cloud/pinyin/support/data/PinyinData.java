package com.taotao.cloud.pinyin.support.data;


import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.pinyin.spi.IPinyinData;
import java.util.Arrays;
import java.util.List;

/**
 * 声母韵母
 */
public class PinyinData implements IPinyinData {

    /**
     * 零声母列表
     * a ai an ang ao e ê ei en eng er o ou
     */
    private static final List<String> ZERO_SHENG_MU_LIST = Arrays
            .asList("a", "ai", "an", "ang", "ao", "e", "ê", "ei", "en", "eng", "er", "o", "ou");


    /**
     * 双字母的声母
     * zh
     * ch
     * sh
     */
    private static final List<String> DOUBLE_SHENG_MU_LIST = Arrays.asList("zh", "ch", "sh");

    @Override
    public String shengMu(String pinyinNormal) {
        if(isZeroShengMu(pinyinNormal)) {
            return StringUtils.EMPTY;
        }

        final String prefixDouble = pinyinNormal.substring(0, 2);
        if(DOUBLE_SHENG_MU_LIST.contains(prefixDouble)) {
            return prefixDouble;
        }

        // 默认返回第一个音节
        return pinyinNormal.substring(0, 1);
    }

    @Override
    public String yunMu(String pinyinNormal) {
        String shengMu = shengMu(pinyinNormal);
        return pinyinNormal.substring(shengMu.length());
    }

    @Override
    public boolean isZeroShengMu(String pinyinNormal) {
        return ZERO_SHENG_MU_LIST.contains(pinyinNormal);
    }

}
