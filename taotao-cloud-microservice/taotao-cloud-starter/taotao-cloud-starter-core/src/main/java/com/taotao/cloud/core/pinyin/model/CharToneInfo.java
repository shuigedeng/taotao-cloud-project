package com.taotao.cloud.core.pinyin.model;

/**
 * <p> project: pinyin-ToneItem </p>
 * <p> create on 2020/1/18 16:14 </p>
 *
 * @author Administrator
 * @since 0.0.3
 */
public class CharToneInfo {

    /**
     * 音调所在位置下表
     * @since 0.0.3
     */
    private int index;

    /**
     * 音调元素
     * @since 0.0.3
     */
    private ToneItem toneItem;

    /**
     * 构建对象实例
     * @param toneItem 音调信息
     * @param index 下标志
     * @return 结果
     * @since 0.0.3
     */
    public static CharToneInfo of(final ToneItem toneItem,
                                  final int index) {
        CharToneInfo item = new CharToneInfo();
        item.toneItem = toneItem;
        item.index = index;

        return item;
    }

    public int getIndex() {
        return index;
    }

    public ToneItem getToneItem() {
        return toneItem;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setToneItem(ToneItem toneItem) {
        this.toneItem = toneItem;
    }

    @Override
    public String toString() {
        return "CharToneInfo{" +
                "index=" + index +
                ", toneItem=" + toneItem +
                '}';
    }
}
