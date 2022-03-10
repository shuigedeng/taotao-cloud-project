package com.taotao.cloud.common.utils.pinyin.spi;

/**
 * 声母韵母
 */
public interface IPinyinData {

    /**
     * 获取声母
     * @param pinyinNormal 拼音
     * @return 声母
     * @since 0.1.1
     *
     * @see #isZeroShengMu(String) 如果是零声母，那么这里返回 {@link com.github.houbb.heaven.util.lang.StringUtil#EMPTY}
     */
    String shengMu(final String pinyinNormal);

    /**
     * 获取韵母
     * @param pinyinNormal 拼音
     * @return 韵母
     * @since 0.1.1
     */
    String yunMu(final String pinyinNormal);

    /**
     * 零声母
     * @param pinyinNormal 拼音
     * @return 是否
     * @since 0.1.1
     */
    boolean isZeroShengMu(final String pinyinNormal);

}
