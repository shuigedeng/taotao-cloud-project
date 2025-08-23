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

package com.taotao.cloud.flink.doe.env;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @since: 2023/12/27
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description: flink编程环境
 */
public class Demo2 {
    public static void main(String[] args) throws Exception {
        // 1 批处理的环境
        ExecutionEnvironment ee = ExecutionEnvironment.getExecutionEnvironment();

        // 2 获取编程环境 对批数据和流数据处理  [流批一体]
        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();
        // see.setParallelism() ;
        // 设置运行模式   批
        // executionEnvironment.setRuntimeMode(RuntimeExecutionMode.BATCH);
        //  流  默认
        // executionEnvironment.setRuntimeMode(RuntimeExecutionMode.STREAMING);

        // 创建环境时 可以传入用户自定义参数
        Configuration conf = new Configuration();
        StreamExecutionEnvironment.getExecutionEnvironment(conf);

        // 3 获取本地 带监控页面的环境
        // 设置页面http服务的请求端口
        conf.set("rest.port", "8888");
        StreamExecutionEnvironment see2 =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);

        see2.socketTextStream("doe01", 8899).print();

        see2.execute();
    }
}
