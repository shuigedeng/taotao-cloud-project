/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.config;

import com.taotao.cloud.system.biz.entity.QuartzJob;
import com.taotao.cloud.system.biz.service.QuartzJobService;
import com.taotao.cloud.system.biz.utils.QuartzManage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobRunner implements ApplicationRunner {

    private final QuartzJobService quartzJobService;

    private final QuartzManage quartzManage;

    public JobRunner(QuartzJobService quartzJobService, QuartzManage quartzManage) {
        this.quartzJobService = quartzJobService;
        this.quartzManage = quartzManage;
    }

    /**
     * 项目启动时重新激活启用的定时任务
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        System.out.println("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobService.findByIsPauseIsFalse();
        quartzJobs.forEach(quartzManage::addJob);
        System.out.println("--------------------定时任务注入完成---------------------");
    }
}
