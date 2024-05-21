package com.taotao.cloud.rpc.common.common.support.inteceptor.impl;

import com.taotao.cloud.rpc.common.common.support.inteceptor.RpcInterceptorContext;

import java.util.Arrays;

/**
 * 日志拦截器
 *
 * @author shuigedeng
 * @since 0.2.2
 */
public class LogRpcInterceptor extends RpcInterceptorAdaptor {

    private static final Log log = LogFactory.getLog(LogRpcInterceptor.class);

    @Override
    public void before(RpcInterceptorContext context) {
        log.info("[Interceptor] param {} for traceId {}", Arrays.toString(context.params()), context.traceId());
    }

    @Override
    public void after(RpcInterceptorContext context) {
        log.info("[Interceptor] result {} for traceId {}", context.result(), context.traceId());
    }

}