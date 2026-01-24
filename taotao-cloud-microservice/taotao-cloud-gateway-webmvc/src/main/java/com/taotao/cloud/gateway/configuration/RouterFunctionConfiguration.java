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

package com.taotao.cloud.gateway.configuration;


import cn.hutool.http.HttpStatus;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.captcha.util.CaptchaUtils;
import com.taotao.boot.common.constant.RedisConstants;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.json.JacksonUtils;
import com.taotao.boot.common.utils.context.ContextUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.monitor.collect.HealthCheckProvider;
import com.taotao.boot.monitor.model.Report;
import com.taotao.cloud.gateway.anti_reptile.constant.AntiReptileConsts;
import com.taotao.cloud.gateway.anti_reptile.handler.RefreshFormHandler;
import com.taotao.cloud.gateway.anti_reptile.handler.ValidateFormHandler;
import com.taotao.cloud.gateway.properties.ApiProperties;
import com.wf.captcha.ArithmeticCaptcha;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * # 使用配置文件方式配置路由
 * spring:
 *   cloud:
 *     gateway:
 *       server:
 *         webmvc:
 *           routes:
 *             # 配置路由到function demo服务
 *             - id: function_demo_route
 *               uri: http://localhost:8082
 *               predicates:
 *                 - Method=GET,PUT,DELETE,POST
 *                 - Path=/functionservletpath/**
 *               filters:
 *                 - AddRequestParameter=code, gateway for functiondemo
 *                 - AddRequestHeader=function-Request-Id,function_header_value
 *             - id: entitle_route
 *               uri: http://localhost:8081
 *               predicates:
 *                 - Method=GET,PUT,DELETE,POST
 *                 - Path=/entitleservice/info/**,/entitleservice/gatewayInfo/**,/entitleservice/user/**,/entitleservice/role/**
 *                 - predicateHeaderExists=custom_header
 *               filters:
 *                 - AddRequestParameter=code, gate way for entitleservice
 *                 - AddRequestHeader=entitle-Request-Id,entitle_header_value
 *                 - instrumentForFilter=req_header,rep_header
 * （java编程方式
 * 特殊路由配置信息
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 22:11
 */
@Configuration
@AllArgsConstructor
public class RouterFunctionConfiguration {

    private static final String FALLBACK = "/fallback";
    private static final String CODE = "/code";
    private static final String FAVICON = "/favicon.ico";
    private static final String HEALTH_REPORT = "/health/report";

    private final ObjectProvider<RefreshFormHandler> refreshFormHandlerObjectProvider;
    private final ObjectProvider<ValidateFormHandler> validateFormHandlerObjectProvider;

    // 当识别到路径为特定路径时，直接转发到对应的runtime上处理即可解决。
    //	@Bean
    //	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    //		return builder.routes()
    //			// 解决POST请求，BODY内容体为空，抛404错误。。
    //			.route(r -> r.method(HttpMethod.POST).or().method(HttpMethod.GET).and()
    //				.path("/xxx/jsapi/refundTransaction", "/xxx/app/refundTransaction",
    //					"/xxx/jsapi/transaction", "/xxx/app/transaction", "/xxx/alipay/transaction")
    //				.uri("lb://xxxx-xxx-runtime"))
    //			.build();
    //	}

    @Bean
    public RouterFunction<ServerResponse> routerFunction(
            FallbackHandler fallbackHandler,
            ImageCodeHandler imageCodeWebHandler,
            FaviconHandler faviconHandler,
            HealthReportHandler healthReportHandler,
            K8sHandler k8sHandler,
            ApiProperties apiProperties ) {
        RouterFunction<ServerResponse> routerFunction =
                RouterFunctions.route(
                                RequestPredicates.path(FALLBACK)
                                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                                fallbackHandler)
                        .andRoute(
                                RequestPredicates.GET(apiProperties.getBaseUri() + CODE)
                                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                                imageCodeWebHandler)
                        .andRoute(
                                RequestPredicates.GET(FAVICON)
                                        .and(RequestPredicates.accept(MediaType.IMAGE_PNG)),
                                faviconHandler)
                        .andRoute(
                                RequestPredicates.GET(HEALTH_REPORT)
                                        .and(RequestPredicates.accept(MediaType.ALL)),
                                healthReportHandler)
                        .andRoute(
                                RequestPredicates.GET("/k8s")
                                        .and(RequestPredicates.accept(MediaType.ALL)),
                                k8sHandler);

//        refreshFormHandlerObjectProvider.ifAvailable(
//                ( validateFormHandler ) -> {
//                    routerFunction.andRoute(
//                            RequestPredicates.GET(AntiReptileConsts.VALIDATE_REQUEST_URI)
//                                    .and(RequestPredicates.accept(MediaType.ALL)),
//                            validateFormHandler);
//                });
//        validateFormHandlerObjectProvider.ifAvailable(
//                ( refreshFormHandler ) -> {
//                    routerFunction.andRoute(
//                            RequestPredicates.GET(AntiReptileConsts.REFRESH_REQUEST_URI)
//                                    .and(RequestPredicates.accept(MediaType.ALL)),
//                            refreshFormHandler);
//                });

        return routerFunction;
    }

    /**
     * Hystrix 降级处理
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/4/29 22:11
     */
    @Component
    public static class FallbackHandler implements HandlerFunction<ServerResponse> {

        private static final int DEFAULT_PORT = 9700;

        @Override
        public Mono<ServerResponse> handle( ServerRequest serverRequest ) {
//            Route route = serverRequest.exchange().getAttribute(GATEWAY_ROUTE_ATTR);
//            System.out.printf("路由元数据: %s%n", route.getMetadata());
//
//            String originalUris =
//                    serverRequest
//                            .exchange()
//                            .getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
//            Optional<InetSocketAddress> socketAddress = serverRequest.remoteAddress();
//
//            Exception exception =
//                    serverRequest
//                            .exchange()
//                            .getAttribute(
//                                    ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR);
//            if (exception instanceof TimeoutException) {
//                LogUtils.error("服务超时", exception);
//            } else if (exception != null && exception.getMessage() != null) {
//                LogUtils.error("服务错误" + exception.getMessage(), exception);
//            } else {
//                LogUtils.error("服务错误", exception);
//            }

//            LogUtils.error(
//                    "网关执行请求:{}失败,请求主机: {},请求数据:{} 进行服务降级处理",
//                    originalUris,
//                    socketAddress.orElse(new InetSocketAddress(DEFAULT_PORT)).getHostString(),
//                    buildMessage(serverRequest));

            return ServerResponse.status(HttpStatus.HTTP_OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Result.fail("funciton访问频繁,请稍后重试")));
        }

//        private String buildMessage( ServerRequest request ) {
//            StringBuilder message = new StringBuilder("[");
//            message.append(request.method().name());
//            message.append(" ");
//            message.append(request.uri());
//            MultiValueMap<String, String> params = request.queryParams();
//            Map<String, String> map = params.toSingleValueMap();
//            if (!map.isEmpty()) {
//                message.append(" 请求参数: ");
//                String serialize = JacksonUtils.toJSONString(message);
//                message.append(serialize);
//            }
//            Object requestBody =
//                    request.exchange()
//                            .getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
//            if (Objects.nonNull(requestBody)) {
//                message.append(" 请求body: ");
//                message.append(requestBody);
//            }
//            message.append("]");
//            return message.toString();
//        }
    }

    /**
     * 图形验证码处理器
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/4/29 22:11
     */
    @Component
    public static class K8sHandler implements HandlerFunction<ServerResponse> {

        @Override
        public Mono<ServerResponse> handle( ServerRequest request ) {
            try {
                String hostName = InetAddress.getLoopbackAddress().getHostAddress();

                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(hostName);
            } catch (Exception e) {
                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
            }
        }
    }

    /**
     * 图形验证码处理器
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/4/29 22:11
     */
    @Component
    public static class ImageCodeHandler implements HandlerFunction<ServerResponse> {

        private static final String PARAM_T = "t";
        private final RedisRepository redisRepository;

        public ImageCodeHandler( RedisRepository redisRepository ) {
            this.redisRepository = redisRepository;
        }

        @Override
        public Mono<ServerResponse> handle( ServerRequest request ) {
            try {
                ArithmeticCaptcha captcha = CaptchaUtils.getArithmeticCaptcha();
                String text = captcha.text();
                LogUtils.info(text);
                MultiValueMap<String, String> params = request.queryParams();
                String t = params.getFirst(PARAM_T);

                redisRepository.setExpire(
                        RedisConstants.CAPTCHA_KEY_PREFIX + t, text.toLowerCase(), 120);

                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Result.success(captcha.toBase64()));
            } catch (Exception e) {
                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
            }
        }
    }

    /**
     * 图形验证码处理器
     *
     * @author shuigedeng
     * @version 2022.03
     * @since 2020/4/29 22:11
     */
    @Component
    public static class FaviconHandler implements HandlerFunction<ServerResponse> {

        @Override
        public Mono<ServerResponse> handle( ServerRequest request ) {
            try {
                ClassPathResource classPathResource = new ClassPathResource("favicon/favicon.ico");
                InputStream inputStream = classPathResource.getInputStream();

                byte[] bytes = IOUtils.toByteArray(inputStream);

                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.IMAGE_PNG)
                        .bodyValue(bytes);
            } catch (Exception e) {
                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
            }
        }
    }

    /**
     * HealthReportHandler
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    @Component
    public static class HealthReportHandler implements HandlerFunction<ServerResponse> {

        @Override
        public Mono<ServerResponse> handle( ServerRequest request ) {
            try {
                String uri = request.uri().getPath();

                String html;
                HealthCheckProvider healthProvider =
                        ContextUtils.getBean(HealthCheckProvider.class, true);
                if (Objects.nonNull(healthProvider) && uri.startsWith(HEALTH_REPORT)) {

                    boolean isAnalyse =
                            !"false"
                                    .equalsIgnoreCase(
                                            request.queryParam("isAnalyse").orElse("false"));

                    Report report = healthProvider.getReport(isAnalyse);
                    MediaType mediaType =
                            request.headers().contentType().orElse(MediaType.TEXT_PLAIN);
                    if (mediaType.includes(MediaType.APPLICATION_JSON)) {
                        return ServerResponse.status(HttpStatus.HTTP_OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Result.success(report.toJson()));
                    } else {
                        html =
                                report.toHtml()
                                        .replace("\r\n", "<br/>")
                                        .replace("\n", "<br/>")
                                        .replace("/n", "\n")
                                        .replace("/r", "\r");
                        html = "dump信息:<a href='/health/dump/'>查看</a><br/>" + html;
                    }
                } else {
                    html =
                            "请配置taotao.cloud.monitor.enabled=true,taotao.cloud.monitor.check.enabled=true";
                }

                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.TEXT_HTML)
                        .header("content-type", "text/html;charset=UTF-8")
                        .bodyValue(html.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                return ServerResponse.status(HttpStatus.HTTP_OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(Result.fail("服务异常,请稍后重试")));
            }
        }
    }
}
