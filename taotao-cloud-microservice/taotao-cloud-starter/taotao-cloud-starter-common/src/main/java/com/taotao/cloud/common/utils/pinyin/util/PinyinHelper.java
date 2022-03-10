package com.taotao.cloud.common.utils.pinyin.util;


import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.pinyin.bs.PinyinBs;
import com.taotao.cloud.common.utils.pinyin.constant.enums.PinyinStyleEnum;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinToneStyle;
import com.taotao.cloud.common.utils.pinyin.support.style.PinyinToneStyles;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简化用于调用
 */
public final class PinyinHelper {

    private PinyinHelper(){}

    /**
     * 默认的实现类
     *
     * 避免最基本的方法调用，多次创建对象的问题
     * @since 0.2.0
     */
    private static final PinyinBs PINYIN_BS_DEFAULT = PinyinBs.newInstance().init();

    /**
     * 转换为拼音
     * @param string 原始信息
     * @return 结果
     * @since 0.0.1
     */
    public static String toPinyin(final String string) {
        return PINYIN_BS_DEFAULT.toPinyin(string);
    }

    /**
     * 转换为拼音
     * @param string 原始信息
     * @param styleEnum 样式枚举
     * @return 结果
     * @since 0.0.3
     */
    public static String toPinyin(final String string,
                                  final PinyinStyleEnum styleEnum) {
        return toPinyin(string, styleEnum, StringUtil.BLANK);
    }

    /**
     * 转换为拼音
     * @param string 原始信息
     * @param styleEnum 样式枚举
     * @param connector 连接符号
     * @return 结果
     * @since 0.1.2
     */
    public static String toPinyin(final String string,
                                  final PinyinStyleEnum styleEnum,
                                  final String connector) {
        ArgUtil.notNull(connector, "connector");

        if(StringUtil.isEmptyTrim(string)) {
            return string;
        }

        final IPinyinToneStyle style = PinyinToneStyles.getTone(styleEnum);
        return PinyinBs.newInstance()
                .style(style)
                .connector(connector)
                .init()
                .toPinyin(string);
    }

    /**
     * 返回拼音列表
     * @param chinese 中文字符
     * @return 结果
     * @since 0.1.1
     */
    public static List<String> toPinyinList(final char chinese) {
        return PINYIN_BS_DEFAULT.toPinyinList(chinese);
    }

    /**
     * 返回拼音列表
     * @param chinese 中文字符
     * @param styleEnum 指定样式
     * @return 结果
     * @since 0.1.1
     */
    public static List<String> toPinyinList(final char chinese, final PinyinStyleEnum styleEnum) {
        final IPinyinToneStyle pinyinTone = PinyinToneStyles.getTone(styleEnum);
        return PinyinBs.newInstance()
                .style(pinyinTone)
                .init()
                .toPinyinList(chinese);
    }

    /**
     * 是否拥有相同的拼音
     * @param chineseOne 汉字一
     * @param chineseTwo 汉字二
     * @return 是否相同
     * @since 0.0.8
     */
    public static boolean hasSamePinyin(final char chineseOne, final char chineseTwo) {
        return PINYIN_BS_DEFAULT.hasSamePinyin(chineseOne, chineseTwo);
    }

    /**
     * 同音字 map
     *
     * 因为汉字存在多音字，所以这里返回的是一个 map
     * key: 汉字的拼音
     * value: 对应的同音字列表
     *
     * @param hanzi 汉字
     * @return 字符列表
     * @since 0.3.0
     */
    public static Map<String, List<String>> samePinyinMap(final char hanzi) {
        //1. 获取汉字的多音字列表
        List<String> pinyinList = toPinyinList(hanzi, PinyinStyleEnum.NUM_LAST);
        if(CollectionUtil.isEmpty(pinyinList)) {
            return Collections.emptyMap();
        }

        //2. 获取每一个汉字对应的同音字列表
        Map<String, List<String>> map = new HashMap<>(pinyinList.size());
        for(String pinyin : pinyinList) {
            List<String> characterList = samePinyinList(pinyin);

            map.put(pinyin, characterList);
        }

        return map;
    }

    /**
     * 获取拼音对应的同音字列表
     * @param pinyinNumLast 拼音
     * @return 结果
     * @since 0.3.0
     */
    public static List<String> samePinyinList(String pinyinNumLast) {
        final boolean sameTone = true;

        return PINYIN_BS_DEFAULT.samePinyinList(pinyinNumLast, sameTone);
    }

}
