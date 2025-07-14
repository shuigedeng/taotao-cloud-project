package com.taotao.cloud.mq.common.retry.core.context;

import com.taotao.cloud.mq.common.retry.api.context.RetryContext;
import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.core.Retry;
import com.taotao.cloud.mq.common.retry.api.support.block.RetryBlock;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;
import com.taotao.cloud.mq.common.retry.api.support.listen.RetryListen;
import com.taotao.cloud.mq.common.retry.api.support.recover.Recover;
import com.taotao.cloud.mq.common.retry.api.support.stop.RetryStop;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 默认的重试上下文
 * @author shuigedeng
 * @since 0.0.1
 * @param <R> 泛型
 */
public class DefaultRetryContext<R>  implements RetryContext<R> {

    /**
     * 重试实现类
     * @since 0.0.5
     */
    private Retry<R> retry;

    /**
     * 执行的条件
     */
    private RetryCondition condition;

    /**
     * 重试等待上下文
     */
    private List<RetryWaitContext<R>> waitContext;

    /**
     * 阻塞实现
     */
    private RetryBlock block;

    /**
     * 停止策略
     */
    private RetryStop stop;

    /**
     * 可执行的方法
     */
    private Callable<R> callable;

    /**
     * 监听器
     */
    private RetryListen listen;

    /**
     * 恢复策略
     */
    private Recover recover;

    /**
     * 请求参数
     * @since 0.1.0
     */
    private Object[] params;

    @Override
    public Retry<R> retry() {
        return retry;
    }

    public DefaultRetryContext<R> retry(Retry<R> retry) {
        this.retry = retry;
        return this;
    }

    @Override
    public RetryCondition condition() {
        return condition;
    }

    public DefaultRetryContext<R> condition(RetryCondition condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public List<RetryWaitContext<R>> waitContext() {
        return waitContext;
    }

    public DefaultRetryContext<R> waitContext(List<RetryWaitContext<R>> waitContext) {
        this.waitContext = waitContext;
        return this;
    }

    @Override
    public RetryBlock block() {
        return block;
    }

    public DefaultRetryContext<R> block(RetryBlock block) {
        this.block = block;
        return this;
    }

    @Override
    public RetryStop stop() {
        return stop;
    }

    public DefaultRetryContext<R> stop(RetryStop stop) {
        this.stop = stop;
        return this;
    }

    @Override
    public Callable<R> callable() {
        return callable;
    }

    public DefaultRetryContext<R> callable(Callable<R> callable) {
        this.callable = callable;
        return this;
    }

    @Override
    public RetryListen listen() {
        return listen;
    }

    public DefaultRetryContext<R> listen(RetryListen listen) {
        this.listen = listen;
        return this;
    }

    @Override
    public Recover recover() {
        return recover;
    }

    public DefaultRetryContext<R> recover(Recover recover) {
        this.recover = recover;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    @Override
    public DefaultRetryContext<R> params(Object[] params) {
        this.params = params;
        return this;
    }
}
