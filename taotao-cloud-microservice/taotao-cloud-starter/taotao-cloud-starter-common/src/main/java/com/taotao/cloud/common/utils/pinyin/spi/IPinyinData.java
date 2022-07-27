package com.taotao.cloud.common.utils.pinyin.spi;

/**
 * 声母韵母
 */
public interface IPinyinData {

    /**
     * 获取声母
     * @param pinyinNormal 拼音
     * @return 声母
     *
     * @see #isZeroShengMu(String)
     */
    String shengMu(final String pinyinNormal);

    /**
     * 获取韵母
     * @param pinyinNormal 拼音
     * @return 韵母
     */
    String yunMu(final String pinyinNormal);

    /**
     * 零声母
     * @param pinyinNormal 拼音
     * @return 是否
     */
    boolean isZeroShengMu(final String pinyinNormal);

}
