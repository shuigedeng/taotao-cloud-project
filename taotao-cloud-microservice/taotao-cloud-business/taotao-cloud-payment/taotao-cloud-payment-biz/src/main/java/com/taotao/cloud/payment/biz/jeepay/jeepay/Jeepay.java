/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.jeepay.jeepay;

/**
 * Jeepay数据对象
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public abstract class Jeepay {

    public static final String LIVE_API_BASE = "https://pay.jeepay.vip";
    public static final String VERSION = "1.0";
    public static final String DEFAULT_SIGN_TYPE = "MD5";
    public static final String API_VERSION_NAME = "version";
    public static final String API_SIGN_TYPE_NAME = "signType";
    public static final String API_SIGN_NAME = "sign";
    public static final String API_REQ_TIME_NAME = "reqTime";

    /** 默认时间格式 */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** Date默认时区 */
    public static final String DATE_TIMEZONE = "GMT+8";

    public static String acceptLanguage = "zh-CN";

    public static volatile String mchNo;

    public static volatile String appId;

    /** 私钥 */
    public static volatile String apiKey;

    /** API 地址 */
    private static volatile String apiBase = LIVE_API_BASE;

    public static volatile String privateKey;
    public static volatile String privateKeyPath;

    public static Boolean DEBUG = false;

    public static final int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 80 * 1000;

    private static volatile int connectTimeout = -1;
    private static volatile int readTimeout = -1;

    private static volatile int maxNetworkRetries = 1;

    public static void overrideApiBase(final String overriddenApiBase) {
        apiBase = overriddenApiBase;
    }

    public static String getApiBase() {
        return apiBase;
    }

    public static void setApiBase(String apiBase) {
        Jeepay.apiBase = apiBase;
    }

    /**
     * 网络连接超时时间
     *
     * @return
     */
    public static int getConnectTimeout() {
        if (connectTimeout == -1) {
            return DEFAULT_CONNECT_TIMEOUT;
        }
        return connectTimeout;
    }

    /**
     * 设置网络连接超时时间 (毫秒)
     *
     * @param timeout
     */
    public static void setConnectTimeout(final int timeout) {
        connectTimeout = timeout;
    }

    /**
     * 数据读取超时时间
     *
     * @return
     */
    public static int getReadTimeout() {
        if (readTimeout == -1) {
            return DEFAULT_READ_TIMEOUT;
        }
        return readTimeout;
    }

    /**
     * 设置数据读取超时时间 (毫秒) 不同接口的耗时时间不一样，部分接口的耗时可能比较长。
     *
     * @param timeout
     */
    public static void setReadTimeout(final int timeout) {
        readTimeout = timeout;
    }

    /**
     * 连接失败时的最大重试次数
     *
     * @return
     */
    public static int getMaxNetworkRetries() {
        return maxNetworkRetries;
    }

    /**
     * 设置连接失败时的最大重试次数
     *
     * @param numRetries
     */
    public static void setMaxNetworkRetries(final int numRetries) {
        maxNetworkRetries = numRetries;
    }

    public static String getAcceptLanguage() {
        return acceptLanguage;
    }

    public static void setAcceptLanguage(String acceptLanguage) {
        Jeepay.acceptLanguage = acceptLanguage;
    }
}
