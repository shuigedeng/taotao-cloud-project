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

import java.sql.Connection;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * TODO
 *
 * @author shuigedeng
 * @version 1.0
 */
public class SinkCustom {

    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<String> sensorDS = env.socketTextStream("hadoop102", 7777);

        sensorDS.addSink(new MySink());

        env.execute();
    }

    /**
     * MySink
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class MySink extends RichSinkFunction<String> {

        Connection conn = null;

        @Override
        public void open( OpenContext openContext ) throws Exception {
            super.open(openContext);
            // 在这里 创建连接
            // conn = new xxxx
        }

        @Override
        public void close() throws Exception {
            super.close();
            // 做一些清理、销毁连接
        }

        /**
         * sink的核心逻辑，写出的逻辑就写在这个方法里
         */
        @Override
        public void invoke( String value, Context context ) throws Exception {
            // 写出逻辑
            // 这个方法是 来一条数据，调用一次,所以不要在这里创建 连接对象

        }
    }
}
