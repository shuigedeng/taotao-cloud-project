package com.taotao.cloud.web.docx4j.input.constants;

/**
 * 常用的正则表达式
 */
public interface RegularExpressions {
    /**
     * 邮箱匹配只允许英文字母、数字、下划线、英文句号、以及中划线组成 {@value}
     */
    String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 电话号码{@value}
     */
    String MOBILE = "^1(3|4|5|6|7|8|9)\\d{9}$";
    /**
     * 域名{@value}
     */
    String DOMAIN = "^((http:\\/\\/)|(https:\\/\\/))?([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}(\\/)";
    /**
     * ip地址{@value}
     */
    String IP = "((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))";
    /**
     * 汉字
     */
    String CHINESE_CHARACTER = "^[\u4e00-\u9fa5]{0,}$";
    /**
     * 整数
     */
    String INTEGER = "^-?[1-9]\\d*$";
    /**
     * 正整数
     */
    String POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * 负整数
     */
    String NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * 浮点数
     */
    String DECIMAL = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
    /**
     * 正浮点数
     */
    String POSITIVE_DECIMAL = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * 负浮点数
     */
    String NEGATIVE_DECIMAL = "^-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$";
}
