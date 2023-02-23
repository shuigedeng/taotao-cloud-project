package com.taotao.cloud.operation.api.enums;

/**
 * 文章分类枚举
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
public enum ArticleEnum {

    /**
     * 关于我们
     */
    ABOUT,
    /**
     * 隐私政策
     */
    PRIVACY_POLICY,
    /**
     * 用户协议
     */
    USER_AGREEMENT,
    /**
     * 证照信息
     */
    LICENSE_INFORMATION,
    /**
     * 店铺入驻
     */
    STORE_REGISTER,
    /**
     * 其他文章
     */
    OTHER;

    public String value() {
        return this.name();
    }

}
