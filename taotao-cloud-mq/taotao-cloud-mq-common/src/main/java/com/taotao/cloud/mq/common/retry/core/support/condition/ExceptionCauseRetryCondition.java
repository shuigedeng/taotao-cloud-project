package com.taotao.cloud.mq.common.retry.core.support.condition;


import com.taotao.boot.common.utils.lang.ObjectUtils;

/**
 * 有异常则触发重试
 *
 * @author shuigedeng
 * @since 0.0.3
 */
public class ExceptionCauseRetryCondition extends AbstractCauseRetryCondition {

    @Override
    protected boolean causeCondition(Throwable throwable) {
        return ObjectUtils.isNotNull(throwable);
    }

}
