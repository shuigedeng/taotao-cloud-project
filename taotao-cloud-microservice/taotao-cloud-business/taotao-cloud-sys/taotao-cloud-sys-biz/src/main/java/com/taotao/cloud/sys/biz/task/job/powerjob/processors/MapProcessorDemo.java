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

import com.google.common.collect.Lists;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.job.biz.powerjob.MysteryService;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.common.serialize.JsonUtils;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.MapProcessor;

/**
 * Map处理器 示例
 */
@Component
public class MapProcessorDemo implements MapProcessor {

    @Autowired
    private MysteryService mysteryService;

    /**
     * 每一批发送任务大小
     */
    private static final int BATCH_SIZE = 100;
    /**
     * 发送的批次
     */
    private static final int BATCH_NUM = 5;

    @Override
    public ProcessResult process( TaskContext context ) throws Exception {

        LogUtils.info("============== MapProcessorDemo#process ==============");
        LogUtils.info("isRootTask:" + isRootTask());
        LogUtils.info("taskContext:" + JsonUtils.toJSONString(context));
        LogUtils.info(mysteryService.hasaki());

        if (isRootTask()) {
            LogUtils.info("==== MAP ====");
            List<SubTask> subTasks = Lists.newLinkedList();
            for (int j = 0; j < BATCH_NUM; j++) {
                SubTask subTask = new SubTask();
                subTask.siteId = j;
                subTask.itemIds = Lists.newLinkedList();
                subTasks.add(subTask);
                for (int i = 0; i < BATCH_SIZE; i++) {
                    subTask.itemIds.add(i + j * 100);
                }
            }
            map(subTasks, "MAP_TEST_TASK");
            return new ProcessResult(true, "map successfully");
        } else {

            LogUtils.info("==== PROCESS ====");
            SubTask subTask = (SubTask) context.getSubTask();
            for (Integer itemId : subTask.getItemIds()) {
                if (Thread.interrupted()) {
                    // 任务被中断
                    LogUtils.info(
                            "job has been stop! so stop to process subTask:" + subTask.getSiteId() + "=>" + itemId);
                    break;
                }
                LogUtils.info("processing subTask: " + subTask.getSiteId() + "=>" + itemId);
                int max = Integer.MAX_VALUE >> 4;
                for (int i = 0; ; i++) {
                    // 模拟耗时操作
                    if (i > max) {
                        break;
                    }
                }
            }
            // 测试在 Map 任务中追加上下文
            context.getWorkflowContext().appendData2WfContext("Yasuo", "A sword's poor company for a long road.");
            boolean b = ThreadLocalRandom.current().nextBoolean();
            return new ProcessResult(b, "RESULT:" + b);
        }
    }

    /**
     * SubTask
     *
     * @author shuigedeng
     * @version 2026.02
     * @since 2025-12-19 09:30:45
     */
    public static class SubTask {

        private Integer siteId;
        private List<Integer> itemIds;

        public SubTask() {
        }

        public SubTask( Integer siteId, List<Integer> itemIds ) {
            this.siteId = siteId;
            this.itemIds = itemIds;
        }

        public Integer getSiteId() {
            return siteId;
        }

        public void setSiteId( Integer siteId ) {
            this.siteId = siteId;
        }

        public List<Integer> getItemIds() {
            return itemIds;
        }

        public void setItemIds( List<Integer> itemIds ) {
            this.itemIds = itemIds;
        }
    }
}
