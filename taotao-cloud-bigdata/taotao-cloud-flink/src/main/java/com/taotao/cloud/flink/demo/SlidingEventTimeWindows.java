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

package com.taotao.cloud.flink.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner.WindowAssignerContext;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.runtime.operators.window.TimeWindow;

public class SlidingEventTimeWindows extends WindowAssigner<Object, TimeWindow> {

    private static final long serialVersionUID = 1L;

    // 窗口大小
    private final long size;
    // 滑动步长
    private final long slide;

    private final long offset;

    protected SlidingEventTimeWindows(long size, long slide, long offset) {
        if (Math.abs(offset) >= slide || size <= 0) {
            throw new IllegalArgumentException(
                    "SlidingEventTimeWindows parameters must satisfy "
                            + "abs(offset) < slide and size > 0");
        }

        this.size = size;
        this.slide = slide;
        this.offset = offset;
    }

    // 根据size以及slide去分配窗口，那么我们可以在这个地方动态调整size以及slide，实现窗口动态变化
    // 我们发现，每次分配窗口的时候都会将原始的数据传进来，那么我们就可以在element上抽取动态改变的数据
    @Override
    public Collection<TimeWindow> assignWindows(
            Object element, long timestamp, WindowAssignerContext context) {
        if (timestamp > Long.MIN_VALUE) {
            List<TimeWindow> windows = new ArrayList<>((int) (size / slide));
            long lastStart = TimeWindow.getWindowStartWithOffset(timestamp, offset, slide);
            for (long start = lastStart; start > timestamp - size; start -= slide) {
                windows.add(new TimeWindow(start, start + size));
            }
            return windows;
        } else {
            throw new RuntimeException(
                    "Record has Long.MIN_VALUE timestamp (= no timestamp marker). "
                            + "Is the time characteristic set to 'ProcessingTime', or did you forget to call "
                            + "'DataStream.assignTimestampsAndWatermarks(...)'?");
        }
    }

    @Override
    public Collection<Object> assignWindows(RowData rowData, long l) throws IOException {
        return null;
    }

    @Override
    public TypeSerializer<Object> getWindowSerializer(ExecutionConfig executionConfig) {
        return null;
    }

    @Override
    public boolean isEventTime() {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    //	StreamExecutionEnvironment env = FlinkEnvironment.getEnv(true,1);
    //	JobConfig config = new JobConfig();
    //        env.getConfig().setGlobalJobParameters(config.getParameterTool());

    //	SingleOutputStreamOperator<String> source = env.addSource(new FakeRecordSource(100))
    //		.assignTimestampsAndWatermarks(watermarkStrategy).setParallelism(2);
    //	//读取配置，将需要调整的时间写入每条数据
    //	SingleOutputStreamOperator<FakeRecordSource.TrafficRecord> resultWithAdjustMap = source
    // .map(new AddAdjustTimeFunction);
    //
    //	SingleOutputStreamOperator<FakeRecordSource.TrafficRecord> result =
    // resultWithAdjustMap.keyBy(new KeySelector<FakeRecordSource.TrafficRecord, Integer>() {
    //			@Override
    //			public Integer getKey(FakeRecordSource.TrafficRecord s) throws Exception {
    //				return s.getCityId();
    //			}
    //		}).window(DynSlidingEventTimeWindows.of(Time.seconds(2), Time.seconds(1), Time.seconds(0),
    // new TimeAdjustExtractor() {
    //			@Override
    //			public long extract(Object element) {
    //				return ((FakeRecordSource.TrafficRecord)element).getAdjustSize();
    //			}
    //		}, new TimeAdjustExtractor() {
    //			@Override
    //			public long extract(Object element) {
    //				return ((FakeRecordSource.TrafficRecord)element).getAdjustSlide();
    //			}
    //		}))
    //		.process(new ProcessWindowFunction<FakeRecordSource.TrafficRecord,
    // FakeRecordSource.TrafficRecord, Integer, TimeWindow>() {
    //			@Override
    //			public void process(Integer integer, Context context,
    // Iterable<FakeRecordSource.TrafficRecord> iterable, Collector<FakeRecordSource.TrafficRecord>
    // collector) throws Exception {
    //
    //
    //			}
    //		}).setParallelism(2);
    //
    //        result.addSink(new SinkFunction<FakeRecordSource.TrafficRecord>() {
    //		@Override
    //		public void invoke(FakeRecordSource.TrafficRecord value) throws Exception {
    //
    //		}
    //	}).setParallelism(2);
    //
    //        env.execute("flink-dynamic");
}
