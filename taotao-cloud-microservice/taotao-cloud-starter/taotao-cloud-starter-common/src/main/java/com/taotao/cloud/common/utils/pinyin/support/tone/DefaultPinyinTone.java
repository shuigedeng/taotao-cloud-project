package com.taotao.cloud.common.utils.pinyin.support.tone;


import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.guava.Guavas;
import com.taotao.cloud.common.utils.io.StreamUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.pinyin.constant.PinyinConst;
import com.taotao.cloud.common.utils.pinyin.constant.enums.PinyinToneNumEnum;
import com.taotao.cloud.common.utils.pinyin.model.CharToneInfo;
import com.taotao.cloud.common.utils.pinyin.model.ToneItem;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinToneStyle;
import com.taotao.cloud.common.utils.pinyin.util.InnerToneHelper;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 正常的拼音注音形式
 *
 * （1）单个字的词库与词组的词库分离
 * （2）二者词库都是懒加载，只有使用的时候才会去初始化。
 *
 */
public class DefaultPinyinTone extends AbstractPinyinTone {

    /**
     * 单个字的 Map
     * DCL 单例，惰性加载。
     *
     * （1）注意多音字的问题
     * （2）默认只返回第一个
     * （3）为了提升读取的性能，在初始化的时候，直接设计好。
     * @since 0.0.1
     */
    private static volatile Map<String, List<String>> charMap;

    /**
     * 词组 map
     *
     * DCL 单例，惰性加载。
     * @since 0.0.1
     */
    private static volatile Map<String, String> phraseMap;

    @Override
    protected List<String> getCharTones(String chinese, final IPinyinToneStyle toneStyle) {
        List<String> defaultList = getCharMap().get(chinese);

        return CollectionUtil.toList(defaultList, new IHandler<String, String>() {
            @Override
            public String handle(String s) {
                return toneStyle.style(s);
            }
        });
    }

    @Override
    protected String getCharTone(String segment, final IPinyinToneStyle toneStyle) {
        // 大部分拼音都是单个字，不是多音字。
        // 直接在初始化的时候，设置好。
        List<String> pinyinList = getCharMap().get(segment);
        if(CollectionUtil.isNotEmpty(pinyinList)) {
            final String firstPinyin = pinyinList.get(0);
            return toneStyle.style(firstPinyin);
        }

        // 没有则返回空
        return null;
    }

    @Override
    protected String getPhraseTone(String segment,
                                   final IPinyinToneStyle toneStyle,
                                   final String connector) {
        String phrasePinyin = getPhraseMap().get(segment);

        // 直接返回空
        if(StringUtil.isEmptyTrim(phrasePinyin)) {
            return StringUtil.EMPTY;
        }

        String[] strings = phrasePinyin.split(StringUtil.BLANK);
        List<String> resultList = Guavas.newArrayList(strings.length);

        for(String string : strings) {
            final String style = toneStyle.style(string);
            resultList.add(style);
        }
        return StringUtil.join(resultList, connector);
    }

    /**
     * 获取单个字符的 map
     *
     * @return map
     * @since 0.0.1
     */
    private Map<String, List<String>> getCharMap() {
        if(ObjectUtil.isNotNull(charMap)) {
            return charMap;
        }

        synchronized (DefaultPinyinTone.class) {
            if(ObjectUtil.isNull(charMap)) {
                List<String> lines = StreamUtil.readAllLines(PinyinConst.PINYIN_DICT_CHAR_SYSTEM);
                // 自定义词库
                List<String> defineLines = StreamUtil.readAllLines(PinyinConst.PINYIN_DICT_CHAR_DEFINE);
                lines.addAll(defineLines);
                charMap = Guavas.newHashMap(lines.size());

                for(String line : lines) {
                    String[] strings = line.split(PunctuationConst.COLON);
                    List<String> pinyinList = StringUtil.splitToList(strings[1]);

                    final String word = strings[0];
                    charMap.put(word, pinyinList);
                }
            }
        }

        return charMap;
    }

    /**
     * 获取词组的拼音 Map
     * （1）词组的拼音是确定的。
     * @return map
     * @since 0.0.1
     */
    private Map<String, String> getPhraseMap() {
        if(ObjectUtil.isNotNull(phraseMap)) {
            return phraseMap;
        }
        synchronized (DefaultPinyinTone.class) {
            if(ObjectUtil.isNull(phraseMap)) {
                final long startTime = System.currentTimeMillis();
                List<String> lines = StreamUtil.readAllLines(PinyinConst.PINYIN_DICT_PHRASE_SYSTEM);
                // 处理自定义字典
                List<String> defineLines = StreamUtil.readAllLines(PinyinConst.
	                PINYIN_DICT_PHRASE_DEFINE);
                lines.addAll(defineLines);
                phraseMap = Guavas.newHashMap(lines.size());

                for(String line : lines) {
                    String[] strings = line.split(PunctuationConst.COLON);
                    String word = strings[0];
                    phraseMap.put(word, strings[1]);
                }

                final long endTime = System.currentTimeMillis();
                System.out.println("[Pinyin] phrase dict loaded, cost time " + (endTime-startTime)+" ms!");
            }
        }

        return phraseMap;
    }

    @Override
    public Set<String> phraseSet() {
        Map<String, String> map = getPhraseMap();
        return map.keySet();
    }

    @Override
    public int toneNum(String defaultPinyin) {
        //1. 获取拼音
        if(StringUtil.isNotEmpty(defaultPinyin)) {
            CharToneInfo toneInfo = this.getCharToneInfo(defaultPinyin);

            int index = toneInfo.getIndex();

            // 轻声
            if (index < 0) {
                return PinyinToneNumEnum.FIVE.num();
            }

            // 直接返回对应的音标
            return toneInfo.getToneItem().getTone();
        }

        // 默认返回未知
        return PinyinToneNumEnum.UN_KNOWN.num();
    }

    /**
     * 获取对应的声调信息
     * @param tone 拼音信息
     * @return 声调信息
     * @since 0.0.3
     */
    protected CharToneInfo getCharToneInfo(final String tone) {
        CharToneInfo charToneInfo = new CharToneInfo();
        charToneInfo.setIndex(-1);

        int length = tone.length();
        for(int i = 0; i < length; i++) {
            char currentChar = tone.charAt(i);
            ToneItem toneItem = InnerToneHelper.getToneItem(currentChar);

            if (ObjectUtil.isNotNull(toneItem)) {
                charToneInfo.setToneItem(toneItem);
                charToneInfo.setIndex(i);
                break;
            }
        }

        return charToneInfo;
    }

}
