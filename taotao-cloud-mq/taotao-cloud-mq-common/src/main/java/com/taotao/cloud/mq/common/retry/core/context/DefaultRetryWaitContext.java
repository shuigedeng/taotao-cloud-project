package com.taotao.cloud.mq.common.retry.core.context;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.AttemptTime;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.wait.RetryWait;

import java.util.List;

/**
 * 默认重试等待上下文
 * @author shuigedeng
 * @since 0.0.3
 */
public class DefaultRetryWaitContext<R> implements RetryWaitContext<R> {

    /**
     * 执行结果
     */
    private R result;

    /**
     * 尝试次数
     */
    private int attempt;

    /**
     * 尝试次数
     */
    private Throwable cause;

    /**
     * 消耗时间
     */
    private AttemptTime time;

    /**
     * 历史信息
     */
    private List<RetryAttempt<R>> history;

    /**
     * 基础值
     */
    private long value;

    /**
     * 最小值
     */
    private long min;

    /**
     * 最大值
     */
    private long max;

    /**
     * 变化因子
     */
    private double factor;

    /**
     * 重试等待类
     */
    private Class<? extends RetryWait> retryWait;

    /**
     * 请求参数
     * @since 0.1.0
     */
    private Object[] params;

    @Override
    public R result() {
        return result;
    }

    public DefaultRetryWaitContext<R> result(R result) {
        this.result = result;
        return this;
    }

    @Override
    public int attempt() {
        return attempt;
    }

    public DefaultRetryWaitContext<R> attempt(int attempt) {
        this.attempt = attempt;
        return this;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    public DefaultRetryWaitContext<R> cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    @Override
    public AttemptTime time() {
        return time;
    }

    public DefaultRetryWaitContext<R> time(AttemptTime time) {
        this.time = time;
        return this;
    }

    @Override
    public List<RetryAttempt<R>> history() {
        return history;
    }

    public DefaultRetryWaitContext<R> history(List<RetryAttempt<R>> history) {
        this.history = history;
        return this;
    }

    @Override
    public long value() {
        return value;
    }

    public DefaultRetryWaitContext<R> value(long value) {
        this.value = value;
        return this;
    }

    @Override
    public long min() {
        return min;
    }

    public DefaultRetryWaitContext<R> min(long min) {
        this.min = min;
        return this;
    }

    @Override
    public long max() {
        return max;
    }

    public DefaultRetryWaitContext<R> max(long max) {
        this.max = max;
        return this;
    }

    @Override
    public double factor() {
        return factor;
    }

    public DefaultRetryWaitContext<R> factor(double factor) {
        this.factor = factor;
        return this;
    }

    @Override
    public Class<? extends RetryWait> retryWait() {
        return retryWait;
    }

    public DefaultRetryWaitContext<R> retryWait(Class<? extends RetryWait> retryWait) {
        this.retryWait = retryWait;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public DefaultRetryWaitContext<R> params(Object[] params) {
        this.params = params;
        return this;
    }
}
