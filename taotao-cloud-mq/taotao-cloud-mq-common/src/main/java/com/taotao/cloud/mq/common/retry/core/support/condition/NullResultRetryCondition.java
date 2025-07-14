package com.taotao.cloud.mq.common.retry.core.support.condition;


import com.taotao.boot.common.utils.lang.ObjectUtils;

/**
 * 空结果重试条件
 * @author shuigedeng
 * @since 0.0.3
 */
public class NullResultRetryCondition<R> extends AbstractResultRetryCondition<R> {

    @Override
    protected boolean resultCondition(R result) {
        return ObjectUtils.isNull(result);
    }

}
