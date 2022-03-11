package com.taotao.cloud.job.elastic.listener;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener;
import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;

public class MyJobListener implements ElasticJobListener {

	@Override
	public void beforeJobExecuted(ShardingContexts shardingContexts) {
		// do something ...
		LogUtil.info("beforeJobExecuted=========================");
	}

	@Override
	public void afterJobExecuted(ShardingContexts shardingContexts) {
		// do something ...

		LogUtil.info("afterJobExecuted=========================");
	}

	@Override
	public String getType() {
		return "myJobListener";
	}
}
