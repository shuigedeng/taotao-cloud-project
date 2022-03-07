package com.taotao.cloud.core.pinyin.spi;

/**
 * <p> project: pinyin-IPinyinStyle </p>
 * <p> create on 2020/2/24 21:44 </p>
 *
 * @author Administrator
 * @since 1.0.0
 */
public interface IPinyinToneStyle {

    /**
     * 进行格式化
     * @param charTone 原始的默认格式拼音
     * @return 结果
     * @since 0.1.1
     */
    String style(final String charTone);

}
