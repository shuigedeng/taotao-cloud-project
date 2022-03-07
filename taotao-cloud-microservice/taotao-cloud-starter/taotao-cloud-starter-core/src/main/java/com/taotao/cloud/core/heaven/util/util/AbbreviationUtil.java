/*
 * Copyright (c)  2019. houbinbin Inc.
 * gen-comment-plugin All rights reserved.
 */

package com.taotao.cloud.core.heaven.util.util;


import com.taotao.cloud.core.heaven.util.lang.StringUtil;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缩写工具类
 * 1. 需要添加自定义支持
 * 2. 或者提供对应的 filter
 * @author bbhou
 * @since 0.1.1
 */
public final class AbbreviationUtil {

    /**    
     *  缩写util    
     */    
    private AbbreviationUtil(){}

    /**    
     * 地图    
     */    
    private static final Map<String, String> MAP = new ConcurrentHashMap<>();

    static {
        MAP.put("impl", "implements");
        MAP.put("msg", "message");
        MAP.put("err", "error");
        MAP.put("e", "exception");
        MAP.put("ex", "exception");
        MAP.put("doc", "document");
        MAP.put("val", "value");
        MAP.put("num", "number");

        MAP.put("vo", "value object");
        MAP.put("dto", "data transfer object");

        MAP.put("gen", "generate");
        MAP.put("dir", "directory");
        MAP.put("init", "initialize");
        MAP.put("cfg", "config");
        MAP.put("arg", "argument");
        MAP.put("args", "arguments");
    }


    /**
     * 设置
     * @param shortName 简称
     * @param fullName 全程
     */
    public static void set(final String shortName, final String fullName) {
        MAP.put(shortName, fullName);
    }

    /**
     * 获取
     * @param shortName 简称
     * @return 全称
     */
    public static String get(final String shortName) {
        return MAP.get(shortName);
    }

    /**
     * 获取并提供默认值
     * @param shortName 简称
     * @param defaultValue 默认值
     * @return 对应的全称
     */
    public static String getOrDefault(final String shortName, final String defaultValue) {
        String value = MAP.get(shortName);

        if(StringUtil.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }


}
