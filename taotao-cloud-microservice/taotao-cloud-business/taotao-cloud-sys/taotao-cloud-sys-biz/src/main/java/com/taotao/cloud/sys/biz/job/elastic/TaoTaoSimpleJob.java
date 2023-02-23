package com.taotao.cloud.sys.biz.job.elastic;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.stereotype.Component;

/**
 * 陶道简单工作
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:26
 */
@Component
public class TaoTaoSimpleJob implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {
		//调用业务逻辑实现

		LogUtils.info("TaoTaoSimpleJob *******************");
	}
}
