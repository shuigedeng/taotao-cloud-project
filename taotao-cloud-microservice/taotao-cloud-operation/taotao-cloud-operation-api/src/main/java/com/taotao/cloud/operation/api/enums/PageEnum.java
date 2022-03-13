
package com.taotao.cloud.operation.api.enums;

/**
 * 楼层装修枚举
 *
 */
public enum PageEnum {

    /**
     * 首页
     */
    INDEX,

    /**
     * 店铺
     */
    STORE,

    /**
     * 专题页面
     */
    SPECIAL;

    public String value() {
        return this.name();
    }

}
