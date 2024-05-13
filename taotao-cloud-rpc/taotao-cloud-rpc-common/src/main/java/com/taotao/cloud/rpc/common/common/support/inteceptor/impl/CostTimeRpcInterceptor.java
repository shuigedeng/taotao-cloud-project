package com.github.houbb.rpc.common.support.inteceptor.impl;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.rpc.common.support.inteceptor.RpcInterceptorContext;

/**
 * 内置耗时 rpc 拦截器实现
 * @author shuigedeng
 * @since 0.1.4
 */
public class CostTimeRpcInterceptor extends RpcInterceptorAdaptor {

    private static final Log log = LogFactory.getLog(CostTimeRpcInterceptor.class);

    @Override
    public void before(RpcInterceptorContext context) {
    }

    @Override
    public void after(RpcInterceptorContext context) {
        long costMills = context.endTime() - context.startTime();
        log.info("[Interceptor] cost time {} mills for traceId: {}", costMills,
                context.traceId());
    }

}
