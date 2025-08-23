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

package com.taotao.cloud.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * JBatchWordCount
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/3 09:05
 */
public class JBatchWordCountDemo1 {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set(RestOptions.PORT, 8050);

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(4);

        DataStream<String> dss = env.socketTextStream("127.0.0.1", 8888);
        SingleOutputStreamOperator<FlinkUser> mainStream =
                dss.map(
                        new RichMapFunction<String, FlinkUser>() {
                            @Override
                            public FlinkUser map(String value) throws Exception {
                                String[] split = value.split(",");
                                return new FlinkUser(
                                        Long.valueOf(split[0]),
                                        split[1],
                                        Long.valueOf(split[2]),
                                        Integer.valueOf(split[3]));
                            }
                        });

        DataStream<String> broadcastSource = env.socketTextStream("127.0.0.1", 8889);
        MapStateDescriptor<String, String> stringshipMapStateDescriptor =
                new MapStateDescriptor<String, String>("state", String.class, String.class);
        BroadcastStream<String> broadcastStream =
                broadcastSource.broadcast(stringshipMapStateDescriptor);

        SingleOutputStreamOperator<FlinkUser> streamOperatorMain =
                mainStream.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<FlinkUser>forMonotonousTimestamps());

        SingleOutputStreamOperator<FlinkUser> filter =
                streamOperatorMain.filter(
                        new FilterFunction<FlinkUser>() {
                            @Override
                            public boolean filter(FlinkUser value) throws Exception {
                                return value.getGender() == 1;
                            }
                        });
        BroadcastConnectedStream<FlinkUser, String> connect = filter.connect(broadcastStream);
        SingleOutputStreamOperator<FlinkUser> singleOutputStreamOperator =
                connect.process(
                        new BroadcastProcessFunction<FlinkUser, String, FlinkUser>() {
                            BroadcastState<String, String> broadcastState;

                            @Override
                            public void processElement(
                                    FlinkUser flinkUser,
                                    BroadcastProcessFunction<FlinkUser, String, FlinkUser>
                                                    .ReadOnlyContext
                                            readOnlyContext,
                                    Collector<FlinkUser> collector)
                                    throws Exception {
                                if (broadcastState != null) {
                                    String value = broadcastState.iterator().next().getValue();
                                    flinkUser.setKey(value);
                                }
                                collector.collect(flinkUser);
                            }

                            @Override
                            public void processBroadcastElement(
                                    String s,
                                    BroadcastProcessFunction<FlinkUser, String, FlinkUser>.Context
                                            context,
                                    Collector<FlinkUser> collector)
                                    throws Exception {
                                broadcastState =
                                        context.getBroadcastState(stringshipMapStateDescriptor);
                                broadcastState.put("tt", s);
                            }
                        });

        SingleOutputStreamOperator<String> map =
                singleOutputStreamOperator.map(
                        new RichMapFunction<FlinkUser, String>() {
                            @Override
                            public String map(FlinkUser value) throws Exception {
                                return value.getKey();
                            }
                        });

        BroadcastConnectedStream<String, String> connect1 = map.connect(broadcastStream);
        connect1.process(
                        new BroadcastProcessFunction<String, String, Tuple2<String, String>>() {
                            BroadcastState<String, String> broadcastState;

                            @Override
                            public void processElement(
                                    String s,
                                    BroadcastProcessFunction<String, String, Tuple2<String, String>>
                                                    .ReadOnlyContext
                                            readOnlyContext,
                                    Collector<Tuple2<String, String>> collector)
                                    throws Exception {
                                if (broadcastState != null) {
                                    String value = broadcastState.iterator().next().getValue();
                                    collector.collect(
                                            Tuple2.of(
                                                    s,
                                                    broadcastState.iterator().next().getValue()));
                                } else {
                                    collector.collect(Tuple2.of(s, "sb"));
                                }
                            }

                            @Override
                            public void processBroadcastElement(
                                    String s,
                                    BroadcastProcessFunction<String, String, Tuple2<String, String>>
                                                    .Context
                                            context,
                                    Collector<Tuple2<String, String>> collector)
                                    throws Exception {
                                broadcastState =
                                        context.getBroadcastState(stringshipMapStateDescriptor);
                                broadcastState.put("tt", s);
                            }
                        })
                .print();

        //			.process(new KeyedProcessFunction<Boolean, FlinkUser, FlinkUser>() {
        //				private MapState<Integer, FlinkUser> mapState;
        //
        //				@Override
        //				public void open(OpenContext openContext) throws Exception {
        //					super.open(openContext);
        //					MapStateDescriptor<Integer, FlinkUser> mapStateDescriptor = new MapStateDescriptor<>(
        //						"valueStateDesc",
        //						TypeInformation.of(new TypeHint<Integer>() {
        //						}),
        //						TypeInformation.of(new TypeHint<FlinkUser>() {
        //						}));
        //					mapState = getRuntimeContext().getMapState(mapStateDescriptor);
        //				}
        //
        //				@Override
        //				public void processElement(FlinkUser flinkUser,
        //					KeyedProcessFunction<Boolean, FlinkUser, FlinkUser>.Context ctx,
        //					Collector<FlinkUser> out) throws Exception {
        //					System.out.println("processElement=====" + flinkUser.toString());
        //
        //					Integer gender = flinkUser.getGender();
        //					mapState.put(gender, flinkUser);
        //
        //					//注册ProcessingTime的定时器
        //					long createtime = flinkUser.getCreatetime();
        //					long fireTime = createtime + 5000;
        //					ctx.timerService().registerProcessingTimeTimer(fireTime);
        //
        //					out.collect(flinkUser);
        //				}
        //
        //				@Override
        //				public void onTimer(long timestamp,
        //					KeyedProcessFunction<Boolean, FlinkUser, FlinkUser>.OnTimerContext ctx,
        //					Collector<FlinkUser> out) throws Exception {
        //
        //					for (FlinkUser value : mapState.values()) {
        //						System.out.println("onTimer=====" + value.toString());
        //
        //						long currentProcessingTime = ctx.timerService().currentProcessingTime();
        //						ctx.timerService().deleteEventTimeTimer(currentProcessingTime);
        //
        //						if (currentProcessingTime > Long.MAX_VALUE - 1) {
        //							System.out.println("清空state");
        //							mapState.clear();
        //						}else {
        //							long fireTime = currentProcessingTime + 5000;
        //							System.out.println("注册timeer");
        //							ctx.timerService().registerProcessingTimeTimer(fireTime);
        //
        //							out.collect(value);
        //						}
        //					}
        //
        //				}
        //
        //				@Override
        //				public void close() throws Exception {
        //					super.close();
        //				}
        //			}).print();

        //		KeyedStream<String, String> keyedBy = dso.keyBy(new KeySelector<String, String>() {
        //			@Override
        //			public String getKey(String value) throws Exception {
        //				return value;
        //			}
        //		});
        //		WindowedStream<String, String, TimeWindow> window =
        // keyedBy.window(EventTimeSessionWindows.withDynamicGap(new
        // SessionWindowTimeGapExtractor<String>() {
        //			@Override
        //			public long extract(String s) {
        //				return 0;
        //			}
        //		}));
        //
        //		DataStream<Tuple2<String, Integer>> dst = dso
        //			.map(new MapFunction<String, Tuple2<String, Integer>>() {
        //				@Override
        //				public Tuple2<String, Integer> map(String value) throws Exception {
        //					return Tuple2.of(value, 1);
        //				}
        //			});
        //
        //		KeyedStream<Tuple2<String, Integer>, String> kst = dst
        //			.keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
        //				@Override
        //				public String getKey(Tuple2<String, Integer> value) throws Exception {
        //					return value.f0;
        //				}
        //			});
        //
        //		SingleOutputStreamOperator<Tuple2<String, Integer>> sum = kst.sum(1);
        //
        //		sum.print();

        env.execute("JBatchWordCount");
    }

    public static class FlinkUser {

        public Long id;
        public String name;
        public Integer gender;
        public Long createtime;
        public String key;

        // 一定要提供一个 空参 的构造器(反射的时候要使用)
        public FlinkUser() {}

        public FlinkUser(Long id, String name, Long createtime, Integer gender) {
            this.id = id;
            this.name = name;
            this.createtime = createtime;
            this.gender = gender;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Long createtime) {
            this.createtime = createtime;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public String toString() {
            return "FlinkUser{"
                    + "id="
                    + id
                    + ", name='"
                    + name
                    + '\''
                    + ", createtime="
                    + createtime
                    + ", gender="
                    + gender
                    + ", key="
                    + key
                    + '}';
        }
    }

    //	public static class ContinuousEventTimeTrigger extends Trigger<FlinkUser, TimeWindow> {
    //
    //		@Override
    //		public TriggerResult onElement(FlinkUser element, long timestamp, TimeWindow window,
    // TriggerContext ctx) throws Exception {
    //
    //		}
    //
    //		@Override
    //		public TriggerResult onProcessingTime(long l, TimeWindow window,
    //			TriggerContext ctx) throws Exception {
    //			if (window.maxTimestamp() <= ctx.getCurrentWatermark()) {
    //				// if the watermark is already past the window fire immediately
    //				return TriggerResult.FIRE;
    //			} else {
    //				ctx.registerEventTimeTimer(window.maxTimestamp());
    //			}
    //
    //			ReducingState<Long> fireTimestamp = ctx.getPartitionedState(stateDesc);
    //			if (fireTimestamp.get() == null) {
    //				//没有注册过定时器
    //				long start = timestamp - (timestamp % interval);
    //				long nextFireTimestamp = start + interval;
    //				//注册下一个触发时间
    //				ctx.registerEventTimeTimer(nextFireTimestamp);
    //				fireTimestamp.add(nextFireTimestamp);
    //			}
    //
    //			return TriggerResult.CONTINUE;
    //		}
    //
    //		@Override
    //		public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws
    // Exception {
    //			//这里传入的time，注释是说就是触发时间
    //			if (time == window.maxTimestamp()){
    //				//达到watarmark，触发
    //				return TriggerResult.FIRE;
    //			}
    //
    //			ReducingState<Long> fireTimestampState = ctx.getPartitionedState(stateDesc);
    //
    //			Long fireTimestamp = fireTimestampState.get();
    //
    //			if (fireTimestamp != null && fireTimestamp == time) {
    //				//达到之前注册的定时器，触发
    //				fireTimestampState.clear();
    //				fireTimestampState.add(time + interval);
    //				ctx.registerEventTimeTimer(time + interval);
    //				return TriggerResult.FIRE;
    //			}
    //
    //			return TriggerResult.CONTINUE;
    //		}
    //
    //		@Override
    //		public void clear(TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
    //
    //		}
    //	}
}
