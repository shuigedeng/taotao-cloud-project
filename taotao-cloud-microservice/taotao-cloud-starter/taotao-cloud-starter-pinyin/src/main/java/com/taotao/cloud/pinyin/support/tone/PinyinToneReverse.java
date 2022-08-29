package com.taotao.cloud.pinyin.support.tone;


import com.google.common.collect.Maps;
import com.taotao.cloud.common.constant.PunctuationConst;

import com.taotao.cloud.common.utils.io.FileStreamUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.pinyin.constant.PinyinConst;
import com.taotao.cloud.pinyin.spi.IPinyinToneReverse;
import com.taotao.cloud.pinyin.spi.IPinyinToneStyle;
import com.taotao.cloud.pinyin.support.style.PinyinToneStyles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 反向拼音
 *
 * key: pinyin
 * value: hanziList
 *
 */
public class PinyinToneReverse implements IPinyinToneReverse {

    /**
     * 单个字的 Map
     * DCL 单例，惰性加载。
     *
     * （1）注意多音字的问题
     * （2）默认只返回第一个
     * （3）为了提升读取的性能，在初始化的时候，直接设计好。
     */
    private static final Map<String, List<String>> CHAR_MAP;

    static {
        List<String> lines = FileStreamUtils.readAllLines(PinyinConst.PINYIN_DICT_CHAR_SYSTEM);
        // 自定义词库
        List<String> defineLines = FileStreamUtils.readAllLines(PinyinConst.PINYIN_DICT_CHAR_DEFINE);
        lines.addAll(defineLines);
        CHAR_MAP = Maps.newHashMap();

        // 拼音格式化
        IPinyinToneStyle pinyinToneStyle = PinyinToneStyles.numLast();

        for(String line : lines) {
            String[] strings = line.split(PunctuationConst.COLON);
            List<String> pinyinList = StringUtils.splitToList(strings[1]);
            final String hanzi = strings[0];

            for(String pinyin : pinyinList) {
                String pinyinNumLast = pinyinToneStyle.style(pinyin);

                // 获取
                List<String> hanziList = CHAR_MAP.get(pinyinNumLast);
                if(hanziList == null) {
                    hanziList = new ArrayList<>();
                }

                hanziList.add(hanzi);

                //pinyin1
                CHAR_MAP.put(pinyinNumLast, hanziList);
            }
        }
    }

    @Override
    public List<String> getHanziList(String pinyinLast) {
        return CHAR_MAP.get(pinyinLast);
    }

}
