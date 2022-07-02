package com.taotao.cloud.sys.biz.support.image_combiner.enums;

/**
 * OutputFormat 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:48:03
 */
public enum OutputFormat {
    JPG("jpg"),
    PNG("png"),
    BMP("bmp");

    public final String name;

    OutputFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
