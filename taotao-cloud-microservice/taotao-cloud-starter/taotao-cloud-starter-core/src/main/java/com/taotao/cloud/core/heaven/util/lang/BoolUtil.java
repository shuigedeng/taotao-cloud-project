package com.taotao.cloud.core.heaven.util.lang;

/**
 * 布尔值工具类
 */
public final class BoolUtil {

    private BoolUtil(){}

    /**
     * Y 字符串
     * @since 0.1.83
     */
    public static final String Y = "Y";

    /**
     * N 字符串
     * @since 0.1.83
     */
    public static final String N = "N";

    /**
     * 获取对应的bool值
     *
     * @param boolStr 布尔字符串
     * @return 是否
     */
    public static boolean getBool(String boolStr) {
        if ("YES".equals(boolStr)) {
            return true;
        }
        if ("Y".equals(boolStr)) {
            return true;
        }
        if("1".equals(boolStr)) {
            return true;
        }
        if("true".equals(boolStr)) {
            return true;
        }
        if("是".equals(boolStr)) {
            return true;
        }

        return false;
    }

    /**
     * 结果
     * @param value 布尔值
     * @return 结果
     * @since 0.1.150
     */
    public static String getYesOrNo(boolean value) {
        if(value) {
            return BoolUtil.Y;
        }

        return BoolUtil.N;
    }

}
