package com.taotao.cloud.job.elastic.listener;

import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.apache.shardingsphere.elasticjob.lite.api.listener.AbstractDistributeOnceElasticJobListener;

public class MyDistributeOnceJobListener extends AbstractDistributeOnceElasticJobListener {

	private static final long startTimeoutMills = 3000;
	private static final long completeTimeoutMills = 3000;

	public MyDistributeOnceJobListener() {
		super(startTimeoutMills, completeTimeoutMills);
	}


	@Override
	public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
		// do something ...
	}

	@Override
	public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
		// do something ...
	}

	@Override
	public String getType() {
		return "distributeOnceJobListener";
	}
}
