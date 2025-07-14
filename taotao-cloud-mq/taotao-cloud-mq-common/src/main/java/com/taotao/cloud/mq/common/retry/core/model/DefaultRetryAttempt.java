package com.taotao.cloud.mq.common.retry.core.model;

import com.taotao.cloud.mq.common.retry.api.model.AttemptTime;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;

import java.util.List;

/**
 * 默认重试信息
 * @author shuigedeng
 * @since 0.0.1
 * @param <R> 泛型
 */
public class DefaultRetryAttempt<R> implements RetryAttempt<R> {

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
     * 请求参数
     * @since 0.1.0
     */
    private Object[] params;

    @Override
    public R result() {
        return result;
    }

    public DefaultRetryAttempt<R> result(R result) {
        this.result = result;
        return this;
    }

    @Override
    public int attempt() {
        return attempt;
    }

    public DefaultRetryAttempt<R> attempt(int attempt) {
        this.attempt = attempt;
        return this;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    public DefaultRetryAttempt<R> cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    @Override
    public AttemptTime time() {
        return time;
    }

    public DefaultRetryAttempt<R> time(AttemptTime time) {
        this.time = time;
        return this;
    }

    @Override
    public List<RetryAttempt<R>> history() {
        return history;
    }

    public DefaultRetryAttempt<R> history(List<RetryAttempt<R>> history) {
        this.history = history;
        return this;
    }

    @Override
    public Object[] params() {
        return params;
    }

    public DefaultRetryAttempt<R> params(Object[] params) {
        this.params = params;
        return this;
    }
}
