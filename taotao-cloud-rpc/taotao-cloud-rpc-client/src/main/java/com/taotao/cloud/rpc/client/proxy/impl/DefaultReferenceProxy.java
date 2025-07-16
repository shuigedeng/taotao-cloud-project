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

package com.taotao.cloud.rpc.client.proxy.impl;

import com.taotao.cloud.rpc.client.proxy.ReferenceProxy;
import com.taotao.cloud.rpc.client.proxy.RemoteInvokeService;
import com.taotao.cloud.rpc.client.proxy.ServiceContext;
import com.taotao.cloud.rpc.common.common.rpc.domain.impl.DefaultRpcRequest;
import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptor;
import com.taotao.cloud.rpc.common.common.support.inteceptor.impl.DefaultRpcInterceptorContext;
import com.taotao.cloud.rpc.common.common.support.status.enums.StatusEnum;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参考：https://blog.csdn.net/u012240455/article/details/79210250
 *
 * （1）方法执行并不需要一定要有实现类。
 * （2）直接根据反射即可处理相关信息。
 * （3）rpc 是一种强制根据接口进行编程的实现方式。
 * @author shuigedeng
 * @since 2024.06
 */
public class DefaultReferenceProxy<T> implements ReferenceProxy<T> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultReferenceProxy.class);

    /**
     * 代理上下文
     * （1）这个信息不应该被修改，应该和指定的 service 紧密关联。
     * @since 2024.06
     */
    private final ServiceContext<T> proxyContext;

    /**
     * 远程调用接口
     * @since 0.1.1
     */
    private final RemoteInvokeService remoteInvokeService;

    public DefaultReferenceProxy(
            ServiceContext<T> proxyContext, RemoteInvokeService remoteInvokeService) {
        this.proxyContext = proxyContext;
        this.remoteInvokeService = remoteInvokeService;
    }

    /**
     * 反射调用
     * @param proxy 代理
     * @param method 方法
     * @param args 参数
     * @return 结果
     * @throws Throwable 异常
     * @since 2024.06
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 状态判断
        //        final String traceId = Ids.uuid32();
        final int statusCode = proxyContext.statusManager().status();
        StatusEnum.assertEnable(statusCode);
        //        final long createTime = Times.systemTime();

        // 1. 拦截器
        final RpcInterceptor rpcInterceptor = proxyContext.interceptor();
        final DefaultRpcInterceptorContext interceptorContext =
                DefaultRpcInterceptorContext.newInstance()
                        .traceId("traceId")
                        .params(args)
                        .startTime(0L);
        rpcInterceptor.before(interceptorContext);

        // 构建基本调用参数
        DefaultRpcRequest rpcRequest = new DefaultRpcRequest();
        rpcRequest.serviceId(proxyContext.serviceId());
        //        rpcRequest.createTime(createTime);
        rpcRequest.paramValues(args);
        //        rpcRequest.paramTypeNames(ReflectMethodUtil.getParamTypeNames(method));
        rpcRequest.methodName(method.getName());
        rpcRequest.returnType(method.getReturnType());
        rpcRequest.timeout(proxyContext.timeout());
        rpcRequest.callType(proxyContext.callType());

        // proxyContext 中应该是属于当前 service 的对应信息。
        // 每一次调用，对应的 invoke 信息应该是不通的，需要创建新的对象去传递信息
        // rpcRequest 因为要涉及到网络间传输，尽可能保证其简洁性。
        DefaultRemoteInvokeContext<T> context = new DefaultRemoteInvokeContext<>();
        context.request(rpcRequest);
        //        context.traceId(traceId);
        context.retryTimes(2);
        context.serviceProxyContext(proxyContext);
        context.remoteInvokeService(remoteInvokeService);

        // 3. 执行远程调用
        Object result = remoteInvokeService.remoteInvoke(context);

        // 4. 拦截器结束
        //        final long endTime = Times.systemTime();
        interceptorContext.endTime(0L).result(result);
        rpcInterceptor.after(interceptorContext);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T proxy() {
        final Class<T> interfaceClass = proxyContext.serviceInterface();
        ClassLoader classLoader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = new Class[] {interfaceClass};
        return (T) Proxy.newProxyInstance(classLoader, interfaces, this);
    }
}
