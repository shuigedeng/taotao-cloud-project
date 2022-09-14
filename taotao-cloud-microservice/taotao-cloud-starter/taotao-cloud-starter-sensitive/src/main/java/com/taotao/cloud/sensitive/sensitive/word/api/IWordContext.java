package com.taotao.cloud.sensitive.sensitive.word.api;

import java.util.Map;

public interface IWordContext {

    /**
     * 是否忽略大小写
     * @return 是否
     */
    boolean ignoreCase();

    /**
     * 是否忽略半角圆角
     * @return 是否
     */
    boolean ignoreWidth();

    /**
     * 是否忽略数字格式
     * @return 是否
     */
    boolean ignoreNumStyle();

    /**
     * 设置是否忽略大小写
     * @param ignoreCase 是否忽略大小写
     * @return this
     */
    IWordContext ignoreCase(boolean ignoreCase);

    /**
     * 设置是否忽略半角圆角
     * @param ignoreWidth 是否忽略半角圆角
     * @return this
     */
    IWordContext ignoreWidth(boolean ignoreWidth);

    /**
     * 设置是否忽略半角圆角
     * @param ignoreNumStyle 是否忽略半角圆角
     * @return this
     */
    IWordContext ignoreNumStyle(boolean ignoreNumStyle);

    /**
     * 忽略中文繁简体格式
     * @return 是否
     */
    boolean ignoreChineseStyle();

    /**
     * 设置是否忽略中文繁简体格式
     * @param ignoreChineseStyle 是否忽略
     * @return this
     */
    IWordContext ignoreChineseStyle(final boolean ignoreChineseStyle);

    /**
     * 获取敏感词信息
     * @return 敏感词
     */
    Map sensitiveWordMap();

    /**
     * 敏感词信息
     * @param map map 信息
     * @return this
     */
    IWordContext sensitiveWordMap(final Map map);

    /**
     * 敏感数字检测
     * @return 数字检测
     */
    boolean sensitiveCheckNum();

    /**
     * 设置敏感数字检测
     * @param sensitiveCheckNum 数字格式检测
     * @return this
     */
    IWordContext sensitiveCheckNum(final boolean sensitiveCheckNum);

    /**
     * 是否进行邮箱检测
     * @return this
     */
    boolean sensitiveCheckEmail();

    /**
     * 设置敏感邮箱检测
     * @param sensitiveCheckEmail 是否检测
     * @return this
     */
    IWordContext sensitiveCheckEmail(final boolean sensitiveCheckEmail);

    /**
     * 敏感链接检测
     * @return 是否启用
     */
    boolean sensitiveCheckUrl();

    /**
     * 设置敏感邮箱检测
     * @param sensitiveCheckUrl 是否检测
     * @return this
     */
    IWordContext sensitiveCheckUrl(final boolean sensitiveCheckUrl);

    /**
     * 忽略英文的写法
     * @return 数字检测
     */
    boolean ignoreEnglishStyle();

    /**
     * 设置忽略英文的写法
     * @param ignoreEnglishStyle 是否忽略
     * @return this
     */
    IWordContext ignoreEnglishStyle(final boolean ignoreEnglishStyle);

    /**
     * 忽略重复词
     * @return 是否忽略
     */
    boolean ignoreRepeat();

    /**
     * 设置忽略重复词
     * @param ignoreRepeat 是否忽略
     * @return this
     */
    IWordContext ignoreRepeat(final boolean ignoreRepeat);

}
