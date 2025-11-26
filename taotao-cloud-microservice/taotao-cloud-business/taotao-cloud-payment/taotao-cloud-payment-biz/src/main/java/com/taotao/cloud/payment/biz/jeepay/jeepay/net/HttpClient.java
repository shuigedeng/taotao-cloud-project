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

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.payment.biz.jeepay.jeepay.exception.APIConnectionException;
import com.taotao.cloud.payment.biz.jeepay.jeepay.exception.JeepayException;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Http请求客户端
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public abstract class HttpClient {

    /** 网络故障后尝试发送HTTP请求之间的最大延迟时间 */
    public static final long maxNetworkRetriesDelay = 5000;

    /** 网络故障后尝试发送HTTP请求之间的最小延迟时间 */
    public static final long minNetworkRetriesDelay = 500;

    /** 是否网络重试休眠 */
    boolean networkRetriesSleep = true;

    public HttpClient() {}

    /**
     * 发送http请求
     *
     * @param request
     * @return
     * @throws JeepayException
     */
    public abstract APIJeepayResponse request(APIJeepayRequest request) throws JeepayException;

    /**
     * 发送请求到Jeepay的API(支持重试)
     *
     * @param request
     * @return
     * @throws JeepayException
     */
    public APIJeepayResponse requestWithRetries(APIJeepayRequest request) throws JeepayException {
        APIConnectionException requestException = null;
        APIJeepayResponse response = null;
        int retry = 0;

        while (true) {
            requestException = null;

            try {
                response = this.request(request);
            } catch (APIConnectionException e) {
                requestException = e;
            }

            if (!this.shouldRetry(retry, requestException, request, response)) {
                break;
            }

            retry += 1;

            try {
                Thread.sleep(this.sleepTime(retry));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (requestException != null) {
            throw requestException;
        }

        response.setNumRetries(retry);

        return response;
    }

    protected static String buildUserAgentString(String version) {
        return String.format("Jeepay/v1 JavaBindings/%s", version);
    }

    protected static String buildXJeepayClientUserAgentString(String version) {
        String[] propertyNames = {
            "os.name", "os.version", "os.arch", "java.version", "java.vendor", "java.vm.version", "java.vm.vendor"
        };

        Map<String, String> propertyMap = new HashMap<>();
        for (String propertyName : propertyNames) {
            propertyMap.put(propertyName, System.getProperty(propertyName));
        }
        propertyMap.put("bindings.version", version);
        propertyMap.put("lang", "Java");
        propertyMap.put("publisher", "Jeepay");
        return JSON.toJSONString(propertyMap);
    }

    private boolean shouldRetry(
            int numRetries, JeepayException exception, APIJeepayRequest request, APIJeepayResponse response) {
        // Do not retry if we are out of retries.
        if (numRetries >= request.options.getMaxNetworkRetries()) {
            return false;
        }

        // Retry on connection error.
        if ((exception != null)
                && (exception.getCause() != null)
                && (exception.getCause() instanceof ConnectException)) {
            return true;
        }

        // Retry on 500, 503, and other internal errors.
        if ((response != null) && (response.getResponseCode() >= 500)) {
            return true;
        }

        return false;
    }

    private long sleepTime(int numRetries) {
        if (!networkRetriesSleep) {
            return 0;
        }

        long delay = (long) (minNetworkRetriesDelay * Math.pow(2, numRetries - 1));

        if (delay > maxNetworkRetriesDelay) {
            delay = maxNetworkRetriesDelay;
        }

        double jitter = ThreadLocalRandom.current().nextDouble(0.75, 1.0);
        delay = (long) (delay * jitter);

        if (delay < minNetworkRetriesDelay) {
            delay = minNetworkRetriesDelay;
        }

        return delay;
    }

    private static String getRequestURIFromURL(URL url) {
        String path = url.getPath();
        String query = url.getQuery();
        if (query == null) {
            return path;
        }
        return path + "?" + query;
    }
}
