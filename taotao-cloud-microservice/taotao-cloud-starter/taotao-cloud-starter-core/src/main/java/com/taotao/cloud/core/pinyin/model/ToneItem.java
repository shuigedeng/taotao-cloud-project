package com.taotao.cloud.core.pinyin.model;

/**
 * <p> project: pinyin-ToneItem </p>
 * <p> create on 2020/1/18 16:14 </p>
 *
 * @author Administrator
 * @since 0.0.3
 */
public class ToneItem {

    /**
     * 字母
     */
    private char letter;

    /**
     * 声调
     * @since 0.0.3
     */
    private int tone;

    /**
     * 构建对象实例
     * @param letter 字母
     * @param tone 声调
     * @return 结果
     * @since 0.0.3
     */
    public static ToneItem of(final char letter,
                              final int tone) {
        ToneItem item = new ToneItem();
        item.letter = letter;
        item.tone = tone;

        return item;
    }

    public char getLetter() {
        return letter;
    }

    public int getTone() {
        return tone;
    }

    @Override
    public String toString() {
        return "ToneItem{" +
                "letter=" + letter +
                ", tone=" + tone +
                '}';
    }

}
