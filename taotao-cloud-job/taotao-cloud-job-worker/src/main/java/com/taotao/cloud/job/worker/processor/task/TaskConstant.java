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

package com.taotao.cloud.job.worker.processor.task;

/**
 * task 常量
 *
 * @author shuigedeng
 * @since 2020/3/17
 */
public class TaskConstant {

    private TaskConstant() {}

    /**
     * 所有根任务的名称
     */
    public static final String ROOT_TASK_NAME = "OMS_ROOT_TASK";

    /**
     * 广播执行任务的名称
     */
    public static final String BROADCAST_TASK_NAME = "OMS_BROADCAST_TASK";

    /**
     * 终极任务的名称（MapReduce的reduceTask和Broadcast的postProcess会有该任务）
     */
    public static final String LAST_TASK_NAME = "OMS_LAST_TASK";
}
