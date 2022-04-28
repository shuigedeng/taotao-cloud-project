package com.taotao.cloud.job.elastic.listener;

import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.apache.shardingsphere.elasticjob.lite.api.listener.AbstractDistributeOnceElasticJobListener;

/**
 * 我分发一次工作侦听器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:29:40
 */
public class MyDistributeOnceJobListener extends AbstractDistributeOnceElasticJobListener {

	/**
	 * 开始超时米尔斯
	 */
	private static final long startTimeoutMills = 3000;
	/**
	 * 完整超时米尔斯
	 */
	private static final long completeTimeoutMills = 3000;

	public MyDistributeOnceJobListener() {
		super(startTimeoutMills, completeTimeoutMills);
	}


	/**
	 * 之前工作终于开始执行吗
	 *
	 * @param shardingContexts 分片上下文
	 * @since 2022-04-27 17:29:40
	 */
	@Override
	public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
		// do something ...
	}

	/**
	 * 执行工作最后完成后做了什么
	 *
	 * @param shardingContexts 分片上下文
	 * @since 2022-04-27 17:29:40
	 */
	@Override
	public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
		// do something ...
	}

	/**
	 * 得到类型
	 *
	 * @return {@link String }
	 * @since 2022-04-27 17:29:40
	 */
	@Override
	public String getType() {
		return "distributeOnceJobListener";
	}
}
