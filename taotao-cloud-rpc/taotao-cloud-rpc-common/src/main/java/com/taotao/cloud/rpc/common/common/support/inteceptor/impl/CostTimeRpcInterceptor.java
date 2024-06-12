package com.taotao.cloud.rpc.common.common.support.inteceptor.impl;

import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 内置耗时 rpc 拦截器实现
 * @author shuigedeng
 * @since 0.1.4
 */
public class CostTimeRpcInterceptor extends RpcInterceptorAdaptor {

    private static final Logger LOG = LoggerFactory.getLogger(CostTimeRpcInterceptor.class);

    @Override
    public void before(RpcInterceptorContext context) {
    }

    @Override
    public void after(RpcInterceptorContext context) {
        long costMills = context.endTime() - context.startTime();
//        log.info("[Interceptor] cost time {} mills for traceId: {}", costMills,
//                context.traceId());
    }

}
