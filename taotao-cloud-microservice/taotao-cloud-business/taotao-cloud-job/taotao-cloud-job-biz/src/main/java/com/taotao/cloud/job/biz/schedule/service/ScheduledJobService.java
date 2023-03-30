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

package com.taotao.cloud.job.biz.schedule.service;

import com.taotao.cloud.job.biz.schedule.entity.ScheduledJob;
import com.taotao.cloud.job.biz.schedule.entity.ScheduledJobLog;
import com.taotao.cloud.job.biz.schedule.model.TaskParam;
import com.taotao.cloud.job.biz.schedule.model.TaskVo;
import java.util.List;

public interface ScheduledJobService {

    /***
     *任务列表查询
     * @return o
     */
    List<ScheduledJob> taskList();

    /**
     * 新增任务
     *
     * @param param 新增参数
     * @return
     */
    Boolean addTask(TaskParam param);

    /**
     * 修改任务
     *
     * @param param 修改参数
     * @return
     */
    Boolean updateTask(TaskParam param);

    /**
     * 执行任务
     *
     * @param id 任务id
     * @return
     */
    Boolean invokeTask(String id);

    /**
     * 暂停任务
     *
     * @param id 任务id
     */
    Boolean stopTask(String id);

    /**
     * 删除任务
     *
     * @param id 任务id
     */
    Boolean deleteTask(String id);

    /**
     * 禁用任务
     *
     * @param id 任务id
     */
    Boolean forbidTask(String id);

    /**
     * 查询详情
     *
     * @param id 任务id
     */
    TaskVo getTaskById(String id);

    /** 任务日志 */
    void insertTaskLog(ScheduledJobLog log);
}
