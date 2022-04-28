package com.taotao.cloud.oss.artislong.core.up.constant;

import com.upyun.RestManager;

/**
 * api域
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:36
 */
public enum ApiDomain {
    /**
     * 根据网络条件自动选择接入点:v0.api.upyun.com
     */
    ED_AUTO(RestManager.ED_AUTO),
    /**
     * 电信接入点:v1.api.upyun.com
     */
    ED_TELECOM(RestManager.ED_TELECOM),
    /**
     * 联通网通接入点:v2.api.upyun.com
     */
    ED_CNC(RestManager.ED_CNC),
    /**
     * 移动铁通接入点:v3.api.upyun.com
     */
    ED_CTT(RestManager.ED_CTT);

    private final String value;

    ApiDomain(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
