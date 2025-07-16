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

package com.taotao.cloud.gateway.filter.global;

import static com.taotao.boot.common.constant.CommonConstants.TTC_TRACE_ID;

import com.taotao.boot.common.constant.CommonConstants;
import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.TraceUtils;
import com.taotao.cloud.gateway.properties.FilterProperties;
import java.util.ArrayList;
import java.util.List;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 第八执行 打印请求和响应简要日志
 *
 * @author shuigedeng
 * @since 2020-7-16
 */
@Component
@ConditionalOnProperty(
        prefix = FilterProperties.PREFIX,
        name = "requestLog",
        havingValue = "true",
        matchIfMissing = true)
public class RequestLogFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "start_time";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getURI().getRawPath();
        String traceId = exchange.getRequest().getHeaders().getFirst(CommonConstants.TTC_TRACE_ID);
        if (StrUtil.isBlank(traceId)) {
            traceId = TraceUtils.getTraceId();
        }
        StringBuilder beforeReqLog = new StringBuilder();

        List<Object> beforeReqArgs = new ArrayList<>();
        beforeReqLog.append("\n\n================ TaoTao Cloud Request Start  ================\n");
        beforeReqLog.append("===> requestMethod: {}, requestUrl: {}, traceId: {}\n");
        String requestMethod = exchange.getRequest().getMethod().name();
        beforeReqArgs.add(requestMethod);
        beforeReqArgs.add(requestUrl);
        beforeReqArgs.add(traceId);

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String header = JsonUtils.toJSONString(headers);
        beforeReqLog.append("===> requestHeaders : {}\n");
        beforeReqArgs.add(header);
        beforeReqLog.append("================ TaoTao Cloud Request End =================\n");
        LogUtils.info(beforeReqLog.toString(), beforeReqArgs.toArray());

        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        exchange.getAttributes().put(TTC_TRACE_ID, traceId);
        return chain.filter(exchange)
                .then(
                        Mono.fromRunnable(
                                () -> {
                                    ServerHttpResponse response = exchange.getResponse();
                                    HttpHeaders httpHeaders = response.getHeaders();
                                    Long startTime = exchange.getAttribute(START_TIME);
                                    long executeTime = 0L;
                                    if (startTime != null) {
                                        executeTime = (System.currentTimeMillis() - startTime);
                                    }

                                    StringBuilder responseLog = new StringBuilder();
                                    List<Object> responseArgs = new ArrayList<>();
                                    responseLog.append(
                                            "\n\n================ TaoTao Cloud Response Start  ================\n");
                                    responseLog.append(
                                            "===> requestMethod: {}, requestUrl: {}, traceId: {}, executeTime: {}\n");
                                    responseArgs.add(requestMethod);
                                    responseArgs.add(requestUrl);
                                    responseArgs.add(exchange.getAttribute(TTC_TRACE_ID));
                                    responseArgs.add(executeTime + "ms");

                                    String httpHeader = JsonUtils.toJSONString(httpHeaders);
                                    responseLog.append("===> responseHeaders : {}\n");
                                    responseArgs.add(httpHeader);
                                    responseLog.append(
                                            "================  TaoTao Cloud Response End  =================\n");
                                    LogUtils.info(responseLog.toString(), responseArgs.toArray());

                                    exchange.getAttributes().remove(START_TIME);
                                    exchange.getAttributes().remove(TTC_TRACE_ID);
                                }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 8;
    }
}
