package com.taotao.cloud.pinyin.constant.enums;

/**
 * 拼音音调编号枚举
 *
 * <p> project: pinyin-PinyinToneNumEnum </p>
 * <p> create on 2020/2/23 17:01 </p>
 *
 */
public enum PinyinToneNumEnum {

    /**
     * 声调枚举
     *
     * 在现代汉语四声中，第一声、第二声是平声；第三声、第四声是仄声。
     *
     * 古代的入声字已经被均摊到这几种读音之中。
     */
    ONE(1, "阴平"),
    TWO(2, "阳平"),
    THREE(3, "上声"),
    FOUR(4, "去声"),
    FIVE(5, "轻声"),
    UN_KNOWN(-1, "未知"),
    ;

    /**
     * 音调编号
     *
     */
    private final int num;

    /**
     * 中文描述
     *
     */
    private final String desc;

    PinyinToneNumEnum(int num, String desc) {
        this.num = num;
        this.desc = desc;
    }

    public int num() {
        return num;
    }

    public String desc() {
        return desc;
    }

    @Override
    public String toString() {
        return "PinyinToneNumEnum{" +
                "num=" + num +
                ", desc='" + desc + '\'' +
                '}';
    }

    /**
     * 是否为平声字
     * @param toneNum 音调
     * @return 结果
     */
    public static boolean isPing(final int toneNum) {
        return ONE.num == toneNum || TWO.num == toneNum;
    }

    /**
     * 是否为仄声字
     * @param toneNum 音调
     * @return 结果
     */
    public static boolean isZe(final int toneNum) {
        return THREE.num == toneNum || FOUR.num == toneNum;
    }

    /**
     * 是否为轻声
     * @param toneNum 音调
     * @return 结果
     */
    public static boolean isSoftly(final int toneNum) {
        return FIVE.num == toneNum;
    }

}
