package com.taotao.cloud.flink.ttc.state;

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import com.atguigu.functions.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Map;

/**
 * TODO 计算每种传感器的水位和
 *
 * @author cjp
 * @version 1.0
 */
public class KeyedReducingStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);


        SingleOutputStreamOperator<WaterSensor> sensorDS = env
                .socketTextStream("hadoop102", 7777)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((element, ts) -> element.getTs() * 1000L)
                );

        sensorDS.keyBy(r -> r.getId())
                .process(
                        new KeyedProcessFunction<String, WaterSensor, String>() {

                            ReducingState<Integer> vcSumReducingState;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);
                                vcSumReducingState = getRuntimeContext()
                                        .getReducingState(
                                                new ReducingStateDescriptor<Integer>(
                                                        "vcSumReducingState",
                                                        new ReduceFunction<Integer>() {
                                                            @Override
                                                            public Integer reduce(Integer value1, Integer value2) throws Exception {
                                                                return value1 + value2;
                                                            }
                                                        },
                                                        Types.INT
                                                )
                                        );
                            }

                            @Override
                            public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
                                // 来一条数据，添加到 reducing状态里
                                vcSumReducingState.add(value.getVc());
                                Integer vcSum = vcSumReducingState.get();
                                out.collect("传感器id为" + value.getId() + ",水位值总和=" + vcSum);


//                                vcSumReducingState.get();   // 对本组的Reducing状态，获取结果
//                                vcSumReducingState.add();   // 对本组的Reducing状态，添加数据
//                                vcSumReducingState.clear(); // 对本组的Reducing状态，清空数据
                            }
                        }
                )
                .print();

        env.execute();
    }
}
