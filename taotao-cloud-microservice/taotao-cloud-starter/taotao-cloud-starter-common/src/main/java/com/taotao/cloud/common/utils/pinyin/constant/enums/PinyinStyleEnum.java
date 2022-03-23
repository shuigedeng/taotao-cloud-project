package com.taotao.cloud.common.utils.pinyin.constant.enums;

/**
 * 拼音样式枚举
 *
 * 某种程度上，这里是不需要得。
 * 为了更好的拓展性，可以考虑不依靠枚举作为参数，而使用接口。
 *
 * （1）使用接口实现 √  这种吧，便于控制。
 * （2）使用接口类
 */
public enum PinyinStyleEnum {

    /**
     * 普通模式
     *
     * 普通风格，即不带声调。
     * 如：pin yin
     */
    NORMAL,

    /**
     * 声调风格，拼音声调在韵母第一个字母上。
     *
     * 注：这是默认的风格。
     * 如：pīn yīn
     *
     */
    DEFAULT,

    /**
     * 声调风格 2，即拼音声调以数字形式在各个拼音之后，用数字 [1-5] 进行表示。
     * 备注: 5 代表轻声
     * 如：pin1 yin1
     *
     */
    NUM_LAST,

    /**
     * 首字母
     *
     * 首字母风格，只返回拼音的首字母部分。
     * 如：p y
     *
     */
    FIRST_LETTER,

    /**
     * 输入法
     *
     * 主要针对：
     * 女 绿
     */
    INPUT,
    ;

}
