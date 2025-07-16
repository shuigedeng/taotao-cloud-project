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

import java.util.List;
import java.util.Map;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.functions.PatternProcessFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * JBatchWordCount
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/11/3 09:05
 */
public class JBatchWordCountDemo {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setInteger(RestOptions.PORT, 8050);

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        //		DataGeneratorSource<FlinkUser> dataGeneratorSource = new DataGeneratorSource<>(
        //			new GeneratorFunction<Long, FlinkUser>() {
        //				// 定义随机数数据生成器
        //				public RandomDataGenerator generator;
        //				public int i = 3;
        //
        //				@Override
        //				public void open(SourceReaderContext readerContext) throws Exception {
        //					generator = new RandomDataGenerator();
        //				}
        //
        //				@Override
        //				public FlinkUser map(Long value) throws Exception {
        //
        //					// 使用 随机数数据生成器 来创建 FlinkUser实例
        //					FlinkUser flinkUser = new FlinkUser(value
        //						, "1"
        //						, System.currentTimeMillis(),
        //						i
        //					);
        //
        //					i = generator.nextInt(1, 2);
        //					return flinkUser;
        //				}
        //			},
        //			Long.MAX_VALUE,
        //			RateLimiterStrategy.perSecond(1L),
        //			TypeInformation.of(FlinkUser.class));
        //		DataGeneratorSource<Long> longGeneratorSource = new DataGeneratorSource<>(new
        // GeneratorFunction<Long, Long>() {
        //
        //			@Override
        //			public Long map(Long value) throws Exception {
        //				// 使用 随机数数据生成器 来创建 FlinkUser实例
        //				return value;
        //			}
        //		},
        //			Long.MAX_VALUE,
        //			RateLimiterStrategy.perSecond(3L),
        //			TypeInformation.of(Long.class));

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

        //		DataStream<FlinkUser> mainStream = env.fromSource(mapSource,
        //			WatermarkStrategy.noWatermarks(), "data-generator");
        //		DataStream<FlinkUser> broadcastStream = env.fromSource(dataGeneratorSource,
        // WatermarkStrategy.noWatermarks(),  "data-generator");

        SingleOutputStreamOperator<FlinkUser> streamOperatorMain =
                mainStream.assignTimestampsAndWatermarks(
                        WatermarkStrategy.<FlinkUser>forMonotonousTimestamps());

        OutputTag<FlinkUserContext> outputTag =
                new OutputTag<>("xxxx", TypeInformation.of(FlinkUserContext.class));
        SingleOutputStreamOperator<FlinkUser> process =
                streamOperatorMain
                        .filter(
                                new FilterFunction<FlinkUser>() {
                                    @Override
                                    public boolean filter(FlinkUser value) throws Exception {
                                        return value.getGender() == 3;
                                    }
                                })
                        .keyBy(s -> true)
                        .process(
                                new KeyedProcessFunction<Boolean, FlinkUser, FlinkUser>() {
                                    private MapState<Integer, FlinkUser> mapState;

                                    @Override
                                    public void open(Configuration parameters) throws Exception {
                                        super.open(parameters);
                                        MapStateDescriptor<Integer, FlinkUser> mapStateDescriptor =
                                                new MapStateDescriptor<>(
                                                        "valueStateDesc",
                                                        TypeInformation.of(
                                                                new TypeHint<Integer>() {}),
                                                        TypeInformation.of(
                                                                new TypeHint<FlinkUser>() {}));
                                        mapState =
                                                getRuntimeContext().getMapState(mapStateDescriptor);
                                    }

                                    @Override
                                    public void processElement(
                                            FlinkUser flinkUser,
                                            KeyedProcessFunction<Boolean, FlinkUser, FlinkUser>
                                                            .Context
                                                    ctx,
                                            Collector<FlinkUser> out)
                                            throws Exception {
                                        System.out.println(
                                                "processElement=====" + flinkUser.toString());

                                        Integer gender = flinkUser.getGender();
                                        mapState.put(gender, flinkUser);

                                        // 注册ProcessingTime的定时器
                                        long createtime = flinkUser.getCreatetime();
                                        long fireTime = createtime + 5000;
                                        ctx.timerService().registerProcessingTimeTimer(fireTime);

                                        out.collect(flinkUser);
                                    }

                                    @Override
                                    public void onTimer(
                                            long timestamp,
                                            KeyedProcessFunction<Boolean, FlinkUser, FlinkUser>
                                                            .OnTimerContext
                                                    ctx,
                                            Collector<FlinkUser> out)
                                            throws Exception {
                                        for (FlinkUser value : mapState.values()) {
                                            System.out.println("onTimer=====" + value.toString());

                                            long currentProcessingTime =
                                                    ctx.timerService().currentProcessingTime();
                                            ctx.timerService()
                                                    .deleteEventTimeTimer(currentProcessingTime);

                                            if (currentProcessingTime > Long.MAX_VALUE - 1) {
                                                System.out.println("清空state");
                                                mapState.clear();
                                            } else {
                                                long fireTime = currentProcessingTime + 5000;
                                                System.out.println("注册timeer");
                                                ctx.timerService()
                                                        .registerProcessingTimeTimer(fireTime);

                                                ctx.output(
                                                        outputTag,
                                                        new FlinkUserContext(
                                                                value,
                                                                ctx.timerService(),
                                                                fireTime));
                                            }
                                        }
                                    }

                                    @Override
                                    public void close() throws Exception {
                                        super.close();
                                    }
                                });
        SideOutputDataStream<FlinkUserContext> sideOutput = process.getSideOutput(outputTag);

        Pattern<FlinkUserContext, FlinkUserContext> pattern =
                Pattern.<FlinkUserContext>begin("first") // 以第一个登录失败事件开始
                        .where(
                                new SimpleCondition<FlinkUserContext>() {
                                    @Override
                                    public boolean filter(FlinkUserContext loginEvent)
                                            throws Exception {
                                        return loginEvent.getFlinkUser().getGender() == 3;
                                    }
                                })
                        .next("end") // 接着是第三个登录失败事件
                        .where(
                                new SimpleCondition<FlinkUserContext>() {
                                    @Override
                                    public boolean filter(FlinkUserContext loginEvent)
                                            throws Exception {
                                        return true;
                                    }
                                });

        PatternStream<FlinkUserContext> patternStream = CEP.pattern(sideOutput, pattern);
        patternStream
                .process(
                        new PatternProcessFunction<FlinkUserContext, FlinkUserContext>() {
                            @Override
                            public void processMatch(
                                    Map<String, List<FlinkUserContext>> match,
                                    Context ctx,
                                    Collector<FlinkUserContext> out)
                                    throws Exception {
                                System.out.println("match========" + match.toString());
                                List<FlinkUserContext> first = match.get("first");
                                List<FlinkUserContext> end = match.get("end");

                                FlinkUserContext next = first.iterator().next();
                                next.getTimerService()
                                        .deleteProcessingTimeTimer(next.getFireTime());
                            }
                        })
                .print();

        //		SingleOutputStreamOperator<FlinkUserContext> output =
        //			CEP.dynamicPatterns(
        //				sideOutput,
        //				new JDBCPeriodicPatternProcessorDiscovererFactory<>(
        //					"",
        //					JDBC_DRIVE,
        //					"",
        //					null,
        //					Long.parseLong("500")),
        //				TimeBehaviour.ProcessingTime,
        //				TypeInformation.of(new TypeHint<FlinkUserContext>() {
        //				}));

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

    public static class FlinkUserContext {

        private FlinkUser flinkUser;
        private TimerService timerService;
        private long fireTime;

        public FlinkUserContext() {}

        public FlinkUserContext(FlinkUser flinkUser, TimerService timerService, long fireTime) {
            this.flinkUser = flinkUser;
            this.timerService = timerService;
            this.fireTime = fireTime;
        }

        public FlinkUser getFlinkUser() {
            return flinkUser;
        }

        public void setFlinkUser(FlinkUser flinkUser) {
            this.flinkUser = flinkUser;
        }

        public TimerService getTimerService() {
            return timerService;
        }

        public void setTimerService(TimerService timerService) {
            this.timerService = timerService;
        }

        public long getFireTime() {
            return fireTime;
        }

        public void setFireTime(long fireTime) {
            this.fireTime = fireTime;
        }
    }
}
