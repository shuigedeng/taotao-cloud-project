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

package com.taotao.cloud.payment.biz.jeepay.jeepay.net;

import com.taotao.cloud.payment.biz.jeepay.jeepay.Jeepay;

/**
 * 请求参数选项内容
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class RequestOptions {

    private String uri;
    private String version;
    private String signType;
    private String appId;
    private String apiKey;

    private int connectTimeout;
    private int readTimeout;
    private int maxNetworkRetries;
    private String acceptLanguage;

    public static RequestOptions getDefault(String uri, String version) {
        return new RequestOptions(
                uri,
                version,
                Jeepay.DEFAULT_SIGN_TYPE,
                Jeepay.appId,
                Jeepay.apiKey,
                Jeepay.getConnectTimeout(),
                Jeepay.getReadTimeout(),
                Jeepay.getMaxNetworkRetries(),
                Jeepay.getAcceptLanguage());
    }

    private RequestOptions(
            String uri,
            String version,
            String signType,
            String appId,
            String apiKey,
            int connectTimeout,
            int readTimeout,
            int maxNetworkRetries,
            String acceptLanguage) {
        this.uri = uri;
        this.version = version;
        this.signType = signType;
        this.appId = appId;
        this.apiKey = apiKey;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxNetworkRetries = maxNetworkRetries;
        this.acceptLanguage = acceptLanguage;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public String getSignType() {
        return signType;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getMaxNetworkRetries() {
        return maxNetworkRetries;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public static RequestOptionsBuilder builder() {
        return new RequestOptionsBuilder();
    }

    public static class RequestOptionsBuilder {
        private String uri;
        private String version;
        private String signType;
        private String appId;
        private String apiKey;
        private int connectTimeout;
        private int readTimeout;
        private int maxNetworkRetries;
        private String acceptLanguage;

        public RequestOptionsBuilder() {
            this.signType = Jeepay.DEFAULT_SIGN_TYPE;
            this.appId = Jeepay.appId;
            this.apiKey = Jeepay.apiKey;
            this.connectTimeout = Jeepay.getConnectTimeout();
            this.readTimeout = Jeepay.getReadTimeout();
            this.maxNetworkRetries = Jeepay.getMaxNetworkRetries();
            this.acceptLanguage = Jeepay.getAcceptLanguage();
        }

        public String getUri() {
            return uri;
        }

        public RequestOptionsBuilder setUri(String uri) {
            this.uri = normalizeApiUri(uri);
            return this;
        }

        public String getVersion() {
            return version;
        }

        public RequestOptionsBuilder setVersion(String version) {
            this.version = version;
            return this;
        }

        public String getSignType() {
            return signType;
        }

        public RequestOptionsBuilder setSignType(String signType) {
            this.signType = signType;
            return this;
        }

        public String getAppId() {
            return appId;
        }

        public RequestOptionsBuilder setAppId(String appId) {
            this.apiKey = normalizeAppId(appId);
            return this;
        }

        public RequestOptionsBuilder clearAppId() {
            this.appId = null;
            return this;
        }

        public String getApiKey() {
            return apiKey;
        }

        public RequestOptionsBuilder setApiKey(String apiKey) {
            this.apiKey = normalizeApiKey(apiKey);
            return this;
        }

        public RequestOptionsBuilder clearApiKey() {
            this.apiKey = null;
            return this;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public RequestOptionsBuilder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public int getReadTimeout() {
            return readTimeout;
        }

        public RequestOptionsBuilder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public int getMaxNetworkRetries() {
            return maxNetworkRetries;
        }

        public RequestOptionsBuilder setMaxNetworkRetries(int maxNetworkRetries) {
            this.maxNetworkRetries = maxNetworkRetries;
            return this;
        }

        public String getAcceptLanguage() {
            return acceptLanguage;
        }

        public RequestOptionsBuilder setAcceptLanguage(String acceptLanguage) {
            this.acceptLanguage = normalizeAcceptLanguage(acceptLanguage);
            return this;
        }

        public RequestOptions build() {
            return new RequestOptions(
                    normalizeApiUri(this.uri),
                    version,
                    signType,
                    normalizeAppId(this.appId),
                    normalizeApiKey(this.apiKey),
                    connectTimeout,
                    readTimeout,
                    maxNetworkRetries,
                    acceptLanguage);
        }
    }

    private static String normalizeApiUri(String apiUri) {
        if (apiUri == null) {
            throw new InvalidRequestOptionsException("接口URI不能为空!");
        }
        if (apiUri.startsWith("/")) {
            throw new InvalidRequestOptionsException("接口URI(" + apiUri + ")不能以'/'开头");
        }
        return apiUri;
    }

    private static String normalizeAppId(String appId) {
        if (appId == null) {
            return null;
        }
        String normalized = appId.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("appId不能为空!");
        }
        return normalized;
    }

    private static String normalizeApiKey(String apiKey) {
        if (apiKey == null) {
            return null;
        }
        String normalized = apiKey.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("API key不能为空!");
        }
        return normalized;
    }

    private static String normalizeAcceptLanguage(String acceptLanguage) {
        if (acceptLanguage == null) {
            return null;
        }
        String normalized = acceptLanguage.trim();
        if (normalized.isEmpty()) {
            throw new InvalidRequestOptionsException("Accept-Language不能空!");
        }
        return normalized;
    }

    public static class InvalidRequestOptionsException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public InvalidRequestOptionsException(String message) {
            super(message);
        }
    }
}
