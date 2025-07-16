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
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.common.log.Log;

public class RequestDispatcher {

    private static RequestDispatcher INSTANCE;

    private RequestHandlerFactory handlerFactory;

    private RequestDispatcher() {}

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
