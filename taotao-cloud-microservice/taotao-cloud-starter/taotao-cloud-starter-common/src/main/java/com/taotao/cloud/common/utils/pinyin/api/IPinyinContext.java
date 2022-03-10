package com.taotao.cloud.common.utils.pinyin.api;


import com.taotao.cloud.common.utils.pinyin.spi.IPinyinChinese;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinData;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinSegment;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinTone;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinToneReverse;
import com.taotao.cloud.common.utils.pinyin.spi.IPinyinToneStyle;

/**
 * 拼音核心用户 api 上下文
 */
public interface IPinyinContext {

    /**
     * 样式
     * @return 样式
     * @since 0.1.1
     */
    IPinyinToneStyle style();

    /**
     * 分词实现
     * @return 分词
     * @since 0.1.1
     */
    IPinyinSegment segment();

    /**
     * 拼音数据实现
     * @return 数据实现
     * @since 0.1.1
     */
    IPinyinData data();

    /**
     * 中文服务类
     * @return 中文服务类
     * @since 0.0.1
     */
    IPinyinChinese chinese();

    /**
     * 注音实现
     * @return 注音实现
     * @since 0.0.1
     */
    IPinyinTone tone();

    /**
     * 连接符
     * @since 0.1.2
     * @return 连接符
     */
    String connector();

    /**
     * 拼音反向
     *
     * @return 实现
     * @since 0.3.0
     */
    IPinyinToneReverse pinyinToneReverse();
}
