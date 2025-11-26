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

package com.taotao.cloud.sys.biz.task.job.powerjob.tester;

import com.alibaba.fastjson2.JSONObject;
import com.taotao.boot.common.utils.log.LogUtils;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/** 测试 Oms 在线日志的性能 */
@Component
public class OmsLogPerformanceTester implements BasicProcessor {

    private static final int BATCH = 1000;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {

        OmsLogger omsLogger = context.getOmsLogger();
        // 控制台参数，格式为 {"num":10000, "interval": 200}
        JSONObject jobParams = JSONObject.parseObject(context.getJobParams());
        Long num = jobParams.getLong("num");
        Long interval = jobParams.getLong("interval");

        omsLogger.info("ready to start to process, current JobParams is {}.", jobParams);

        RuntimeException re = new RuntimeException("This is a exception~~~");

        long times = (long) Math.ceil(1.0 * num / BATCH);
        for (long i = 0; i < times; i++) {
            for (long j = 0; j < BATCH; j++) {
                long index = i * BATCH + j;
                LogUtils.info("send index: " + index);

                omsLogger.info("testing omsLogger's performance, current index is {}.", index);
            }
            omsLogger.error("Oh, it seems that we have got an exception.", re);
            try {
                Thread.sleep(interval);
            } catch (Exception ignore) {
            }
        }

        omsLogger.info("anyway, we finished the job~configuration~");
        return new ProcessResult(true, "good job");
    }
}
