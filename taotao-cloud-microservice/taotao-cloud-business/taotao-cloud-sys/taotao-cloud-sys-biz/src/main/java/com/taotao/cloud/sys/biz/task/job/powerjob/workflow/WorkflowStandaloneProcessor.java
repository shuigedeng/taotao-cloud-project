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

package com.taotao.cloud.sys.biz.task.job.powerjob.workflow;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.log.LogUtils;
import java.util.Map;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/** 工作流测试 */
@Component
public class WorkflowStandaloneProcessor implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        OmsLogger logger = context.getOmsLogger();
        logger.info("current:" + context.getJobParams());
        LogUtils.info("jobParams: " + context.getJobParams());
        LogUtils.info("currentContext:" + JSON.toJSONString(context));

        // 尝试获取上游任务
        Map<String, String> workflowContext = context.getWorkflowContext().fetchWorkflowContext();
        LogUtils.info("工作流上下文数据：");
        LogUtils.info(workflowContext);

        return new ProcessResult(true, context.getJobId() + " process successfully.");
    }
}
