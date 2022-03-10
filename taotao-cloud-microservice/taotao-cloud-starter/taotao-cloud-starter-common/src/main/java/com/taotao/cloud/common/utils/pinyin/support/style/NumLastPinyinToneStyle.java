package com.taotao.cloud.common.utils.pinyin.support.style;


import com.taotao.cloud.common.utils.pinyin.model.CharToneInfo;

/**
 * 数字标注放在最后拼音注音形式
 *
 */
public class NumLastPinyinToneStyle extends AbstractPinyinToneStyle {

    @Override
    protected String getCharFormat(String tone, CharToneInfo toneInfo) {
        int index = toneInfo.getIndex();

        // 轻声
        if (index < 0) {
            return tone + "5";
        }

        // 直接拼接在最后
        char letter = toneInfo.getToneItem().getLetter();
        int num = toneInfo.getToneItem().getTone();
        return super.connector(tone, index, String.valueOf(letter))+num;
    }

}
