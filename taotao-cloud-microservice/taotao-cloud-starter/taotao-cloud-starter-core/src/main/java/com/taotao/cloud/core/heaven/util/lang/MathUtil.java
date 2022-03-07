package com.taotao.cloud.core.heaven.util.lang;

import java.util.List;

/**
 * 数学工具类
 * <p> project: heaven-MathUtil </p>
 * <p> create on 2020/6/19 19:42 </p>
 *
 */
public final class MathUtil {

    private MathUtil(){}

    /**
     * 黄金分割比
     * @since 0.1.107
     */
    public static final double GOLD_SECTION  = 0.61803398874989484820458683436565;

    /**
     * 辗转相除法的递归调用求两个数的最大公约数
     *
     * @param x 其中一个数
     * @param y 其中另一个数
     * @return 递归调用，最终返回最大公约数
     * @since 0.1.107
     */
    public static int gcd(int x, int y) {
        return y == 0 ? x : gcd(y, x % y);
    }

    /**
     * 求n个数的最大公约数
     *
     * @param list 列表
     * @return 递归调用，最终返回最大公约数
     * @since 0.1.107
     */
    public static int ngcd(List<Integer> list) {
        return ngcd(list, list.size());
    }

    /**
     * 求n个数的最大公约数
     *
     * @param target n个数的集合
     * @param z                 数据个数
     * @return 递归调用，最终返回最大公约数
     */
    private static int ngcd(List<Integer> target, int z) {
        if (z == 1) {
            //真正返回的最大公约数
            return target.get(0);
        }
        //递归调用，两个数两个数的求
        return gcd(target.get(z - 1), ngcd(target, z - 1));
    }

    /**
     * 辗转相除法的递归调用求两个数的最小公倍数
     *
     * @param x 其中一个数
     * @param y 其中另一个数
     * @return 递归调用，最终返回最小公倍数
     */
    public static int lcm(int x, int y) {
        return (x * y) / gcd(x, y);
    }

    /**
     * 求n个数的最小公倍数
     *
     * @param list list n个数的集合
     * @return 递归调用，最终返回最小公倍数
     * @since 0.1.107
     */
    public static int nlcm(List<Integer> list) {
        return nlcm(list, list.size());
    }

    /**
     * 求n个数的最小公倍数
     *
     * @param target list n个数的集合
     * @param z      数据个数
     * @return 递归调用，最终返回最小公倍数
     * @since 0.1.107
     */
    private static int nlcm(List<Integer> target, int z) {
        if (z == 1) {
            //真正返回的最小公倍数
            return target.get(z - 1);
        }
        //递归调用，两个数两个数的求
        return lcm(target.get(z - 1), nlcm(target, z - 1));
    }

}
