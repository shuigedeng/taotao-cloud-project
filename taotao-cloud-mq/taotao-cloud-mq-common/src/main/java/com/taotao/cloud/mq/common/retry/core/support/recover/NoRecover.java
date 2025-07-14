package com.taotao.cloud.mq.common.retry.core.support.recover;

import com.taotao.boot.common.support.instance.impl.InstanceFactory;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.recover.Recover;

/**
 * 不指定任何动作
 *
 * @author shuigedeng
 * @since 0.0.1
 */
public class NoRecover implements Recover {

	/**
	 * 获取一个单例示例
	 *
	 * @return 单例示例
	 */
	public static Recover getInstance() {
		return InstanceFactory.getInstance().singleton(NoRecover.class);
	}

	@Override
	public <R> void recover(RetryAttempt<R> retryAttempt) {

	}
}
