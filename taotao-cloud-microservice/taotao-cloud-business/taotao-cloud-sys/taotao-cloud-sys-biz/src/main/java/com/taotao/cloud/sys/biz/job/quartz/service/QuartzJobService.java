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

package com.taotao.cloud.sys.biz.job.quartz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.job.api.model.dto.QuartzJobDTO;
import com.taotao.cloud.job.api.model.page.QuartzJobPageQuery;
import com.taotao.cloud.job.biz.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

/**
 * 石英工作服务
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-09-06 09:02:48
 */
public interface QuartzJobService extends IService<QuartzJob> {

    void init() throws SchedulerException;

    /**
     * 添加任务
     *
     * @param quartzJobDTO 石英工作dto
     * @since 2022-09-06 09:02:48
     */
    void addJob(QuartzJobDTO quartzJobDTO);

    /**
     * 更新任务
     *
     * @param quartzJobDTO 石英工作dto
     * @since 2022-09-06 09:02:48
     */
    void updateJob(QuartzJobDTO quartzJobDTO);

    /**
     * 立即运行一次定时任务
     *
     * @param id id
     * @since 2022-09-06 09:02:48
     */
    void runOnce(Long id);

    /**
     * 开始任务
     *
     * @param id id
     * @since 2022-09-06 09:02:48
     */
    void start(Long id);

    /**
     * 停止任务
     *
     * @param id id
     * @since 2022-09-06 09:02:48
     */
    void stopJob(Long id);

    /**
     * 删除任务
     *
     * @param id
     */
    void deleteJob(Long id);

    /**
     * 同步状态
     *
     * @since 2022-09-06 09:02:59
     */
    void syncJobStatus();

    /**
     * 启动所有任务
     *
     * @since 2022-09-06 09:03:03
     */
    void startAllJobs();

    /**
     * 暂停所有任务
     *
     * @since 2022-09-06 09:03:05
     */
    void pauseAllJobs();

    /**
     * 恢复所有任务
     *
     * @since 2022-09-06 09:03:06
     */
    void resumeAllJobs();

    /**
     * 关闭所有任务
     *
     * @since 2022-09-06 09:03:08
     */
    void shutdownAllJobs();

    QuartzJob findById(Long id);

    IPage<QuartzJob> page(QuartzJobPageQuery quartzJobPageQuery);

    /** 判断是否是定时任务类 */
    String judgeJobClass(String jobClassName);

    boolean addQuartzJobDTOTestSeata(QuartzJobDTO quartzJobDTO);

}
