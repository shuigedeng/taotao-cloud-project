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

package com.taotao.cloud.rpc.server.custom;

import com.taotao.cloud.rpc.common.common.RpcReponse;
import com.taotao.cloud.rpc.common.common.RpcRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc 核心处理器<br>
 *
 * @author shuigedeng
 * @version v1.0.0
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);
    private Map<String, Object> handlerMap;

    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * 接受消息 处理消息 返回结果
     *
     * @param ctx     ctx
     * @param request request
     * @return void
     * @author shuigedeng
     * @since 2024.06
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcReponse response = new RpcReponse();
        response.setRequestId(request.getRequestId());

        try {
            Object obj = doRequestHandle(request);
            response.setResult(obj);
        } catch (Throwable e) {
            response.setErrorMsg(e);
        }

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 处理请求
     *
     * @param request request
     * @return java.lang.Object
     * @author shuigedeng
     * @since 2024.06
     */
    private Object doRequestHandle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        Object handler = handlerMap.get(className);

        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName, parameterTypes);
        return method.invoke(handler, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理异常");
        ctx.close();
    }
}
