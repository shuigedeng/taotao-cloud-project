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

package com.taotao.cloud.sys.biz.job.powerjob.tester;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.Map;
import org.springframework.stereotype.Component;
import tech.powerjob.common.WorkflowContextConstant;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/** 测试追加工作流上下文数据 */
@Component
public class AppendWorkflowContextTester implements BasicProcessor {

    private static final String FAIL_CODE = "0";

    @Override
    @SuppressWarnings("squid:S106")
    public ProcessResult process(TaskContext context) throws Exception {

        Map<String, String> workflowContext = context.getWorkflowContext().fetchWorkflowContext();
        String originValue = workflowContext.get(WorkflowContextConstant.CONTEXT_INIT_PARAMS_KEY);
        LogUtils.info("======= AppendWorkflowContextTester#start =======");
        LogUtils.info("current instance id : " + context.getInstanceId());
        LogUtils.info("current workflow context : " + workflowContext);
        LogUtils.info("current job param : " + context.getJobParams());
        LogUtils.info("initParam of workflow context : " + originValue);
        int num = 0;
        try {
            num = Integer.parseInt(originValue);
        } catch (Exception e) {
            // ignore
        }
        context.getWorkflowContext().appendData2WfContext(WorkflowContextConstant.CONTEXT_INIT_PARAMS_KEY, num + 1);
        LogUtils.info("======= AppendWorkflowContextTester#end =======");
        if (FAIL_CODE.equals(context.getJobParams())) {
            return new ProcessResult(false, "Failed!");
        }
        return new ProcessResult(true, "Success!");
    }
}
