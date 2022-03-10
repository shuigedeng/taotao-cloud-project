package com.taotao.cloud.common.utils.pinyin.support.style;


import com.taotao.cloud.common.utils.pinyin.model.CharToneInfo;

/**
 * 首字母的拼音注音形式
 *
 */
public class FirstLetterPinyinToneStyle extends AbstractPinyinToneStyle {

    @Override
    protected String getCharFormat(String tone, CharToneInfo toneInfo) {
        int index = toneInfo.getIndex();

        // 没有音调，直接返回
        if(index != 0) {
            return String.valueOf(tone.charAt(0));
        }

        // 刚好是第一个
        return String.valueOf(toneInfo.getToneItem().getLetter());
    }

}
