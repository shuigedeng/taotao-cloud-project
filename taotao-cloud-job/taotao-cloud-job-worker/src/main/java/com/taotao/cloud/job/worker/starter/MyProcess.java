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

package com.taotao.cloud.job.worker.starter;

import com.taotao.cloud.job.worker.processor.ProcessResult;
import com.taotao.cloud.job.worker.processor.task.TaskContext;
import com.taotao.cloud.job.worker.processor.type.BasicProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "testProcessor")
public class MyProcess implements BasicProcessor {
    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        log.info("单机处理器正在处理");
        log.info(context.getJobParams());
        System.out.println("用户:" + context.getJobParams() + "该吃药啦！");
        boolean success = true;
        return new ProcessResult(success, context + ": " + success);
    }
}
