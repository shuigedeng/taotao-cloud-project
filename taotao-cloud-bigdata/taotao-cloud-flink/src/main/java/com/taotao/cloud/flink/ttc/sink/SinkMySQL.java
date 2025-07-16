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

package com.taotao.cloud.flink.ttc.sink;

import com.taotao.cloud.flink.ttc.bean.WaterSensor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SinkMySQL {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS =
                env.socketTextStream("hadoop102", 7777).map(new WaterSensorMapFunction());

        /**
         * TODO 写入mysql
         * 1、只能用老的sink写法： addsink
         * 2、JDBCSink的4个参数:
         *    第一个参数： 执行的sql，一般就是 insert into
         *    第二个参数： 预编译sql， 对占位符填充值
         *    第三个参数： 执行选项 ---》 攒批、重试
         *    第四个参数： 连接选项 ---》 url、用户名、密码
         */
        SinkFunction<WaterSensor> jdbcSink =
                JdbcSink.sink(
                        "insert into ws values(?,?,?)",
                        new JdbcStatementBuilder<WaterSensor>() {
                            @Override
                            public void accept(
                                    PreparedStatement preparedStatement, WaterSensor waterSensor)
                                    throws SQLException {
                                // 每收到一条WaterSensor，如何去填充占位符
                                preparedStatement.setString(1, waterSensor.getId());
                                preparedStatement.setLong(2, waterSensor.getTs());
                                preparedStatement.setInt(3, waterSensor.getVc());
                            }
                        },
                        JdbcExecutionOptions.builder()
                                .withMaxRetries(3) // 重试次数
                                .withBatchSize(100) // 批次的大小：条数
                                .withBatchIntervalMs(3000) // 批次的时间
                                .build(),
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withUrl(
                                        "jdbc:mysql://hadoop102:3306/test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8")
                                .withUsername("root")
                                .withPassword("000000")
                                .withConnectionCheckTimeoutSeconds(60) // 重试的超时时间
                                .build());

        sensorDS.addSink(jdbcSink);

        env.execute();
    }
}
