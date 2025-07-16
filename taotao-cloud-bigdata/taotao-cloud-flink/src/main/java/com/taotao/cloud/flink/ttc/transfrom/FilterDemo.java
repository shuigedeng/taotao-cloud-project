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

package com.taotao.cloud.flink.ttc.transfrom;

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import com.taotao.cloud.flink.ttc.functions.FilterFunctionImpl;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class FilterDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<WaterSensor> sensorDS =
                env.fromElements(
                        new WaterSensor("s1", 1L, 1),
                        new WaterSensor("s1", 11L, 11),
                        new WaterSensor("s2", 2L, 2),
                        new WaterSensor("s3", 3L, 3));

        // TODO filter： true保留，false过滤掉
        //        SingleOutputStreamOperator<WaterSensor> filter = sensorDS.filter(new
        // FilterFunction<WaterSensor>() {
        //            @Override
        //            public boolean filter(WaterSensor value) throws Exception {
        //                return "s1".equals(value.getId());
        //            }
        //        });

        SingleOutputStreamOperator<WaterSensor> filter =
                sensorDS.filter(new FilterFunctionImpl("s1"));

        filter.print();

        env.execute();
    }
}
