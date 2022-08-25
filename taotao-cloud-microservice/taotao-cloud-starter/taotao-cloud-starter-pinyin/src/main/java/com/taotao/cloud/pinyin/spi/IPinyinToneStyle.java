package com.taotao.cloud.pinyin.spi;

public interface IPinyinToneStyle {

    /**
     * 进行格式化
     * @param charTone 原始的默认格式拼音
     * @return 结果
     */
    String style(final String charTone);

}
