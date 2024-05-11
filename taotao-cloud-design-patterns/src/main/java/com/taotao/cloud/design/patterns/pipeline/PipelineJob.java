package com.taotao.cloud.design.patterns.pipeline;

/**
 * 管道任务接口
 *
 * @param <P> 管道产品
 */
@FunctionalInterface
public interface PipelineJob<P> {
    /**
     * 执行任务
     *
     * @param product 管道产品
     * @return {@link P}
     */
    P execute(P product);
}
