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

import com.taotao.cloud.payment.biz.jeepay.jeepay.exception.APIConnectionException;
import com.taotao.cloud.payment.biz.jeepay.jeepay.util.StreamUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpURL连接客户端
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @since 2021-06-08 11:00
 */
public class HttpURLConnectionClient extends HttpClient {

    public HttpURLConnectionClient() {
        super();
    }

    @Override
    public APIJeepayResponse request(APIJeepayRequest request) throws APIConnectionException {
        HttpURLConnection conn = null;

        try {
            conn = createJeepayConnection(request);

            // trigger the request
            int responseCode = conn.getResponseCode();
            HttpHeaders headers = HttpHeaders.of(conn.getHeaderFields());
            String responseBody;

            if (responseCode >= 200 && responseCode < 300) {
                responseBody = StreamUtils.readToEnd(conn.getInputStream(), APIResource.CHARSET);
            } else {
                responseBody = StreamUtils.readToEnd(conn.getErrorStream(), APIResource.CHARSET);
            }

            return new APIJeepayResponse(responseCode, responseBody, headers);
        } catch (IOException e) {
            throw new APIConnectionException(
                    String.format("请求Jeepay(%s)异常,请检查网络或重试.异常信息:%s", request.getUrl(), e.getMessage()), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    static HttpHeaders getHeaders(APIJeepayRequest request) {
        Map<String, List<String>> userAgentHeadersMap = new HashMap<>();

        userAgentHeadersMap.put(
                "User-Agent",
                Collections.singletonList(
                        buildUserAgentString(request.getOptions().getVersion())));
        userAgentHeadersMap.put(
                "X-Jeepay-Client-User-Agent",
                Collections.singletonList(
                        buildXJeepayClientUserAgentString(request.getOptions().getVersion())));

        return request.getHeaders().withAdditionalHeaders(userAgentHeadersMap);
    }

    private static HttpURLConnection createJeepayConnection(APIJeepayRequest request) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) request.url.openConnection();

        conn.setConnectTimeout(request.options.getConnectTimeout());
        conn.setReadTimeout(request.options.getReadTimeout());
        conn.setUseCaches(false);
        for (Map.Entry<String, List<String>> entry : getHeaders(request).map().entrySet()) {
            conn.setRequestProperty(entry.getKey(), StringUtils.join(",", entry.getValue()));
        }

        conn.setRequestMethod(request.method.name());

        // 如有其他业务参数，可在此处增加

        if (request.content != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", request.content.contentType);

            try (OutputStream output = conn.getOutputStream()) {
                output.write(request.content.byteArrayContent);
            }
        }

        return conn;
    }
}
