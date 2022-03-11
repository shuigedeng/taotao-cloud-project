package com.taotao.cloud.sys.biz.timetask.elastic;

import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.List;
import org.apache.calcite.adapter.jdbc.JdbcSchema.Foo;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.stereotype.Component;

@Component
public class TaoTaoDataflowJob implements DataflowJob<Foo> {

	@Override
	public List<Foo> fetchData(final ShardingContext shardingContext) {
		// 获取数据
		LogUtil.info("MyDataflowJob *******************");

		return null;
	}

	@Override
	public void processData(final ShardingContext shardingContext, final List<Foo> data) {
		// 处理数据
	}
}
