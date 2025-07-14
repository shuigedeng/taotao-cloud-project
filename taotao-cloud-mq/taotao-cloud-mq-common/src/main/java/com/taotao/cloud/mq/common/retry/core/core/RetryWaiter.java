package com.taotao.cloud.mq.common.retry.core.core;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.support.wait.RetryWait;
import com.taotao.cloud.mq.common.retry.core.constant.RetryWaitConst;
import com.taotao.cloud.mq.common.retry.core.context.DefaultRetryWaitContext;
import com.taotao.cloud.mq.common.retry.core.support.wait.ExponentialRetryWait;
import com.taotao.cloud.mq.common.retry.core.support.wait.IncreaseRetryWait;
import com.taotao.cloud.mq.common.retry.core.support.wait.NoRetryWait;

/**
 * 重试等待类构造器
 * @author shuigedeng
 * @since 0.0.3
 * @param <R> 泛型
 */
public class RetryWaiter<R> {

    /**
     * 重试等待类的类型
     * 必须要有无参构造函数
     */
    private Class<? extends RetryWait> retryWait = NoRetryWait.class;

    /**
     * 默认的时间
     */
    private long value = RetryWaitConst.VALUE_MILLS;

    /**
     * 最小值
     */
    private long min = RetryWaitConst.MIN_MILLS;

    /**
     * 最小值
     */
    private long max = RetryWaitConst.MAX_MILLS;

    /**
     * 变化因子
     * 1. 如果是 {@link ExponentialRetryWait} 则为 {@link RetryWaitConst#MULTIPLY_FACTOR}
     * 2. 如果是 {@link IncreaseRetryWait} 则为 {@link RetryWaitConst#INCREASE_MILLS_FACTOR}
     */
    private double factor = Double.MIN_VALUE;

    /**
     * 构造器私有化
     */
    private RetryWaiter(){}

    /**
     * 设置重试等待的对象类型
     * @param retryWait 重试等待类
     * @param <R> 泛型
     * @return 重试等待类
     */
    public static <R> RetryWaiter<R> retryWait(Class<? extends RetryWait> retryWait) {
        RetryWaiter<R> retryWaiter = new RetryWaiter<>();
        retryWaiter.setRetryWait(retryWait);

        //设置 factor 的默认值
        if(IncreaseRetryWait.class.equals(retryWait)) {
            retryWaiter.factor(RetryWaitConst.INCREASE_MILLS_FACTOR);
        }
        if(ExponentialRetryWait.class.equals(retryWait)) {
            retryWaiter.factor(RetryWaitConst.MULTIPLY_FACTOR);
        }
        return retryWaiter;
    }

    public Class<? extends RetryWait> retryWait() {
        return retryWait;
    }

    private RetryWaiter<R> setRetryWait(Class<? extends RetryWait> retryWait) {
        this.retryWait = retryWait;
        return this;
    }

    public long value() {
        return value;
    }

    public RetryWaiter<R> value(long value) {
        this.value = value;
        return this;
    }

    public long min() {
        return min;
    }

    public RetryWaiter<R> min(long min) {
        this.min = min;
        return this;
    }

    public long max() {
        return max;
    }

    public RetryWaiter<R> max(long max) {
        this.max = max;
        return this;
    }

    public double factor() {
        return factor;
    }

    public RetryWaiter<R> factor(double factor) {
        this.factor = factor;
        return this;
    }

    /**
     * 构建重试等待时间上下文
     * @return 重试等待时间上下文
     * @since 0.0.5
     */
    public RetryWaitContext<R> context() {
        DefaultRetryWaitContext<R> waitContext = new DefaultRetryWaitContext<>();
        waitContext.factor(factor);
        waitContext.max(max);
        waitContext.min(min);
        waitContext.retryWait(retryWait);
        waitContext.value(value);
        return waitContext;
    }

}
