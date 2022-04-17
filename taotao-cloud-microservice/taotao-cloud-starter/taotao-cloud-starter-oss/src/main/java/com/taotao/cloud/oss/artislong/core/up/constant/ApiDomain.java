package com.taotao.cloud.oss.artislong.core.up.constant;

import com.upyun.RestManager;

/**
 * @author 陈敏
 * @version ApiDomain.java, v 1.1 2022/2/18 14:19 chenmin Exp $
 * Created on 2022/2/18
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
