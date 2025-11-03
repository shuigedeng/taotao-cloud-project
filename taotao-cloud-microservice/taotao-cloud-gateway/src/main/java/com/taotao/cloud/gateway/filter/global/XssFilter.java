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

import static cn.hutool.http.HtmlUtil.cleanHtmlTag;
import static com.taotao.cloud.gateway.utils.WebFluxUtils.isJsonRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.properties.XssProperties;
import io.netty.buffer.ByteBufAllocator;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.*;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Gatewayfilter 实现方式上，有两种过滤器； AbstractGatewayFilterFactory  GlobalFilter
 *
 * 1）局部过滤器：通过 spring.cloud.gateway.routes.filters 配置在具体路由下，只作用在当前路由上；自带的过滤器都可以配置或者自定义按照自带过滤器的方式。如果配置
 *
 * 2）全局过滤器：不需要在配置文件中配置，作用在所有的路由上；实现 GlobalFilter 接口即可。
 * spring.cloud.gateway.default-filters 上会对所有路由生效也算是全局的过滤器；但是这些过滤器的实现上都是要实现GatewayFilterFactory接口。
 *
 *
 * GlobalFilter WebFilter 区别
 * 应用范围：GlobalFilter是全局的，应用于所有的路由；而WebFilter可以应用于特定的路由或请求路径。
 *
 * 灵活性：由于WebFilter可以根据条件来决定是否应用过滤器，因此它比GlobalFilter更加灵活。
 *
 * 使用方式：在使用上，你需要根据具体的需求选择适合的过滤器类型，并在过滤器中定义相应的过滤逻辑。
 *
 * 使用场景
 * 当你需要执行一些全局的操作，比如日志记录、异常处理等时，可以使用GlobalFilter。
 *
 * 当你需要根据特定的路由或请求路径执行不同的过滤逻辑时，可以使用WebFilter。
 *
 * 无论你选择使用GlobalFilter还是WebFilter，都需要确保正确地实现过滤逻辑，并在Spring Cloud Gateway的配置中正确配置它们。
 *
 * 第三执行 跨站脚本过滤器
 */
@Component
@AllArgsConstructor
// @ConditionalOnProperty(value = "security.xss.enabled", havingValue = "true")
public class XssFilter implements GlobalFilter, Ordered {

    private final XssProperties xss;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LogUtils.info(" xss跨站脚本过滤器!");

        ServerHttpRequest request = exchange.getRequest();
        // GET DELETE 不过滤
        HttpMethod method = request.getMethod();
        if (method == HttpMethod.GET || method == HttpMethod.DELETE) {
            return chain.filter(exchange);
        }

        // 非json类型，不过滤
        if (!isJsonRequest(exchange)) {
            return chain.filter(exchange);
        }

        // excludeUrls 不过滤
        String url = request.getURI().getPath();
        if (matches(url, xss.getExcludeUrls())) {
            return chain.filter(exchange);
        }

        ServerHttpRequestDecorator httpRequestDecorator = requestDecorator(exchange);
        return chain.filter(exchange.mutate().request(httpRequestDecorator).build());
    }

    private ServerHttpRequestDecorator requestDecorator(ServerWebExchange exchange) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                Flux<DataBuffer> body = super.getBody();
                return body.buffer()
                        .map(
                                dataBuffers -> {
                                    DataBufferFactory dataBufferFactory =
                                            new DefaultDataBufferFactory();
                                    DataBuffer join = dataBufferFactory.join(dataBuffers);
                                    byte[] content = new byte[join.readableByteCount()];
                                    join.read(content);
                                    DataBufferUtils.release(join);
                                    String bodyStr = new String(content, StandardCharsets.UTF_8);
                                    // 防xss攻击过滤
                                    bodyStr = cleanHtmlTag(bodyStr);
                                    // 转成字节
                                    byte[] bytes = bodyStr.getBytes();
                                    NettyDataBufferFactory nettyDataBufferFactory =
                                            new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
                                    DataBuffer buffer =
                                            nettyDataBufferFactory.allocateBuffer(bytes.length);
                                    buffer.write(bytes);
                                    return buffer;
                                });
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                // 由于修改了请求体的body，导致content-length长度不确定，因此需要删除原先的content-length
                httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                return httpHeaders;
            }
        };
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs) {
        if (isEmpty(str) || CollUtil.isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置: ? 表示单个字符; * 表示一层路径内的任意字符串，不可跨层级; ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return boolean
     * @since 2023-08-17 11:41:58
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return StrUtil.isEmpty(str);
    }
}
