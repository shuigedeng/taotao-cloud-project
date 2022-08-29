package com.taotao.cloud.retry.listener;

import com.taotao.cloud.common.utils.log.LogUtils;
import io.github.itning.retry.Attempt;
import io.github.itning.retry.listener.RetryListener;

public class RetryLogListener implements RetryListener {

    @Override
    public <V> void onRetry(Attempt<V> attempt) {

        // 第几次重试,(注意:第一次重试其实是第一次调用)
        LogUtils.info("retry time : [{}]", attempt.getAttemptNumber());

        // 距离第一次重试的延迟
		LogUtils.info("retry delay : [{}]", attempt.getDelaySinceFirstAttempt());

        // 重试结果: 是异常终止, 还是正常返回
		LogUtils.info("hasException={}", attempt.hasException());
		LogUtils.info("hasResult={}", attempt.hasResult());

        // 是什么原因导致异常
        if (attempt.hasException()) {
			LogUtils.info("causeBy={}" , attempt.getExceptionCause().toString());
        } else {
            // 正常返回时的结果
			LogUtils.info("result={}" , attempt.getResult());
        }

		LogUtils.info("log listen over.");

    }
}
