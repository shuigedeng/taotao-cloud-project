package com.taotao.cloud.core.cron.util;


import com.taotao.cloud.core.cron.pojo.CronPosition;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 一些比较功能工具类
 */
public class CompareUtil {
    /**
     * 从小到大排好序的列表中找第一个符合的
     * @param current 当前值
     * @param sortedList 排好序的列表
     * @param <T> T
     */
    public static <T extends Comparable<T>> T findNext(T current , List<T> sortedList){
        for (T item : sortedList) {
            if(item.compareTo(current) >= 0){
                return item;
            }
        }
        throw new IllegalArgumentException("超出范围了");
    }

    public static <T> boolean inList(T num, List<T> list) {
        for (T tmp : list) {
            if (tmp.equals(num)) {
                //相同要执行
                return true;
            }
        }
        return false;
    }

    /**
     * 利用Set列表去重,要求<T>必须实现hashCode和equals方法
     */
    public static <T> void removeDuplicate(Collection<T> list) {
        LinkedHashSet<T> set = new LinkedHashSet<>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
    }

    /**
     * 比较大小,左边的必须比右边小
     */
    public static void assertSize(int left, int right) {
        if (left > right) {
            throw new IllegalArgumentException("right should bigger than left , but find " + left + " > " + right);
        }
    }

    /**
     * 某个域的范围
     */
    public static void assertRange(CronPosition cronPosition, int value) {
        int min = cronPosition.getMin();
        int max = cronPosition.getMax();
        if (value < min || value > max) {
            throw new IllegalArgumentException(cronPosition.name() + " 域[" + min + " , " + max + "],  but find " + value);
        }
    }
}
