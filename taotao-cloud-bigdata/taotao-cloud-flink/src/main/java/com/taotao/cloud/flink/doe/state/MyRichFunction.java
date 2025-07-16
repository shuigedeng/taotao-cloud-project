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

package com.taotao.cloud.flink.doe.state;

import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.OperatorStateStore;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;

/**
 * @since: 2024/1/5
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class MyRichFunction extends RichMapFunction<String, String>
        implements CheckpointedFunction {
    ListState<Integer> listState;
    // 自己定义的list不具备容错能力
    List<Integer> CNT = new ArrayList<Integer>();

    @Override
    public String map(String value) throws Exception {

        if ("xiaotao".equals(value)) {
            throw new RuntimeException();
        }

        listState.add(1); // 摄入一条数据后   向状态中 +1  [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]

        int cnt = 0;
        for (Integer integer : listState.get()) {
            cnt++;
        }

        // CNT.add(1) ;
        /*    for (Integer integer : CNT) {
            cnt ++  ;
        }*/

        int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
        return "doe-subtask: " + indexOfThisSubtask + "总处理数据:  " + cnt + "当前数据是:  " + value;
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {}

    /**
     * 当SubTask 第一次示例化时
     *    当SubTask容错重启时
     * @param context the context for initializing the operator
     * @throws Exception
     */
    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        // 获取算子状态数据  初始化  /  获取
        OperatorStateStore operatorStateStore = context.getOperatorStateStore();
        // 底层  类似 list 结构存储数据
        ListStateDescriptor listStateDescriptor =
                new ListStateDescriptor("cnt", TypeInformation.of(Integer.class));
        listState = operatorStateStore.getListState(listStateDescriptor);
    }
}
