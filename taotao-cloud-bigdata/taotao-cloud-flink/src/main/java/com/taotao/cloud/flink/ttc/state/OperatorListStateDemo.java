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

package com.taotao.cloud.flink.ttc.state;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO 在map算子中计算数据的个数
 *
 * @author shuigedeng
 * @version 1.0
 */
public class OperatorListStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        env.socketTextStream("hadoop102", 7777).map(new MyCountMapFunction()).print();

        env.execute();
    }

    // TODO 1.实现 CheckpointedFunction 接口
    public static class MyCountMapFunction
            implements MapFunction<String, Long>, CheckpointedFunction {

        private Long count = 0L;
        private ListState<Long> state;

        @Override
        public Long map(String value) throws Exception {
            return ++count;
        }

        /**
         * TODO 2.本地变量持久化：将 本地变量 拷贝到 算子状态中,开启checkpoint时才会调用
         *
         * @param context
         * @throws Exception
         */
        @Override
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            System.out.println("snapshotState...");
            // 2.1 清空算子状态
            state.clear();
            // 2.2 将 本地变量 添加到 算子状态 中
            state.add(count);
        }

        /**
         * TODO 3.初始化本地变量：程序启动和恢复时， 从状态中 把数据添加到 本地变量，每个子任务调用一次
         *
         * @param context
         * @throws Exception
         */
        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            System.out.println("initializeState...");
            // 3.1 从 上下文 初始化 算子状态
            state =
                    context.getOperatorStateStore()
                            .getListState(new ListStateDescriptor<Long>("state", Types.LONG));
            //                    .getUnionListState(new ListStateDescriptor<Long>("union-state",
            // Types.LONG));

            // 3.2 从 算子状态中 把数据 拷贝到 本地变量
            if (context.isRestored()) {
                for (Long c : state.get()) {
                    count += c;
                }
            }
        }
    }
}

/**
 * 算子状态中， list 与 unionlist的区别：  并行度改变时，怎么重新分配状态
 * 1、list状态：  轮询均分 给 新的 并行子任务
 * 2、unionlist状态： 原先的多个子任务的状态，合并成一份完整的。 会把 完整的列表 广播给 新的并行子任务 （每人一份完整的）
 */
