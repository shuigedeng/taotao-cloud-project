/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.flink.ttc.watermark;

import com.taotao.cloud.flink.doe.operator.MyPartitioner;
import java.time.Duration;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class WatermarkIdlenessDemo {

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.setParallelism(2);

		// 自定义分区器：数据%分区数，只输入奇数，都只会去往map的一个子任务
		SingleOutputStreamOperator<Integer> socketDS =
			env.socketTextStream("hadoop102", 7777)
				.partitionCustom(new MyPartitioner(), r -> r)
				.map(r -> Integer.parseInt(r))
				.assignTimestampsAndWatermarks(
					WatermarkStrategy.<Integer>forMonotonousTimestamps()
						.withTimestampAssigner((r, ts) -> r * 1000L)
						.withIdleness(Duration.ofSeconds(5)) // 空闲等待5s
				);

		// 分成两组： 奇数一组，偶数一组 ， 开10s的事件时间滚动窗口
		socketDS.keyBy(r -> r % 2)
			.window(TumblingEventTimeWindows.of(Duration.ofSeconds(10)))
			.process(
				new ProcessWindowFunction<Integer, String, Integer, TimeWindow>() {
					@Override
					public void process(
						Integer integer,
						Context context,
						Iterable<Integer> elements,
						Collector<String> out)
						throws Exception {
						long startTs = context.window().getStart();
						long endTs = context.window().getEnd();
						String windowStart =
							DateFormatUtils.format(startTs, "yyyy-MM-dd HH:mm:ss.SSS");
						String windowEnd =
							DateFormatUtils.format(endTs, "yyyy-MM-dd HH:mm:ss.SSS");

						long count = elements.spliterator().estimateSize();

						out.collect(
							"key="
								+ integer
								+ "的窗口["
								+ windowStart
								+ ","
								+ windowEnd
								+ ")包含"
								+ count
								+ "条数据===>"
								+ elements.toString());
					}
				})
			.print();

		env.execute();
	}
}
