package com.taotao.cloud.sys.biz.timetask.elastic;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;

@Component
public class TaoTaoSimpleJob implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {
		//调用业务逻辑实现

		LogUtil.info("TaoTaoSimpleJob *******************");
	}
}
