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

package com.taotao.cloud.sys.biz.task.job.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.job.quartz.entity.QuartzTask;
import com.taotao.boot.job.quartz.enums.QuartzJobCode;
import com.taotao.boot.job.quartz.exception.QuartzExecutionException;
import com.taotao.boot.job.quartz.utils.QuartzManager;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.*;


import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务
 */
@Service
@AllArgsConstructor
public class QuartzJobServiceImpl extends ServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {

    private final QuartzJobMapper quartzJobMapper;
    private final QuartzManager quartzManager;

    /**
     * 启动项目时，初始化定时器
     */
    @Override
    public void init() throws SchedulerException {
        quartzManager.clear();

        List<QuartzJob> quartzJobList = quartzJobMapper.selectList(new LambdaQueryWrapper<>());
        for (QuartzJob quartzJobEntity : quartzJobList) {
            QuartzTask quartzJob = BeanUtil.copyProperties(quartzJobEntity, QuartzTask.class);
            quartzManager.addJob(quartzJob);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addJob(QuartzJobDTO quartzJobDTO) {
        QuartzJob quartzJobEntity = BeanUtil.copyProperties(quartzJobDTO, QuartzJob.class);
        quartzJobEntity.setState(QuartzJobCode.STOP);

        if (quartzJobMapper.insert(quartzJobEntity) > 0) {
            QuartzTask quartzJob = new QuartzTask();
            BeanUtil.copyProperties(quartzJobEntity, quartzJob);

            quartzManager.addJob(quartzJob);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(QuartzJobDTO quartzJobDTO) {
        QuartzJob quartzJobEntity = quartzJobMapper.selectById(quartzJobDTO.getId());
        BeanUtil.copyProperties(
                quartzJobDTO, quartzJobEntity, CopyOptions.of().ignoreNullValue());
        quartzJobMapper.updateById(quartzJobEntity);

        QuartzTask quartzJob = new QuartzTask();
        BeanUtil.copyProperties(quartzJobEntity, quartzJob);
        quartzManager.updateJob(quartzJob);

        // jobScheduler.delete(quartzJob.getId());
        // if (Objects.equals(quartzJob.getState(), QuartzJobCode.RUNNING)) {
        //	jobScheduler.add(quartzJob.getId(), quartzJob.getJobClassName(), quartzJob.getCron(),
        // quartzJob.getParameter());
        // }
    }

    @Override
    public void runOnce(Long id) {
        QuartzJob quartzJobEntity = quartzJobMapper.selectById(id);

        QuartzTask quartzJob = new QuartzTask();
        BeanUtil.copyProperties(quartzJobEntity, quartzJob);
        quartzManager.runJobNow(quartzJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void start(Long id) {
        QuartzJob quartzJobEntity = quartzJobMapper.selectById(id);

        // 非运行才进行操作
        if (!Objects.equals(quartzJobEntity.getState(), QuartzJobCode.RUNNING)) {
            quartzJobEntity.setState(QuartzJobCode.RUNNING);
            quartzJobMapper.updateById(quartzJobEntity);

            QuartzTask quartzJob = new QuartzTask();
            BeanUtil.copyProperties(quartzJobEntity, quartzJob);
            quartzManager.addJob(quartzJob);
            // jobScheduler.add(quartzJob.getId(), quartzJob.getJobClassName(), quartzJob.getCron(),
            // quartzJob.getParameter());
        } else {
            throw new QuartzExecutionException("已经是启动状态");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopJob(Long id) {
        QuartzJob quartzJobEntity = quartzJobMapper.selectById(id);
        if (!Objects.equals(quartzJobEntity.getState(), QuartzJobCode.STOP)) {
            quartzJobEntity.setState(QuartzJobCode.STOP);
            quartzJobMapper.updateById(quartzJobEntity);

            QuartzTask quartzJob = new QuartzTask();
            BeanUtil.copyProperties(quartzJobEntity, quartzJob);
            quartzManager.pauseJob(quartzJob);
            // jobScheduler.delete(id);
        } else {
            throw new QuartzExecutionException("已经是停止状态");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(Long id) {
        QuartzJob quartzJobEntity = quartzJobMapper.selectById(id);
        quartzJobMapper.deleteById(id);

        QuartzTask quartzJob = new QuartzTask();
        BeanUtil.copyProperties(quartzJobEntity, quartzJob);
        quartzManager.deleteJob(quartzJob);
    }

    @Override
    public void syncJobStatus() {
        LambdaQueryWrapper<QuartzJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuartzJob::getState, QuartzJobCode.RUNNING);
        List<QuartzJob> quartzJobs = quartzJobMapper.selectList(wrapper);
        Map<String, QuartzJob> quartzJobMap =
                quartzJobs.stream().collect(Collectors.toMap(o -> o.getId().toString(), o -> o));

        List<Trigger> triggers = quartzManager.findTriggers();

        // 将开始任务列表里没有的Trigger给删除. 将未启动的任务状态更新成停止
        for (Trigger trigger : triggers) {
            String triggerName = trigger.getKey().getName();
            if (!quartzJobMap.containsKey(triggerName)) {
                quartzManager.deleteTrigger(triggerName);
            } else {
                quartzJobMap.remove(triggerName);
            }
        }

        // 更新任务列表状态
        Collection<QuartzJob> quartzJobList = quartzJobMap.values();
        for (QuartzJob quartzJob : quartzJobList) {
            quartzJob.setState(QuartzJobCode.STOP);
        }
        if (CollUtil.isNotEmpty(quartzJobList)) {
            quartzJobList.forEach(quartzJobMapper::updateById);
        }
    }

    @Override
    public void startAllJobs() {
        quartzManager.startAllJobs();
    }

    @Override
    public void pauseAllJobs() {
        quartzManager.pauseAll();
    }

    @Override
    public void resumeAllJobs() {
        quartzManager.resumeAll();
    }

    @Override
    public void shutdownAllJobs() {
        quartzManager.shutdownAll();
    }

    @Override
    public QuartzJob findById(Long id) {
        return quartzJobMapper.selectById(id);
    }

    @Override
    public IPage<QuartzJob> page(QuartzJobPageQuery quartzJobPageQuery) {
        LambdaQueryWrapper<QuartzJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(QuartzJob::getId);
        return quartzJobMapper.selectPage(quartzJobPageQuery.buildMpPage(), wrapper);

    }

    @Override
    public String judgeJobClass(String jobClassName) {
        return quartzManager.getJobClass(jobClassName).getName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addQuartzJobDTOTestSeata(QuartzJobDTO quartzJobDTO) {
        QuartzJob quartzJobEntity = BeanUtil.copyProperties(quartzJobDTO, QuartzJob.class);
        quartzJobEntity.setState(QuartzJobCode.STOP);

        quartzJobMapper.insert(quartzJobEntity);
        return true;

    }
}
