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
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/** 测试用户反馈的无法停止实例的问题 https://github.com/PowerJob/PowerJob/issues/37 */
@Component
public class StopInstanceTester implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        int i = 0;
        while (true) {
            LogUtils.info(i++);
            Thread.sleep(1000 * 10);
        }
    }
}
