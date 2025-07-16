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

package com.taotao.cloud.rpc.core.handler;

import com.taotao.cloud.rpc.common.exception.RpcException;
import com.taotao.cloud.rpc.common.protocol.RpcRequest;
import com.taotao.cloud.rpc.core.provider.DefaultServiceProvider;
import com.taotao.cloud.rpc.core.provider.ServiceProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求处理器
 */
@Slf4j
public class RequestHandler {

    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new DefaultServiceProvider();
    }

    /**
     * 调用成功返回 正确结果 调用失败 返回 异常结果 并 打印异常信息
     *
     * @param rpcRequest
     * @return
     */
    public Object handler(RpcRequest rpcRequest) {
        long begin = System.currentTimeMillis();
        Object result = null;
        try {
            Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
            result = invokeTargetMethod(rpcRequest, service);
            log.info(
                    "Service: {} has invoked method: {} ",
                    rpcRequest.getInterfaceName(),
                    rpcRequest.getMethodName());
            long end = System.currentTimeMillis();
            log.info("==> Task execution takes {} ms", (end - begin));
        } catch (NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException
                | RpcException e) {
            log.error("Error occurred while invoking remote method: ", e);
            throw new RuntimeException(
                    "Error occurred while invoking remote method: " + e.getMessage());
        }
        // 捕获异常不会return 具体调用的方法结果
        return result;
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        log.info("Proxy target Service {}", service);
        log.info(
                "Service: {} is invoking method [{}], paramTypes [{}] ,parameters [{}]",
                rpcRequest.getInterfaceName(),
                rpcRequest.getMethodName(),
                rpcRequest.getParamTypes(),
                rpcRequest.getParameters());
        Method method =
                service.getClass()
                        .getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        // 调用方法的 返回结果
        return method.invoke(service, rpcRequest.getParameters());
    }
}
