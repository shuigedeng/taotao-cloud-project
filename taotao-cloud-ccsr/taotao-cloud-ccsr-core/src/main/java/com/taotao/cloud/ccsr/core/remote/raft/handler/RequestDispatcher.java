package com.taotao.cloud.ccsr.core.remote.raft.handler;

import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.common.log.Log;

public class RequestDispatcher {

    private static RequestDispatcher INSTANCE;

    private RequestHandlerFactory handlerFactory;

    private RequestDispatcher() {
    }

    private RequestDispatcher(RequestHandlerFactory factory) {
        this.handlerFactory = factory;
    }

    public static RequestDispatcher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestDispatcher(RequestHandlerFactory.init());
        }
        return INSTANCE;
    }

    public Response dispatch(Message request, Class<?> clazz) {

        Log.print("===请求dispatch开始===> clazz:%s", clazz);

        try {
            RequestHandler<? extends Message> handler = handlerFactory.getHandler(clazz);
            return handler.onApply(request);
        } catch (Exception e) {
            // 错误处理
            RequestHandler<? extends Message> handler = handlerFactory.getHandler(clazz);
            handler.onError(e);
            throw new RuntimeException(e);
        }
    }
}
