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

package com.taotao.cloud.sys.biz.task.job.powerjob.processors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;
import tech.powerjob.common.serialize.JsonUtils;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.TaskResult;
import tech.powerjob.worker.core.processor.sdk.MapReduceProcessor;
import tech.powerjob.worker.log.OmsLogger;

/**
 * MapReduce 处理器示例 控制台参数：{"batchSize": 100, "batchNum": 2}
 */
@Component
public class MapReduceProcessorDemo implements MapReduceProcessor {

    @Override
    public ProcessResult process( TaskContext context ) throws Exception {

        OmsLogger omsLogger = context.getOmsLogger();

        LogUtils.info("============== TestMapReduceProcessor#process ==============");
        LogUtils.info("isRootTask:" + isRootTask());
        LogUtils.info("taskContext:" + JsonUtils.toJSONString(context));

        // 根据控制台参数获取MR批次及子任务大小
        final JSONObject jobParams = JSONObject.parseObject(context.getJobParams());

        Integer batchSize = (Integer) jobParams.getOrDefault("batchSize", 100);
        Integer batchNum = (Integer) jobParams.getOrDefault("batchNum", 10);

        if (isRootTask()) {
            LogUtils.info("==== MAP ====");
            omsLogger.info("[DemoMRProcessor] start root task~");
            List<TestSubTask> subTasks = Lists.newLinkedList();
            for (int j = 0; j < batchNum; j++) {
                for (int i = 0; i < batchSize; i++) {
                    int x = j * batchSize + i;
                    subTasks.add(new TestSubTask("name" + x, x));
                }
                map(subTasks, "MAP_TEST_TASK");
                subTasks.clear();
            }
            omsLogger.info("[DemoMRProcessor] map success~");
            return new ProcessResult(true, "MAP_SUCCESS");
        } else {
            LogUtils.info("==== NORMAL_PROCESS ====");
            omsLogger.info("[DemoMRProcessor] process subTask: {}.", JSON.toJSONString(context.getSubTask()));
            LogUtils.info("subTask: " + JsonUtils.toJSONString(context.getSubTask()));
            Thread.sleep(1000);
            if (context.getCurrentRetryTimes() == 0) {
                return new ProcessResult(false, "FIRST_FAILED");
            } else {
                return new ProcessResult(true, "PROCESS_SUCCESS");
            }
        }
    }

    @Override
    public ProcessResult reduce( TaskContext context, List<TaskResult> taskResults ) {
        log.info("================ MapReduceProcessorDemo#reduce ================");
        log.info("TaskContext: {}", JSONObject.toJSONString(context));
        log.info("List<TaskResult>: {}", JSONObject.toJSONString(taskResults));
        context.getOmsLogger().info("MapReduce job finished, result is {}.", taskResults);

        boolean success = ThreadLocalRandom.current().nextBoolean();
        return new ProcessResult(success, context + ": " + success);
    }

    /**
     * TestSubTask
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    public static class TestSubTask {

        private String name;
        private int age;

        public TestSubTask() {
        }

        public TestSubTask( String name, int age ) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName( String name ) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge( int age ) {
            this.age = age;
        }
    }
}
