package com.taotao.cloud.rpc.filter;

import com.taotao.cloud.rpc.common.filter.Filter;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.metrics.MetricsCollector;
import com.taotao.cloud.rpc.common.spi.Activate;

/**
 * Provider 端监控过滤器
 */
@Activate(group = "provider", order = 100)
public class ProviderMonitorFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            Result result = invoker.invoke(invocation);
            success = !result.hasException();
            return result;
        } finally {
            long rt = System.currentTimeMillis() - start;
            MetricsCollector.getInstance().record(
                    invoker.getInterface().getName(),
                    invocation.getMethodName(),
                    rt, success);
        }
    }
}
