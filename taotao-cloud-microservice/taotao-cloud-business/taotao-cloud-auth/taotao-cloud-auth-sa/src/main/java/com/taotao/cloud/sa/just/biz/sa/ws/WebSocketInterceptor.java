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

package com.taotao.cloud.sa.just.biz.sa.ws;

import cn.dev33.satoken.stp.StpUtil;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * WebSocket 握手的前置拦截器
 *
 * @author kong
 * @since: 2022-2-11
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    // 握手之前触发 (return true 才会握手成功 )
    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler handler,
            Map<String, Object> attr) {

        LogUtils.info("---- 握手之前触发 " + StpUtil.getTokenValue());

        // 未登录情况下拒绝握手
        if (!StpUtil.isLogin()) {
            LogUtils.info("---- 未授权客户端，连接失败");
            return false;
        }

        // 标记 userId，握手成功
        attr.put("userId", StpUtil.getLoginIdAsLong());
        return true;
    }

    // 握手之后触发
    @Override
    public void afterHandshake(
            ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        LogUtils.info("---- 握手之后触发 ");
    }
}
