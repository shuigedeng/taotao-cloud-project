/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.mq.common.retry.core.core.retry;

import com.taotao.boot.common.support.instance.InstanceFactory;
import cn.hutool.core.date.DateUtil;
import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.cloud.mq.common.retry.api.context.RetryContext;
import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.core.Retry;
import com.taotao.cloud.mq.common.retry.api.exception.RetryException;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;
import com.taotao.cloud.mq.common.retry.api.support.block.RetryBlock;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;
import com.taotao.cloud.mq.common.retry.api.support.listen.RetryListen;
import com.taotao.cloud.mq.common.retry.api.support.recover.Recover;
import com.taotao.cloud.mq.common.retry.api.support.stop.RetryStop;
import com.taotao.cloud.mq.common.retry.api.support.wait.RetryWait;
import com.taotao.cloud.mq.common.retry.core.context.DefaultRetryWaitContext;
import com.taotao.cloud.mq.common.retry.core.model.DefaultAttemptTime;
import com.taotao.cloud.mq.common.retry.core.model.DefaultRetryAttempt;
import com.taotao.cloud.mq.common.retry.core.model.DefaultWaitTime;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


/**
 * 默认的重试实现
 *
 * @author shuigedeng
 * @since 0.0.1
 * @param <R> 泛型
 */
public class DefaultRetry<R> implements Retry<R> {

    /**
     * 获取单例
     * @return 获取单例
     */
    public static DefaultRetry getInstance() {
        return InstanceFactory.getInstance().singleton(DefaultRetry.class);
    }

    @Override
    public R retryCall(RetryContext<R> context) {
        List<RetryAttempt<R>> history = new ArrayList<>();

        // 1. 执行方法
        int attempts = 1;
        final Callable<R> callable = context.callable();
        RetryAttempt<R> retryAttempt = execute(callable, attempts, history, context);

        // 2. 是否进行重试
        // 2.1 触发执行的 condition
        // 2.2 不触发 stop 策略
        final List<RetryWaitContext<R>> waitContextList = context.waitContext();
        final RetryCondition retryCondition = context.condition();
        final RetryStop retryStop = context.stop();
        final RetryBlock retryBlock = context.block();
        final RetryListen retryListen = context.listen();

        while (retryCondition.condition(retryAttempt) && !retryStop.stop(retryAttempt)) {
            // 线程阻塞
            WaitTime waitTime = calcWaitTime(waitContextList, retryAttempt);
            retryBlock.block(waitTime);

            // 每一次执行会更新 executeResult
            attempts++;
            history.add(retryAttempt);
            retryAttempt = this.execute(callable, attempts, history, context);

            // 触发 listener
            retryListen.listen(retryAttempt);
        }

        // 如果仍然满足触发条件。但是已经满足停止策略
        // 触发 recover
        if (retryCondition.condition(retryAttempt) && retryStop.stop(retryAttempt)) {
            final Recover recover = context.recover();
            recover.recover(retryAttempt);
        }

        // 如果最后一次有异常，直接抛出异常 v0.0.2
        final Throwable throwable = retryAttempt.cause();
        if (ObjectUtils.isNotNull(throwable)) {
            // 1. 运行时异常，则直接抛出
            // 2. 非运行时异常，则包装成为 RetryException
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RetryException(retryAttempt.cause());
        }
        // 返回最后一次尝试的结果
        return retryAttempt.result();
    }

    /**
     * 构建等待时间
     * @param waitContextList 等待上下文列表
     * @param retryAttempt 重试信息
     * @return 等待时间毫秒
     */
    private WaitTime calcWaitTime(
            final List<RetryWaitContext<R>> waitContextList, final RetryAttempt<R> retryAttempt) {
        long totalTimeMills = 0;
        for (RetryWaitContext context : waitContextList) {
            RetryWait retryWait =
                    (RetryWait) InstanceFactory.getInstance().threadSafe(context.retryWait());
            final RetryWaitContext retryWaitContext = buildRetryWaitContext(context, retryAttempt);
            WaitTime waitTime = retryWait.waitTime(retryWaitContext);
            totalTimeMills += TimeUnit.MILLISECONDS.convert(waitTime.time(), waitTime.unit());
        }
        return new DefaultWaitTime(totalTimeMills);
    }

    /**
     * 构建重试等待上下文
     * @param waitContext 等待上下文
     * @param retryAttempt 重试信息
     * @return 构建后的等待信息
     */
    private RetryWaitContext buildRetryWaitContext(
            RetryWaitContext waitContext, final RetryAttempt<R> retryAttempt) {
        DefaultRetryWaitContext<R> context = (DefaultRetryWaitContext<R>) waitContext;
        context.attempt(retryAttempt.attempt());
        context.result(retryAttempt.result());
        context.history(retryAttempt.history());
        context.cause(retryAttempt.cause());
        context.time(retryAttempt.time());
        context.params(retryAttempt.params());
        return context;
    }

    /**
     * 执行 callable 方法
     *
     * @param callable 待执行的方法
     * @param attempts 重试次数
     * @param history  历史记录
     * @param context 请求上下文
     * @return 相关的额执行信息
     */
    private RetryAttempt<R> execute(
            final Callable<R> callable,
            final int attempts,
            final List<RetryAttempt<R>> history,
            final RetryContext<R> context) {
        final Date startTime = new Date();

        DefaultRetryAttempt<R> retryAttempt = new DefaultRetryAttempt<>();
        Throwable throwable = null;
        R result = null;
        try {
            result = callable.call();
        } catch (Exception e) {
            throwable = getActualThrowable(e);
        }
        final Date endTime = new Date();
        final long costTimeInMills = costTimeInMills(startTime, endTime);
        DefaultAttemptTime attemptTime = new DefaultAttemptTime();
        attemptTime.startTime(startTime).endTime(endTime).costTimeInMills(costTimeInMills);

        retryAttempt
                .attempt(attempts)
                .time(attemptTime)
                .cause(throwable)
                .result(result)
                .history(history);

        // 设置请求入参，主要用于回调等使用。
        retryAttempt.params(context.params());
        return retryAttempt;
    }

    public static Throwable getActualThrowable(final Throwable throwable) {
        if (InvocationTargetException.class.equals(throwable.getClass())) {
            InvocationTargetException exception = (InvocationTargetException) throwable;
            return exception.getTargetException();
        }
        return throwable;
    }

    public static long costTimeInMills(final Date start, final Date end) {
        return end.getTime() - start.getTime();
    }
}
