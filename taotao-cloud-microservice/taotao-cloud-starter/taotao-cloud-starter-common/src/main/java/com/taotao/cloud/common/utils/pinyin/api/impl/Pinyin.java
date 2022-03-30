package com.taotao.cloud.common.utils.pinyin.api.impl;


import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.CollectionUtil;
import com.taotao.cloud.common.utils.guava.Guavas;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.common.utils.pinyin.api.IPinyin;
import com.taotao.cloud.common.utils.pinyin.api.IPinyinContext;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinChinese;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinData;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinSegment;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinTone;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinToneReverse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pinyin implements IPinyin {

    @Override
    public String toPinyin(String string, IPinyinContext context) {
        List<String> stringList = toPinyinList(string, context);
        final String connector = context.connector();
        return StringUtil.join(stringList, connector);
    }

    @Override
    public List<String> toPinyinList(char chinese, IPinyinContext context) {
        String original = String.valueOf(chinese);

        // 基本属性
        final IPinyinChinese pinyinChinese = context.chinese();
        final IPinyinTone pinyinTone = context.tone();

        if(pinyinChinese.isChinese(original)) {
            String simple = pinyinChinese.toSimple(original);
            return pinyinTone.toneList(simple, context);
        }

        // 直接返回
        return Collections.singletonList(original);
    }

    @Override
    public boolean hasSamePinyin(char chineseOne, char chineseTwo, IPinyinContext context) {
        // 基本属性
        final IPinyinChinese pinyinChinese = context.chinese();
        final IPinyinTone pinyinTone = context.tone();

        if(pinyinChinese.isChinese(chineseOne) && pinyinChinese.isChinese(chineseTwo)) {
            //fast-return
            if(chineseOne == chineseTwo) {
                return true;
            }

            String simpleOne = pinyinChinese.toSimple(chineseOne);
            String simpleTwo = pinyinChinese.toSimple(chineseTwo);

            List<String> tonesOne = pinyinTone.toneList(simpleOne, context);
            List<String> tonesTwo = pinyinTone.toneList(simpleTwo, context);

            // 交集大于0
            return CollectionUtil.containAny(tonesOne, tonesTwo);
        }

        return false;
    }

	@Deprecated
    @Override
    public List<Integer> toneNumList(String chinese, IPinyinContext context) {
        // 获取拼音结果
        List<String> pinyinList = this.toPinyinList(chinese, context);

        return this.buildToneNumList(pinyinList, context.tone());
    }

	@Deprecated
    @Override
    public List<Integer> toneNumList(char chinese, IPinyinContext context) {
        List<String> pinyinList = toPinyinList(chinese, context);

        return this.buildToneNumList(pinyinList, context.tone());
    }

	@Deprecated
    @Override
    public List<String> shengMuList(String chinese, IPinyinContext context) {
        final IPinyinData pinyinData = context.data();
        return normalPinyinHandler(chinese, context, pinyinData::shengMu);
    }

	@Deprecated
    @Override
    public List<String> yunMuList(String chinese, IPinyinContext context) {
        final IPinyinData pinyinData = context.data();
        return normalPinyinHandler(chinese, context, pinyinData::yunMu);
    }

    @Override
    public List<String> samePinyinList(String pinyin, boolean sameToneNum, IPinyinContext context) {
        IPinyinToneReverse pinyinToneReverse = context.pinyinToneReverse();

        // 是否相同的拼音
        if(sameToneNum) {
            return pinyinToneReverse.getHanziList(pinyin);
        }

        // 构建所有的拼音
        List<String> resultList = new ArrayList<>();
        String pinyinRaw = pinyin.substring(0, pinyin.length()-1);
        for(int i = 1; i <= 5; i++) {
            String pinyinLast = pinyinRaw+ i;
            List<String> characterList = pinyinToneReverse.getHanziList(pinyinLast);
            if(CollectionUtil.isNotEmpty(characterList)) {
                resultList.addAll(characterList);
            }
        }

        return resultList;
    }

    /**
     * 转换为拼音列表
     * @param string 字符串
     * @return 结果
     */
    private List<String> toPinyinList(final String string, final IPinyinContext context) {
        if(StringUtil.isEmptyTrim(string)) {
            return Collections.emptyList();
        }

        // 基本属性
        final IPinyinSegment pinyinSegment = context.segment();
        final IPinyinChinese pinyinChinese = context.chinese();
        final IPinyinTone pinyinTone = context.tone();

        List<String> entryList = pinyinSegment.segment(string);
        List<String> resultList = Guavas.newArrayList(entryList.size());

        // 映射处理与连接
        for(String entry : entryList) {
            // 直接加入原始信息
            if(StringUtil.isEmptyTrim(entry)) {
                // 跳过空白信息
                continue;
            }

            // 这里理论上是不用判断是否为中文
            if(pinyinChinese.isChinese(entry)) {
                // 是否简体转换也应该加一个开关
                String simple = pinyinChinese.toSimple(entry);
                String tone = pinyinTone.tone(simple, context);

                resultList.add(tone);
            } else {
                resultList.add(entry);
            }
        }

        return resultList;
    }

    /**
     * 构建拼音编号列表
     * @param pinyinList 拼音结果
     * @param pinyinTone 拼音标注实现
     * @return  结果
     */
    private List<Integer> buildToneNumList(List<String> pinyinList, final IPinyinTone pinyinTone) {
        List<Integer> resultList = Guavas.newArrayList(pinyinList.size());

        for(String pinyin : pinyinList) {
            Integer toneNum = pinyinTone.toneNum(pinyin);
            resultList.add(toneNum);
        }
        return resultList;
    }

    /**
     * 默认拼音处理类
     * @param chinese 中文拼音
     * @param context 上下文
     * @param handler 拼音处理类
     * @return 结果
     */
    private List<String> normalPinyinHandler(final String chinese, final IPinyinContext context,
                                             final IHandler<String, String> handler) {
        // 获取拼音结果
        List<String> pinyinList = this.toPinyinList(chinese, context);
        List<String> resultList = Guavas.newArrayList(pinyinList.size());

        for (String pinyin : pinyinList) {
            String result = handler.handle(pinyin);
            resultList.add(result);
        }

        return resultList;
    }

}
