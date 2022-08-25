package com.taotao.cloud.pinyin.support.tone;


import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.pinyin.api.IPinyinContext;
import com.taotao.cloud.pinyin.spi.IPinyinTone;
import com.taotao.cloud.pinyin.spi.IPinyinToneStyle;
import java.util.Collections;
import java.util.List;

/**
 * 抽象拼音映射实现类
 *
 */
public abstract class AbstractPinyinTone implements IPinyinTone {

    /**
     * 获取单个字的映射
     * @param segment 单个的字
     * @param toneStyle 拼音格式
     * @return 结果
     */
    protected abstract String getCharTone(final String segment, final IPinyinToneStyle toneStyle);

    /**
     * 获取单个字符的所有拼音列表
     * @param chinese 中文字符
     * @param toneStyle 拼音格式
     * @return 结果列表
     */
    protected abstract List<String> getCharTones(final String chinese, final IPinyinToneStyle toneStyle);

    /**
     * 获取词组的映射
     * @param phraseTone 单个的字
     * @param toneStyle 拼音格式
     * @param connector 连接符号
     * @return 结果
     */
    protected abstract String getPhraseTone(final String phraseTone,
                                            final IPinyinToneStyle toneStyle,
                                            final String connector);

    @Override
    public String tone(String segment, final IPinyinContext context) {
        int length = segment.length();
        final IPinyinToneStyle toneStyle = context.style();
        // FIXED 6
        final String connector = context.connector();

        if(length == 1) {
            getCharToneDefault(segment, toneStyle);
        }

        String result = getPhraseTone(segment, toneStyle, connector);
        if(StringUtil.isNotEmpty(result)) {
            return result;
        }

        // 直接拆分为单个字，然后用空格分隔
        List<String> chars = StringUtil.toCharStringList(segment);
        List<String> tones = CollectionUtil.toList(chars, new IHandler<String, String>() {
            @Override
            public String handle(String string) {
                return getCharToneDefault(string, toneStyle);
            }
        });

        return StringUtil.join(tones, StringUtil.BLANK);
    }

    /**
     * 处理单个字符没有结果的情况
     * 1. 如果有拼音，则返回对应的拼音
     * 2. 如果没有，则返回单个字符本身（比如字典中没有录入的繁体字等）
     * @param string 字符串
     * @param toneStyle 格式化
     * @return 结果
     */
    private String getCharToneDefault(final String string, final IPinyinToneStyle toneStyle) {
        String pinyin = getCharTone(string, toneStyle);

        if(StringUtil.isNotEmpty(pinyin)) {
            return pinyin;
        }

        return string;
    }

    @Override
    public List<String> toneList(String chinese, final IPinyinContext context) {
        final IPinyinToneStyle toneStyle = context.style();

        return getCharTonesDefault(chinese, toneStyle);
    }

    /**
     * 多音字列表的默认处理
     * @param chinese 英文字符
     * @param toneStyle 拼音格式
     * @return 结果
     */
    private List<String> getCharTonesDefault(final String chinese, final IPinyinToneStyle toneStyle) {
        List<String> toneList = getCharTones(chinese, toneStyle);

        if(CollectionUtil.isNotEmpty(toneList)) {
            return toneList;
        }

        return Collections.singletonList(chinese);
    }

}
