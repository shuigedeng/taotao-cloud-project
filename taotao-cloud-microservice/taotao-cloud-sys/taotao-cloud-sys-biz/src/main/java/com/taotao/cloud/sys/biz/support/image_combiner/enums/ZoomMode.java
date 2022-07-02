package com.taotao.cloud.sys.biz.support.image_combiner.enums;

/**
 * ZoomMode 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:48:03
 */
public enum ZoomMode {
    /**
     * 原始比例，不缩放
     */
    Origin,
    /**
     * 指定宽度，高度按比例自动计算
     */
    Width,
    /**
     * 指定高度，宽度按比例自动计算
     */
    Height,
    /**
     * 指定高度和宽度，强制缩放
     */
    WidthHeight;
}
