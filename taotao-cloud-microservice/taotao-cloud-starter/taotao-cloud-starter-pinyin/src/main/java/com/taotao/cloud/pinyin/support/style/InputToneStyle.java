package com.taotao.cloud.pinyin.support.style;


import com.taotao.cloud.pinyin.model.CharToneInfo;

/**
 * 符合输入法的方式
 *
 * nv 女
 * lv 绿
 */
public class InputToneStyle extends AbstractPinyinToneStyle {

    @Override
    protected String getCharFormat(String tone, CharToneInfo toneInfo) {
        int index = toneInfo.getIndex();

        // 没有音调，直接返回
        String result = tone;
        if (index >= 0) {
            char letter = toneInfo.getToneItem().getLetter();
            result = super.connector(tone, index, String.valueOf(letter));
        }
        // 替换掉输入法不支持的部分
        return result.replace('ü', 'v');
    }

}
