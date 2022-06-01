package com.taotao.cloud.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public final class FilterUtils {

    private FilterUtils() {

    }


    public static Mono<Void> invalidToken(ServerWebExchange exchange) {
        JSONObject json = new JSONObject();
        json.put("code", HttpStatus.UNAUTHORIZED.value());
        json.put("msg", "无效的token");
        return buildReturnMono(json, exchange);
    }

    public static Mono<Void> invalidUrl(ServerWebExchange exchange){
        JSONObject json = new JSONObject();
        json.put("code", HttpStatus.BAD_REQUEST.value());
        json.put("msg", "无效的请求");
        return buildReturnMono(json, exchange);
    }


    public static Mono<Void> buildReturnMono(JSONObject json, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = json.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

}
