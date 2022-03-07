package com.taotao.cloud.core.pinyin.support.style;

import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.pinyin.model.CharToneInfo;

/**
 * 不带声调的注音形式
 * <p>
 * （1）直接使用便利替换，避免 regex 的开销。
 * （2）原理上来复杂度都是 O(n)
 * <p>
 * 一个拼音只会有一个声调，如果有直接返回即可。
 *
 */
@ThreadSafe
public class NormalPinyinToneStyle extends AbstractPinyinToneStyle {

    @Override
    protected String getCharFormat(String tone, CharToneInfo toneInfo) {
        int index = toneInfo.getIndex();

        // 没有音调，直接返回
        if (index < 0) {
            return tone;
        }

        char letter = toneInfo.getToneItem().getLetter();
        return super.connector(tone, index, String.valueOf(letter));
    }

}
