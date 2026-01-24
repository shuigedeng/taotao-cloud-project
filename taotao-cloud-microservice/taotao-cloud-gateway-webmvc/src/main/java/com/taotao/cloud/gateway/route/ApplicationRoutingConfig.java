//package com.taotao.cloud.gateway.route;
//
//import com.taotao.cloud.gateway.filter.SampleHandlerFilterFunctions;
//import com.taotao.cloud.gateway.predicates.SampleRequestPredicates;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.web.servlet.function.*;
//
//import java.time.ZonedDateTime;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//
//import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.*;
//import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
//import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
//import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.*;
//import static org.springframework.web.servlet.function.RequestPredicates.accept;
//
//
///**
// * @author Mr han
// * @date 2025/12/6
// * @description 路由配置，此处配置了路由后，application.yml中配置的路由将无效，两种配置只能存在一种，不能并存
// * 同等效果配置文件方式配置@see application.yml,自定义的filter和predicate在两种配置方式都能够生效
// *
// *
//*/
//@Configuration(proxyBeanMethods = false)
//@Profile("javaconfig")
//public class ApplicationRoutingConfig {
//
//    private static final Logger log = LoggerFactory.getLogger(ApplicationRoutingConfig.class);
//
//    /**
//     *
//     * @return
//     * @date 2025/12/17
//     * @description 使用java代码方式配置路由，等价于@see application.yml文件配置方式
//	 * return route("order_v2_canary")
//	 *       .route(path("/api/orders/**").and(header("X-Canary", "true")), http())
//	 *       .before(uri("http://order-service-v2:8080"))
//	 *       .build()
//	 *     .and(route("order_v1_default")
//	 *       .route(path("/api/orders/**"), http())
//	 *       .before(uri("http://order-service-v1:8080"))
//	 *       .build());
//	 *
//     * return route("resource")
//	 *       .GET("/resource", http())
//	 *       .before(uri("http://localhost:9000"))
//	 *       .filter(tokenRelay()) // 不传 registrationId：转发当前已认证用户的 token
//	 *       .build();
//	 *
//	 *         - id: resource
//	 *             uri: http://localhost:9000
//	 *             predicates:
//	 *               - Path=/resource
//	 *             filters:
//	 *               - TokenRelay=
//	 *
//	 *   Spring Security Resource Server：负责“token 合法性”
//	 * Gateway Filter：负责“把必要的用户信息打到下游”（例如 userId、tenantId、traceId）
//	 * TokenRelay（可选）：如果你是 OAuth2 Client 场景，就用它转发 access token
//     */
//    @Bean
//    public RouterFunction<ServerResponse> customRoutes(GatewayHandler gatewayHandler) {
//        // @formatter:off
//        return route("path_route")
//                .GET("/gateway/getRole", accept(MediaType.TEXT_HTML),gatewayHandler::getRole)
//                .POST("/gateway/updateRole/{roleId}", accept(MediaType.APPLICATION_JSON), gatewayHandler::updateRole)
//                .before(request -> {
//                    String path = request.requestPath().value();
//                    log.error("=========开始进入本服务请求：{}=========",path);
//                    return request;
//                })
//                // 添加请求之后的函数处理器
//                .after((request,response) -> {
//                    log.error("==========本服务请求处理完毕：{}，响应的状态码：",request.requestPath().value(),response.statusCode());
//                    return response;
//                })
//                .build().and(route("entitle_route")
//                    .route(path("/entitleservice/**").and(SampleRequestPredicates.predicateHeaderExists("entitle_header")), http())
//                    .before(uri("http://localhost:8081"))
//                    .before(addRequestParameter("code","Java gateway for entitlementservice"))
//                    .before(addRequestHeader("java_config_header","java config value"))
//                    .filter(SampleHandlerFilterFunctions.instrumentForFilter("req_header","rep_header"))
//                .build().and(route("function_demo_route")
//                    .route(path("/functionservletpath/**").and(SampleRequestPredicates.predicateHeaderExists("function_header")), http())
//                    .before(uri("http://localhost:8082"))
//                    .before(addRequestParameter("code","Java config gateway for functiondemo"))
//                    .before(addRequestHeader("function-Request-Id","function_header_value"))
//                .build()));
//    }
//
//    /**
//     * @author Mr han
//     * @date 2025/12/15
//     * @description 自定义的predicate 是否包含某个固定的请求头，此类的实现等等同于方法 @see {@link SampleRequestPredicates#predicateHeaderExists(String)}
//     *
//    */
//    private static class CustomHeaderExists implements RequestPredicate {
//
//        // 请求头名称
//        private static final String HEADER_NAME = "custom_header";
//
//        /**
//         *
//         * @return
//         * @date 2025/12/15
//         * @description 需要包含custom_header请求头
//         *
//         */
//        @Override
//        public boolean test(ServerRequest request) {
//            return request.headers().asHttpHeaders().containsKey(HEADER_NAME);
//        }
//    }
//    /**
//     *
//     * @return
//     * @date 2025/12/15
//     * @description 使用函数式的方法，自定义过滤器，使用函数式方法实现过滤器
//     * 在自定义的过滤器中可以实现日志的打印，已经权限的认证等逻辑，这里仅仅是增加请求头及响应头
//     * 此方法等同于@see {@link SampleHandlerFilterFunctions#instrumentForFilter(String, String)}
//     *
//     */
//    private static HandlerFilterFunction<ServerResponse, ServerResponse> instrumentWithFilter(String requestHeader,String responseHeader){
//        return (request,next) -> {
//            ServerRequest modifier = ServerRequest.from(request).header(requestHeader, "hello").build();
//            ServerResponse responseHandler = next.handle(modifier);
//            responseHandler.headers().add(responseHeader, "world");
//            return responseHandler;
//        };
//    }
//    /**
//     *
//     * @return
//     * @date 2025/12/15
//     * @description 使用函数式的方法，定义before方式的过滤器
//     *
//     */
//    private static Function<ServerRequest, ServerRequest> instrumentWithBefore(String requestHeader){
//        return request -> {
//            return ServerRequest.from(request).header(requestHeader, "before_header_value").build();
//        };
//    }
//    /**
//     *
//     * @return
//     * @date 2025/12/15
//     * @description 使用函数式的方法，定义after方式的过滤器
//     *
//     */
//    private static BiFunction<ServerRequest, ServerResponse, ServerResponse> instrumentWithAfter(String header){
//        return (request,response) -> {
//            response.headers().add(header, "after_header_value");
//            return response;
//        };
//    }
//}
//
//
