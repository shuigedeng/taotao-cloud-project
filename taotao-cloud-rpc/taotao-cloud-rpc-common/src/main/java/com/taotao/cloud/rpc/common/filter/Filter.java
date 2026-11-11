package com.taotao.cloud.rpc.common.filter;

import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.spi.SPI;

/**
 * 拦截器接口
 * <p>
 * Dubbo 风格的责任链扩展点。
 * <p>
 * 实现示例：
 * <pre>
 * &#64;Activate(group = "consumer", order = 100)
 * public class MonitorFilter implements Filter {
 *     public Result invoke(Invoker&lt;?&gt; invoker, Invocation inv) {
 *         long start = System.currentTimeMillis();
 *         try {
 *             return invoker.invoke(inv);
 *         } finally {
 *             MetricsCollector.record(inv, System.currentTimeMillis() - start);
 *         }
 *     }
 * }
 * </pre>
 * <p>
 * 资源文件：META-INF/ttc-rpc/com.taotao.cloud.rpc.filter.Filter
 */
@SPI
public interface Filter {

    /**
     * 拦截调用
     *
     * @param invoker 目标调用者
     * @param invocation 调用信息
     * @return 调用结果
     * @throws Throwable 任何异常
     */
    Result invoke(Invoker<?> invoker, Invocation invocation) throws Throwable;
}
