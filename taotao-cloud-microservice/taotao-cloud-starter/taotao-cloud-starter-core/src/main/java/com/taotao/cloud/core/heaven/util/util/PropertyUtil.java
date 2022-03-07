package com.taotao.cloud.core.heaven.util.util;


import com.taotao.cloud.core.heaven.support.tuple.impl.Pair;
import com.taotao.cloud.core.heaven.util.lang.StringUtil;

/**
 * 属性工具类
 *
 */
public final class PropertyUtil {

    private PropertyUtil(){}

    /**
     * 对字符串进行分割
     * @param text 原始文本
     * @return 结果
     * @since 0.1.123
     */
    public static Pair<String, String> getPropertyPair(final String text) {
        if(StringUtil.isEmpty(text)) {
            return Pair.of(null, null);
        }

        // 根据第一个 = 进行分割
        int index = text.indexOf("=");
        if(index < 0) {
            return Pair.of(text, null);
        }

        String key = text.substring(0, index);
        String value = text.substring(index+1);
        return Pair.of(key, value);
    }

    public static void main(String[] args) {
        System.out.println(getPropertyPair(null));
        System.out.println(getPropertyPair("=123"));
        System.out.println(getPropertyPair("key=123"));
    }

}
