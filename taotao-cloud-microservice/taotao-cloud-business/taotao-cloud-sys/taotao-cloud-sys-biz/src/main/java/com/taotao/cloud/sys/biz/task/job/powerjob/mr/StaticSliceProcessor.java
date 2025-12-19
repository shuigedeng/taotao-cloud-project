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

package com.taotao.cloud.sys.biz.task.job.powerjob.mr;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.TaskResult;
import tech.powerjob.worker.core.processor.sdk.MapReduceProcessor;
import tech.powerjob.worker.log.OmsLogger;

/**
 * MapReduce 模拟 静态分片 典型的杀鸡焉用牛刀～
 */
@Component
public class StaticSliceProcessor implements MapReduceProcessor {

    @Override
    public ProcessResult process( TaskContext context ) throws Exception {
        OmsLogger omsLogger = context.getOmsLogger();

        // root task 负责分发任务
        if (isRootTask()) {
            // 从控制台传递分片参数，假设格式为KV：1=a&2=b&3=c
            String jobParams = context.getJobParams();
            Map<String, String> paramsMap =
                    Splitter.on("&").withKeyValueSeparator("=").split(jobParams);

            List<SubTask> subTasks = Lists.newLinkedList();
            paramsMap.forEach(( k, v ) -> subTasks.add(new SubTask(Integer.parseInt(k), v)));
            map(subTasks, "SLICE_TASK");
            return new ProcessResult(true, "map successfully");
        }

        Object subTask = context.getSubTask();
        if (subTask instanceof SubTask) {
            // 实际处理
            // 当然，如果觉得 subTask 还是很大，也可以继续分发哦

            return new ProcessResult(true, "subTask:" + ( (SubTask) subTask ).getIndex() + " process successfully");
        }
        return new ProcessResult(false, "UNKNOWN BUG");
    }

    @Override
    public ProcessResult reduce( TaskContext context, List<TaskResult> taskResults ) {
        // 按需求做一些统计工作... 不需要的话，直接使用 Map 处理器即可
        return new ProcessResult(true, "xxxx");
    }

    /**
     * SubTask
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    private static class SubTask {

        private int index;
        private String params;

        public SubTask( int index, String params ) {
            this.index = index;
            this.params = params;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex( int index ) {
            this.index = index;
        }

        public String getParams() {
            return params;
        }

        public void setParams( String params ) {
            this.params = params;
        }
    }
}
