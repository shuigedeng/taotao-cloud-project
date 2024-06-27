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

package com.taotao.cloud.sys.biz.job.schedule.runner;

import com.taotao.cloud.job.schedule.task.TaskManager;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** 启动完成后加载 */
@Component
public class ScheduleApplicationRunner implements ApplicationRunner {

    @Resource
    private TaskManager taskManager;

    private final Logger logger = LoggerFactory.getLogger(ScheduleApplicationRunner.class);

    @Override
    public void run(ApplicationArguments args) {
        logger.info("==== 系统运行开始 ====");

        // 初始化任务
        //taskManager.initTask();
    }
}
