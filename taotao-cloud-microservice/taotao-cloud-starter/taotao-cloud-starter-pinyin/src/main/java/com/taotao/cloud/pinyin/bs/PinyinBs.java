package com.taotao.cloud.pinyin.bs;


import com.taotao.cloud.common.support.instance.impl.Instances;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.pinyin.api.IPinyin;
import com.taotao.cloud.pinyin.api.IPinyinContext;
import com.taotao.cloud.pinyin.api.impl.Pinyin;
import com.taotao.cloud.pinyin.api.impl.PinyinContext;
import com.taotao.cloud.pinyin.spi.IPinyinChinese;
import com.taotao.cloud.pinyin.spi.IPinyinData;
import com.taotao.cloud.pinyin.spi.IPinyinSegment;
import com.taotao.cloud.pinyin.spi.IPinyinTone;
import com.taotao.cloud.pinyin.spi.IPinyinToneReverse;
import com.taotao.cloud.pinyin.spi.IPinyinToneStyle;
import com.taotao.cloud.pinyin.support.chinese.PinyinChineses;
import com.taotao.cloud.pinyin.support.data.PinyinData;
import com.taotao.cloud.pinyin.support.segment.PinyinSegments;
import com.taotao.cloud.pinyin.support.style.PinyinToneStyles;
import com.taotao.cloud.pinyin.support.tone.PinyinToneReverse;
import com.taotao.cloud.pinyin.support.tone.PinyinTones;

import java.util.List;

/**
 * 拼音引导类
 */
public final class PinyinBs {

    private PinyinBs(){}

    /**
     * 默认分词
     */
    private IPinyinSegment pinyinSegment = PinyinSegments.defaults();

    /**
     * 中文服务类
     */
    private final IPinyinChinese pinyinChinese = PinyinChineses.defaults();

    /**
     * 注音映射
     */
    private final IPinyinTone pinyinTone = PinyinTones.defaults();

    /**
     * 拼音数据实现
     */
    private final IPinyinData data = Instances.singleton(PinyinData.class);

    /**
     * 拼音的形式
     */
    private IPinyinToneStyle style = PinyinToneStyles.defaults();

    /**
     * 默认核心实现
     */
    private final IPinyin pinyin = Instances.singleton(Pinyin.class);

    /**
     * 连接符号
     */
    private String connector = StringUtil.BLANK;

    /**
     * 拼音的反向标注
     */
    private IPinyinToneReverse pinyinToneReverse = new PinyinToneReverse();

    /**
     * 拼音上下文
     */
    private IPinyinContext pinyinContext;

    /**
     * 新建引导类实例
     * @return 引导类
     */
    public static PinyinBs newInstance() {
        return new PinyinBs();
    }

    /**
     * 设置样式
     * @param style 样式
     * @return this
     */
    public PinyinBs style(IPinyinToneStyle style) {
        ArgUtil.notNull(style, "style");

        this.style = style;
        return this;
    }

    /**
     * 设置连接符号
     * @param connector 连接符号
     * @return this
     */
    public PinyinBs connector(String connector) {
        this.connector = connector;
        return this;
    }

    /**
     * 添加自定义分词
     * @param pinyinSegment 拼音分词实现
     * @return 分词
     */
    public PinyinBs segment(IPinyinSegment pinyinSegment) {
        ArgUtil.notNull(pinyinSegment, "segment");

        this.pinyinSegment = pinyinSegment;
        return this;
    }

    /**
     * 拼音反向标注
     * @param pinyinToneReverse 反向拼音标注
     * @return 结果
     */
    public PinyinBs pinyinToneReverse(IPinyinToneReverse pinyinToneReverse) {
        ArgUtil.notNull(pinyinToneReverse, "pinyinToneReverse");

        this.pinyinToneReverse = pinyinToneReverse;
        return this;
    }

    public PinyinBs init() {
        this.pinyinContext = PinyinContext.newInstance()
                .chinese(pinyinChinese)
                .data(data)
                .segment(pinyinSegment)
                .style(style)
                .tone(pinyinTone)
                .connector(connector)
                .pinyinToneReverse(pinyinToneReverse);

        return this;
    }

    private synchronized void statusCheck() {
        if(pinyinContext == null) {
            this.init();
        }
    }

    /**
     * 转换为拼音字符串
     * @param string 字符串
     * @return 结果
     */
    public String toPinyin(String string) {
        if(StringUtil.isEmpty(string)) {
            return string;
        }

        statusCheck();
        return pinyin.toPinyin(string, pinyinContext);
    }

    /**
     * 转换为拼音列表
     * @param chinese 中文字符
     * @return 结果
     */
    public List<String> toPinyinList(char chinese) {
        statusCheck();
        return pinyin.toPinyinList(chinese, pinyinContext);
    }

    /**
     * 拥有相同的拼音
     * @param chineseOne 中文1
     * @param chineseTwo 中文2
     * @return 结果
     */
    public boolean hasSamePinyin(char chineseOne, char chineseTwo) {
        statusCheck();
        return pinyin.hasSamePinyin(chineseOne, chineseTwo, pinyinContext);
    }

    /**
     * 相同的拼音列表
     * @param pinyin 拼音
     * @param sameTone 相同的声调
     * @return 结果
     */
    public List<String> samePinyinList(String pinyin, final boolean sameTone) {
        statusCheck();
        return this.pinyin.samePinyinList(pinyin, sameTone, pinyinContext);
    }

}
