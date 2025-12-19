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

package com.taotao.cloud.sys.biz.task.job.schedule.task;

import com.taotao.boot.common.utils.log.LogUtils;

import java.util.Random;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * TaskBusinessService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Service("taskBusinessService")
public class TaskBusinessService {

    private final Logger logger = LoggerFactory.getLogger(TaskBusinessService.class);

    /**
     * 为了记录异常 所有的方法必须在try内 每个方法默认接收一个taskId
     */
    // @Async
    @SchedulerLock(name = "taskBusinessService_taskA", lockAtLeastFor = "PT30S", lockAtMostFor = "PT10M")
    public void taskA( String id ) {
        try {
            logger.info("======执行业务代码 --- this is A ======");
            // 模拟时长
            int number = new Random().nextInt(100) + 1;
            // 模拟耗时
            Thread.sleep(2000);

            logger.info("======执行业务代码 --- this is 完成 ======");
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }
}
