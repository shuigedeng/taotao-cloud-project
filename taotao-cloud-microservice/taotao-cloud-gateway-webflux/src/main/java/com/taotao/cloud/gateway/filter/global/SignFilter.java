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

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.secure.MD5Utils;
import com.taotao.boot.common.utils.secure.RSAUtils;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.gateway.properties.FilterProperties;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 第十执行 签名过滤器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-08 13:19:14
 */
@Component
@ConditionalOnProperty(
        prefix = FilterProperties.PREFIX,
        name = "sign",
        havingValue = "true",
        matchIfMissing = true)
public class SignFilter implements GlobalFilter, Ordered {

    public static final String PRIVATE_KEY =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIW1OUvrUiogZ359EtSEnQyOyoVcUmzAKiEnjmbnVa9vEM9E/eCWVbRYKGTvgPkkQ6kwNJajgLtF+gaUFE1buRDqpga6RhnmOBinOhPT6Cneif3p9BcTJAnKy/3GJM8h2ZJddVWPUcA4nDb1FvPEhUpRLPM9e8S1dFO0ILX7CQAlAgMBAAECgYBC4amtbiKFa/wY61tV7pfYRjzLhKi+OUlZmD3E/4Z+4KGZ7DrJ8qkgMtDR3HO5LAikQrare1HTW2d7juqw32ascu+uDObf4yrYNKin+ZDLUYvIDfLhThPxnZJwQ/trdtfxO3VM//XbwZacmwYbAsYW/3QPUXwwOPAgbC2oth8kqQJBANKLyXcdjZx4cwJVl7xNeC847su8y6bPpcBASsaQloCIPiNBIg1h76dpfEGIQBYWJWbBsxtHe/MhOmz7fNFDS2sCQQCiktYZR0dZNH4eNX329LoRuBiltpr9tf36rVOlKr1GSHkLYEHF2qtyXV2mdrY8ZWpvuo3qm1oSLaqmop2rN9avAkBHk85B+IIUF77BpGeZVJzvMOO9z8lMRHuNCE5jgvQnbinxwkrZUdovh+T+QlvHJnBApslFFOBGn51FP5oHamFRAkEAmwZmPsinkrrpoKjlqz6GyCrC5hKRDWoj/IyXfKKaxpCJTH3HeoIghvfdO8Vr1X/n1Q8SESt+4mLFngznSMQAZQJBAJx07bCFYbA2IocfFV5LTEYTIiUeKdue2NP2yWqZ/+tB5H7jNwQTJmX1mn0W/sZm4+nJM7SjfETpNZhH49+rV6U=";

    private static final String ERROR_MESSAGE = "拒绝服务";

    private final RedisRepository redisRepository;

    public SignFilter(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        LogUtils.info("访问地址：" + request.getURI());

        // 1 获取时间戳
        Long dateTimestamp = getDateTimestamp(exchange.getRequest().getHeaders());

        // 2 获取RequestId
        String requestId = getRequestId(exchange.getRequest().getHeaders());

        // 3 获取签名
        String sign = getSign(exchange.getRequest().getHeaders());

        // 4 如果不是登录校验Token
        String requestUrl = exchange.getRequest().getPath().value();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        if (!pathMatcher.match("/user/login", requestUrl)) {
            String token = exchange.getRequest().getHeaders().getFirst("token");
            if (StringUtils.isBlank(token)) {
                return ResponseUtils.fail(exchange, "无效的token");
            }
        }

        // 5 修改请求参数,获取请求参数
        Map<String, Object> paramMap;
        try {
            paramMap = updateRequestParam(exchange);
        } catch (Exception e) {
            return ResponseUtils.fail(exchange, "无效的url");
        }

        // 6 获取请求体,修改请求体
        ServerRequest serverRequest =
                ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<String> modifiedBody =
                serverRequest
                        .bodyToMono(String.class)
                        .flatMap(
                                body -> {
                                    String encrypt = RSAUtils.decrypt(body, PRIVATE_KEY);
                                    JSONObject jsonObject = JSON.parseObject(encrypt);
                                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                                        paramMap.put(entry.getKey(), entry.getValue());
                                    }
                                    checkSign(sign, dateTimestamp, requestId, paramMap);
                                    return Mono.just(encrypt);
                                });

        // 创建BodyInserter修改请求体
        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter =
                BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        // 创建CachedBodyOutputMessage并且把请求param加入,初始化校验信息
        MyCachedBodyOutputMessage outputMessage = new MyCachedBodyOutputMessage(exchange, headers);
        outputMessage.initial(paramMap, requestId, sign, dateTimestamp);

        return bodyInserter
                .insert(outputMessage, new BodyInserterContext())
                .then(
                        Mono.defer(
                                () -> {
                                    ServerHttpRequestDecorator decorator =
                                            new ServerHttpRequestDecorator(exchange.getRequest()) {
                                                @Override
                                                public Flux<DataBuffer> getBody() {
                                                    Flux<DataBuffer> body = outputMessage.getBody();
                                                    if (body.equals(Flux.empty())) {
                                                        // 验证签名
                                                        checkSign(
                                                                outputMessage.getSign(),
                                                                outputMessage.getDateTimestamp(),
                                                                outputMessage.getRequestId(),
                                                                outputMessage.getParamMap());
                                                    }
                                                    return outputMessage.getBody();
                                                }
                                            };
                                    return chain.filter(
                                            exchange.mutate().request(decorator).build());
                                }));
    }

    public void checkSign(
            String sign, Long dateTimestamp, String requestId, Map<String, Object> paramMap) {
        String str = JSON.toJSONString(paramMap) + requestId + dateTimestamp;
        String tempSign = MD5Utils.encrypt(str);
        assert tempSign != null;
        if (!tempSign.equals(sign)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    /**
     * 修改前端传的参数
     */
    private Map<String, Object> updateRequestParam(ServerWebExchange exchange)
            throws NoSuchFieldException, IllegalAccessException {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String query = uri.getQuery();

        if (StringUtils.isNotBlank(query) && query.contains("param")) {
            String[] split = query.split("=");
            String param = RSAUtils.decrypt(split[1], PRIVATE_KEY);
            Field targetQuery = uri.getClass().getDeclaredField("query");
            targetQuery.setAccessible(true);
            targetQuery.set(uri, param);
            assert param != null;
            return getParamMap(param);
        }

        return new TreeMap<>();
    }

    private Map<String, Object> getParamMap(String param) {
        Map<String, Object> map = new TreeMap<>();
        String[] split = param.split("&");
        for (String str : split) {
            String[] params = str.split("=");
            map.put(params[0], params[1]);
        }
        return map;
    }

    private String getSign(HttpHeaders headers) {
        List<String> list = headers.get("sign");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return list.get(0);
    }

    private Long getDateTimestamp(HttpHeaders httpHeaders) {
        List<String> list = httpHeaders.get("timestamp");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        long timestamp = Long.parseLong(list.get(0));
        long currentTimeMillis = System.currentTimeMillis();

        // 有效时长为5分钟
        if (currentTimeMillis - timestamp > 1000 * 60 * 5) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return timestamp;
    }

    private String getRequestId(HttpHeaders headers) {
        List<String> list = headers.get("requestId");
        if (CollectionUtils.isEmpty(list)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        String requestId = list.get(0);

        // 如果requestId存在redis中直接返回
        String temp = (String) redisRepository.get(requestId);
        if (StringUtils.isNotBlank(temp)) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        redisRepository.setExpire(requestId, requestId, 5, TimeUnit.MINUTES);
        return requestId;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }

    public static class MyCachedBodyOutputMessage extends CachedBodyOutputMessage {

        private Map<String, Object> paramMap;

        private Long dateTimestamp;

        private String requestId;

        private String sign;

        public MyCachedBodyOutputMessage(ServerWebExchange exchange, HttpHeaders httpHeaders) {
            super(exchange, httpHeaders);
        }

        public void initial(
                Map<String, Object> paramMap, String requestId, String sign, Long dateTimestamp) {
            this.paramMap = paramMap;
            this.requestId = requestId;
            this.sign = sign;
            this.dateTimestamp = dateTimestamp;
        }

        public Map<String, Object> getParamMap() {
            return paramMap;
        }

        public Long getDateTimestamp() {
            return dateTimestamp;
        }

        public String getRequestId() {
            return requestId;
        }

        public String getSign() {
            return sign;
        }
    }
}
