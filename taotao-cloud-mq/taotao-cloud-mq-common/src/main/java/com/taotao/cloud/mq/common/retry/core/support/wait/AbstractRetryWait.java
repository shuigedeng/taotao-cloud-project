package com.taotao.cloud.mq.common.retry.core.support.wait;

import com.taotao.cloud.mq.common.retry.api.model.WaitTime;
import com.taotao.cloud.mq.common.retry.api.support.wait.RetryWait;
import com.taotao.cloud.mq.common.retry.core.model.DefaultWaitTime;

/**
 * 抽象重试时间等待
 * @author shuigedeng
 * @since 0.0.3
 */
public abstract class AbstractRetryWait implements RetryWait {

    /**
     * 修正范围
     * @param timeMills 结果
     * @param min 最小值
     * @param max 最大值
     * @return 修正范围
     */
    protected WaitTime rangeCorrect(final long timeMills, final long min, final long max) {
        long resultMills = timeMills;
        if(timeMills > max) {
            resultMills = max;
        }
        if(timeMills < min) {
            resultMills = min;
        }
        return new DefaultWaitTime(resultMills);
    }

}
