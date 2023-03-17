package com.taotao.cloud.job.biz.elastic;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.job.biz.elastic.TaoTaoDataflowJob.Foo;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;
import org.springframework.stereotype.Component;

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
		LogUtils.info("MyDataflowJob *******************");
		LogUtils.info("Item : {}, Time: {}, Thread: {}, type:  {}",
			shardingContext.getShardingItem(),
			LocalDateTime.now(), Thread.currentThread().getId(), "DATAFLOW");

		return new ArrayList<>();
	}

	@Override
	public void processData(final ShardingContext shardingContext, final List<Foo> data) {
		// 处理数据
		LogUtils.info("Item : {}, Time: {}, Thread: {}, type:  {}",
			shardingContext.getShardingItem(),
			LocalDateTime.now(), Thread.currentThread().getId(), "DATAFLOW");

	}

	public static class Foo implements Serializable {

		private final long id;
		private final String location;
		private final String status;

		public Foo(long id, String location, String status) {
			this.id = id;
			this.location = location;
			this.status = status;
		}

		public long getId() {
			return id;
		}

		public String getLocation() {
			return location;
		}

		public String getStatus() {
			return status;
		}
	}
}
