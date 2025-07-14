package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.boot.common.support.instance.impl.InstanceFactory;
import com.taotao.boot.common.support.pipeline.Pipeline;
import com.taotao.boot.common.utils.collection.ArrayUtils;
import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

/**
 * 重试条件工具类
 * 1. 结果 是否存在 是否等于
 * 2. 异常 是否存在异常
 * 3. 异常的类型。
 *
 * 待优化点：所有的 condition 实现可以写成单例实现。
 * @author shuigedeng
 * @since 0.0.1
 */
public final class RetryConditions {

    private RetryConditions(){}

    /**
     * 结果为空
     * @param <R> 单例
     * @return 结果为空
     */
    public static <R> RetryCondition<R> isNullResult() {
        return InstanceFactory.getInstance().singleton(NullResultRetryCondition.class);
    }

    /**
     * 结果不为空
     * @param <R> 单例
     * @return 结果为空
     */
    public static <R> RetryCondition<R> isNotNullResult() {
        return InstanceFactory.getInstance().singleton(NotNullResultRetryCondition.class);
    }

    /**
     * 结果等于预期值
     * 注意：null 值不等于任何值。
     * @param excepted 预期值
     * @param <R> 单例
     * @return 结果为空
     */
    public static <R> RetryCondition<R> isEqualsResult(final R excepted) {
        return new AbstractResultRetryCondition<R>() {
            @Override
            protected boolean resultCondition(R result) {
                if(ObjectUtils.isNull(result)) {
                    return false;
                }
                return result.equals(excepted);
            }
        };
    }

    /**
     * 结果不等于预期值
     * @param excepted 预期值
     * @param <R> 单例
     * @return 结果为空
     */
    public static <R> RetryCondition<R> isNotEqualsResult(final R excepted) {
        return new AbstractResultRetryCondition<R>() {
            @Override
            protected boolean resultCondition(R result) {
                if(ObjectUtils.isNull(result)) {
                    return true;
                }
                return !result.equals(excepted);
            }
        };
    }

    /**
     * 程序执行过程中遇到异常
     * @return 重试条件
     * @since 0.0.2
     */
    public static RetryCondition hasExceptionCause() {
        return InstanceFactory.getInstance().singleton(ExceptionCauseRetryCondition.class);
    }

    /**
     * 是预期的异常类型
     * @param exClass 异常类型
     * @return 异常重试条件
     * @since 0.0.2
     */
    public static RetryCondition isExceptionCauseType(final Class<? extends Throwable> exClass) {
        return new AbstractCauseRetryCondition() {
            @Override
            protected boolean causeCondition(Throwable throwable) {
                return exClass.isAssignableFrom(throwable.getClass());
            }
        };
    }

    /**
     * 将多个条件整合在一起
     * @param retryConditions 重试条件
     * @return 一个重试条件
     * @since 0.0.2
     */
    public static RetryCondition conditions(final RetryCondition ... retryConditions) {
        if(ArrayUtils.isEmpty(retryConditions)) {
            return AlwaysFalseRetryCondition.getInstance();
        }

        return new AbstractRetryConditionInit() {
            @Override
            protected void init(Pipeline<RetryCondition> pipeline, RetryAttempt retryAttempt) {
                for(RetryCondition retryCondition : retryConditions) {
                    pipeline.addLast(retryCondition);
                }
            }
        };
    }

}
