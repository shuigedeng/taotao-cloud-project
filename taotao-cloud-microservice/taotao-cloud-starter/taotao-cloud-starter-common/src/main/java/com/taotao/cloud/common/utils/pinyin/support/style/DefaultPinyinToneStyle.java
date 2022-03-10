package com.taotao.cloud.common.utils.pinyin.support.style;


import com.taotao.cloud.common.utils.pinyin.spi.IPinyinToneStyle;

/**
 * 默认格式
 */
public class DefaultPinyinToneStyle implements IPinyinToneStyle {

    @Override
    public String style(String charTone) {
        return charTone;
    }

}
