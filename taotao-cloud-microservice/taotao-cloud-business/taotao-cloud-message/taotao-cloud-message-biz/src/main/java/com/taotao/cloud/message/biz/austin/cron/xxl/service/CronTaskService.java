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

package com.taotao.cloud.message.biz.austin.cron.xxl.service;

import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import com.taotao.cloud.message.biz.austin.cron.xxl.entity.XxlJobGroup;
import com.taotao.cloud.message.biz.austin.cron.xxl.entity.XxlJobInfo;

/**
 * 定时任务服务
 *
 * @author 3y
 */
public interface CronTaskService {

    /**
     * 新增/修改 定时任务
     *
     * @param xxlJobInfo
     * @return 新增时返回任务Id，修改时无返回
     */
    BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo);

    /**
     * 删除定时任务
     *
     * @param taskId
     * @return BasicResultVO
     */
    BasicResultVO deleteCronTask(Integer taskId);

    /**
     * 启动定时任务
     *
     * @param taskId
     * @return BasicResultVO
     */
    BasicResultVO startCronTask(Integer taskId);

    /**
     * 暂停定时任务
     *
     * @param taskId
     * @return BasicResultVO
     */
    BasicResultVO stopCronTask(Integer taskId);

    /**
     * 得到执行器Id
     *
     * @param appName
     * @param title
     * @return BasicResultVO
     */
    BasicResultVO getGroupId(String appName, String title);

    /**
     * 创建执行器
     *
     * @param xxlJobGroup
     * @return BasicResultVO
     */
    BasicResultVO createGroup(XxlJobGroup xxlJobGroup);
}
