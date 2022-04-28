package com.taotao.cloud.sys.biz.timetask.elastic;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.calcite.adapter.jdbc.JdbcSchema.Foo;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 淘淘数据流工作
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:23
 */
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
