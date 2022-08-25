package com.taotao.cloud.pinyin.support.style;


import com.taotao.cloud.pinyin.spi.IPinyinToneStyle;

/**
 * 默认格式
 */
public class DefaultPinyinToneStyle implements IPinyinToneStyle {

    @Override
    public String style(String charTone) {
        return charTone;
    }

}
