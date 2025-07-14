package com.taotao.cloud.mq.common.retry.core.support.wait;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

/**
 * 指数增长的等待策略
 * 1. 如果因数大于 1 越来越快。
 * 2. 如果因数等于1 保持不变
 * 3. 如果因数大于0，且小于1 。越来越慢
 *
 * 斐波那契数列就是一种乘数接近于：1.618 的黄金递增。
 * 可以参考 {@link com.github.houbb.heaven.constant.MathConst#GOLD_SECTION}
 * 指数等待函数
 * @author shuigedeng
 * @since 0.0.1
 */
public class ExponentialRetryWait extends AbstractRetryWait {

    @Override
    public WaitTime waitTime(RetryWaitContext retryWaitContext) {
        final int previousAttempt = retryWaitContext.attempt()-1;
        double exp = Math.pow(retryWaitContext.factor(), previousAttempt);
        long result = Math.round(retryWaitContext.value() * exp);

        return super.rangeCorrect(result, retryWaitContext.min(), retryWaitContext.max());
    }

}
