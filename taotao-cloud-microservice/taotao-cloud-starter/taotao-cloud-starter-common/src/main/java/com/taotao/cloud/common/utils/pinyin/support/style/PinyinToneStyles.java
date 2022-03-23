package com.taotao.cloud.common.utils.pinyin.support.style;

import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.utils.pinyin.constant.enums.PinyinStyleEnum;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinToneStyle;

/**
 * 拼音标注的不同形式
 */
public final class PinyinToneStyles {

    private PinyinToneStyles(){}

    /**
     * 获取对应的标注实现
     * @param styleEnum 样式枚举
     * @return 标注实现
     */
    public static IPinyinToneStyle getTone(final PinyinStyleEnum styleEnum) {
        if(PinyinStyleEnum.DEFAULT.equals(styleEnum)) {
            return defaults();
        }
        if(PinyinStyleEnum.NORMAL.equals(styleEnum)) {
            return normal();
        }
        if(PinyinStyleEnum.NUM_LAST.equals(styleEnum)) {
            return numLast();
        }
        if(PinyinStyleEnum.FIRST_LETTER.equals(styleEnum)) {
            return firstLetter();
        }
        if(PinyinStyleEnum.INPUT.equals(styleEnum)) {
            return input();
        }

        // 返回默认
        return defaults();
    }

    /**
     * 返回默认实现
     * @return 默认实现
     */
    public static IPinyinToneStyle defaults() {
        return Instances.singleton(DefaultPinyinToneStyle.class);
    }

    /**
     * 返回首字母实现
     * @return 实现
     */
    public static IPinyinToneStyle firstLetter() {
        return Instances.singleton(FirstLetterPinyinToneStyle.class);
    }

    /**
     * 数字放在最后的实现
     * @return 实现
     */
    public static IPinyinToneStyle numLast() {
        return Instances.singleton(NumLastPinyinToneStyle.class);
    }

    /**
     * 正常格式的实现
     * @return 实现
     */
    public static IPinyinToneStyle normal() {
        return Instances.singleton(NormalPinyinToneStyle.class);
    }

    /**
     * 符合输入法的实现方式
     * @return 实现
     */
    public static IPinyinToneStyle input() {
        return Instances.singleton(InputToneStyle.class);
    }

}
