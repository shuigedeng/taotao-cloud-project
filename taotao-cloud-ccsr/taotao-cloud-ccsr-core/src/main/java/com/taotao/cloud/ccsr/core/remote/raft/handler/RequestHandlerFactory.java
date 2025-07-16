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

package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.spi.SpiExtensionFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shuigedeng
 * @date 2025-03-17 12:46
 */
public class RequestHandlerFactory {

    private final Map<Class<?>, RequestHandler<? extends Message>> handlerMap = new HashMap<>();

    private static RequestHandlerFactory factory;

    private RequestHandlerFactory() {}

    public static RequestHandlerFactory init() {
        if (factory == null) {
            factory = new RequestHandlerFactory();
        }

        SpiExtensionFactory.getExtensions(RequestHandler.class).forEach(factory::registerHandler);
        return factory;
    }

    /**
     * 注册处理器
     */
    public void registerHandler(RequestHandler<? extends Message> handler) {
        handlerMap.put(handler.clazz(), handler);
    }

    /**
     * 根据 clazz 类型获取对应的处理器
     */
    public RequestHandler<? extends Message> getHandler(Class<?> clazz) {
        RequestHandler<? extends Message> handler = handlerMap.get(clazz);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for class: " + clazz.getName());
        }
        return handler;
    }
}
