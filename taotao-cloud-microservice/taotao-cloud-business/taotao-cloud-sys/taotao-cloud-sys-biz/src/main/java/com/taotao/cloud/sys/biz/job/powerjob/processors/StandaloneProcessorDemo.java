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
import java.util.Collections;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/** 单机处理器 示例 */
@Component
public class StandaloneProcessorDemo implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {

        OmsLogger omsLogger = context.getOmsLogger();
        omsLogger.info("StandaloneProcessorDemo start process,context is {}.", context);
        omsLogger.info("Notice! If you want this job process failed, your jobParams need to be 'failed'");

        omsLogger.info("Let's test the exception~");
        // 测试异常日志
        try {
            Collections.emptyList().add("277");
        } catch (Exception e) {
            omsLogger.error("oh~it seems that we have an exception~", e);
        }

        LogUtils.info("================ StandaloneProcessorDemo#process ================");
        LogUtils.info(context.getJobParams());
        // 根据控制台参数判断是否成功
        boolean success = !"failed".equals(context.getJobParams());
        omsLogger.info("StandaloneProcessorDemo finished process,success: .", success);

        omsLogger.info("anyway, we finished the job successfully~Congratulations!");
        return new ProcessResult(success, context + ": " + success);
    }
}
