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

    private RequestHandlerFactory() {
    }

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
