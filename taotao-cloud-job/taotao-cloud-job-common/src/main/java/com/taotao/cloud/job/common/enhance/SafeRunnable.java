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

package com.taotao.cloud.job.common.enhance;

import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;

/**
 * 安全的 runnable，可防止因抛出异常导致周期性任务终止
 * 使用 {@link ScheduledExecutorService} 执行任务时，推荐继承此类捕获并打印异常，避免因为抛出异常导致周期性任务终止
 *
 * @author shuigedeng
 * @since 2023/9/20 15:52
 */
@Slf4j
public abstract class SafeRunnable implements Runnable {
    @Override
    public void run() {
        try {
            run0();
        } catch (Exception e) {
            log.error("[SafeRunnable] run failed", e);
        }
    }

    protected abstract void run0();
}
