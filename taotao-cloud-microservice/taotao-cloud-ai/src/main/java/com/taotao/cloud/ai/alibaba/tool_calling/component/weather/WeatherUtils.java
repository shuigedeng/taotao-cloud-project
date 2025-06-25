package com.taotao.cloud.ai.alibaba.tool_calling.component.weather;

import cn.hutool.extra.pinyin.PinyinUtil;

/**
 * @author yingzi
 * @date 2025/3/26:13:26
 */
public class WeatherUtils {

    public static String preprocessLocation(String location) {
        if (containsChinese(location)) {
            return PinyinUtil.getPinyin(location, "");
        }
        return location;
    }

    public static boolean containsChinese(String str) {
        return str.matches(".*[\u4e00-\u9fa5].*");
    }
}
