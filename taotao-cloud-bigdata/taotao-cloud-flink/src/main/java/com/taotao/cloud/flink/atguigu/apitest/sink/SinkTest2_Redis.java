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

package com.taotao.cloud.flink.atguigu.apitest.sink;

import com.taotao.cloud.flink.atguigu.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

// import org.apache.flink.streaming.connectors.redis.RedisSink;
// import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
// import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
// import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
// import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * SinkTest2_Redis
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class SinkTest2_Redis {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 从文件读取数据
        DataStream<String> inputStream =
                env.readTextFile(
                        "D:\\Projects\\BigData\\FlinkTutorial\\src\\main\\resources\\sensor.txt");

        // 转换成SensorReading类型
        DataStream<SensorReading> dataStream =
                inputStream.map(
                        line -> {
                            String[] fields = line.split(",");
                            return new SensorReading(
                                    fields[0], new Long(fields[1]), new Double(fields[2]));
                        });

        //     // 定义jedis连接配置
        //     FlinkJedisPoolConfig config = new FlinkJedisPoolConfig.Builder()
        //             .setHost("localhost")
        //             .setPort(6379)
        //             .build();
        //
        //     dataStream.addSink( new RedisSink<>(config, new MyRedisMapper()));
        //
        //     env.execute();
        // }
        //
        // // 自定义RedisMapper
        // public static class MyRedisMapper implements RedisMapper<SensorReading>{
        //     // 定义保存数据到redis的命令，存成Hash表，hset sensor_temp id temperature
        //     @Override
        //     public RedisCommandDescription getCommandDescription() {
        //         return new RedisCommandDescription(RedisCommand.HSET, "sensor_temp");
        //     }
        //
        //     @Override
        //     public String getKeyFromData(SensorReading data) {
        //         return data.getId();
        //     }
        //
        //     @Override
        //     public String getValueFromData(SensorReading data) {
        //         return data.getTemperature().toString();
        //     }
        // }
    }
}
