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

package com.taotao.cloud.sys.biz.job.powerjob.processors;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.List;
import org.springframework.stereotype.Component;
import tech.powerjob.common.utils.NetUtils;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.TaskResult;
import tech.powerjob.worker.core.processor.sdk.BroadcastProcessor;
import tech.powerjob.worker.log.OmsLogger;

/** 广播处理器 示例 */
@Component
public class BroadcastProcessorDemo implements BroadcastProcessor {

    @Override
    public ProcessResult preProcess(TaskContext context) throws Exception {
        LogUtils.info("===== BroadcastProcessorDemo#preProcess ======");
        context.getOmsLogger().info("BroadcastProcessorDemo#preProcess, current host: {}", NetUtils.getLocalHost());
        if ("rootFailed".equals(context.getJobParams())) {
            return new ProcessResult(false, "console need failed");
        } else {
            return new ProcessResult(true);
        }
    }

    @Override
    public ProcessResult process(TaskContext taskContext) throws Exception {
        OmsLogger logger = taskContext.getOmsLogger();
        LogUtils.info("===== BroadcastProcessorDemo#process ======");
        logger.info("BroadcastProcessorDemo#process, current host: {}", NetUtils.getLocalHost());
        long sleepTime = 1000;
        try {
            sleepTime = Long.parseLong(taskContext.getJobParams());
        } catch (Exception e) {
            logger.warn("[BroadcastProcessor] parse sleep time failed!", e);
        }
        Thread.sleep(Math.max(sleepTime, 1000));
        return new ProcessResult(true);
    }

    @Override
    public ProcessResult postProcess(TaskContext context, List<TaskResult> taskResults) throws Exception {
        LogUtils.info("===== BroadcastProcessorDemo#postProcess ======");
        context.getOmsLogger()
                .info(
                        "BroadcastProcessorDemo#postProcess, current host: {}, taskResult: {}",
                        NetUtils.getLocalHost(),
                        taskResults);
        return new ProcessResult(true, "success");
    }
}
