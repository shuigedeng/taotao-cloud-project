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

package com.taotao.cloud.sys.biz.task.job.powerjob.processors.test;

import com.alibaba.fastjson2.JSONObject;
import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Component;
import tech.powerjob.official.processors.util.CommonUtils;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/** LogTestProcessor */
@Component
public class LogTestProcessor implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {

        final OmsLogger omsLogger = context.getOmsLogger();
        final String parseParams = CommonUtils.parseParams(context);
        final JSONObject config =
                Optional.ofNullable(JSONObject.parseObject(parseParams)).orElse(new JSONObject());

        final long loopTimes = Optional.ofNullable(config.getLong("loopTimes")).orElse(1000L);

        for (int i = 0; i < loopTimes; i++) {
            omsLogger.debug("[DEBUG] one DEBUG log in {}", new Date());
            omsLogger.info("[INFO] one INFO log in {}", new Date());
            omsLogger.warn("[WARN] one WARN log in {}", new Date());
            omsLogger.error("[ERROR] one ERROR log in {}", new Date());
        }

        return new ProcessResult(true);
    }
}
