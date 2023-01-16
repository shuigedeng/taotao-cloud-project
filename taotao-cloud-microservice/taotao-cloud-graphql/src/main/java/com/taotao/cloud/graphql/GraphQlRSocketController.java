package com.taotao.cloud.graphql;// package com.taotao.cloud.backend.graphql;
//
// import org.springframework.graphql.server.GraphQlRSocketHandler;
// import org.springframework.stereotype.Controller;
//
// @Controller
// public class GraphQlRSocketController {
//
//      private final GraphQlRSocketHandler handler;
//
//      GraphQlRSocketController(GraphQlRSocketHandler handler) {
//             this.handler = handler;
//      }
//
//      @MessageMapping("graphql")
//      public Mono<Map<String, Object>> handle(Map<String, Object> payload) {
//             return this.handler.handle(payload);
//      }
//
//      @MessageMapping("graphql")
//      public Flux<Map<String, Object>> handleSubscription(Map<String, Object> payload) {
//             return this.handler.handleSubscription(payload);
//      }
// }
//
