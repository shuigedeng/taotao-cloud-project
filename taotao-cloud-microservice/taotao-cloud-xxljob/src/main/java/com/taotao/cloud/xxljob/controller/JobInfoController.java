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

package com.taotao.cloud.xxljob.controller;

import com.taotao.cloud.xxljob.controller.interceptor.PermissionInterceptor;
import com.taotao.cloud.xxljob.core.exception.XxlJobException;
import com.taotao.cloud.xxljob.core.model.XxlJobGroup;
import com.taotao.cloud.xxljob.core.model.XxlJobInfo;
import com.taotao.cloud.xxljob.core.model.XxlJobUser;
import com.taotao.cloud.xxljob.core.route.ExecutorRouteStrategyEnum;
import com.taotao.cloud.xxljob.core.scheduler.MisfireStrategyEnum;
import com.taotao.cloud.xxljob.core.scheduler.ScheduleTypeEnum;
import com.taotao.cloud.xxljob.core.thread.JobScheduleHelper;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import com.taotao.cloud.xxljob.dao.XxlJobGroupDao;
import com.taotao.cloud.xxljob.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {
    private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);

    @Resource private XxlJobGroupDao xxlJobGroupDao;
    @Resource private XxlJobService xxlJobService;

    @RequestMapping
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "jobGroup", required = false, defaultValue = "-1") int jobGroup) {

        // 枚举-字典
        model.addAttribute(
                "ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values()); // 路由策略-列表
        model.addAttribute("GlueTypeEnum", GlueTypeEnum.values()); // Glue类型-字典
        model.addAttribute(
                "ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values()); // 阻塞处理策略-字典
        model.addAttribute("ScheduleTypeEnum", ScheduleTypeEnum.values()); // 调度类型
        model.addAttribute("MisfireStrategyEnum", MisfireStrategyEnum.values()); // 调度过期策略

        // 执行器列表
        List<XxlJobGroup> jobGroupList_all = xxlJobGroupDao.findAll();

        // filter group
        List<XxlJobGroup> jobGroupList =
                PermissionInterceptor.filterJobGroupByRole(request, jobGroupList_all);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
        }

        model.addAttribute("JobGroupList", jobGroupList);
        model.addAttribute("jobGroup", jobGroup);

        return "jobinfo/jobinfo.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
            @RequestParam(value = "length", required = false, defaultValue = "10") int length,
            @RequestParam("jobGroup") int jobGroup,
            @RequestParam("triggerStatus") int triggerStatus,
            @RequestParam("jobDesc") String jobDesc,
            @RequestParam("executorHandler") String executorHandler,
            @RequestParam("author") String author) {

        return xxlJobService.pageList(
                start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
    }

    @RequestMapping("/add")
    @ResponseBody
    public ReturnT<String> add(HttpServletRequest request, XxlJobInfo jobInfo) {
        // valid permission
        PermissionInterceptor.validJobGroupPermission(request, jobInfo.getJobGroup());

        // opt
        XxlJobUser loginUser = PermissionInterceptor.getLoginUser(request);
        return xxlJobService.add(jobInfo, loginUser);
    }

    @RequestMapping("/update")
    @ResponseBody
    public ReturnT<String> update(HttpServletRequest request, XxlJobInfo jobInfo) {
        // valid permission
        PermissionInterceptor.validJobGroupPermission(request, jobInfo.getJobGroup());

        // opt
        XxlJobUser loginUser = PermissionInterceptor.getLoginUser(request);
        return xxlJobService.update(jobInfo, loginUser);
    }

    @RequestMapping("/remove")
    @ResponseBody
    public ReturnT<String> remove(@RequestParam("id") int id) {
        return xxlJobService.remove(id);
    }

    @RequestMapping("/stop")
    @ResponseBody
    public ReturnT<String> pause(@RequestParam("id") int id) {
        return xxlJobService.stop(id);
    }

    @RequestMapping("/start")
    @ResponseBody
    public ReturnT<String> start(@RequestParam("id") int id) {
        return xxlJobService.start(id);
    }

    @RequestMapping("/trigger")
    @ResponseBody
    public ReturnT<String> triggerJob(
            HttpServletRequest request,
            @RequestParam("id") int id,
            @RequestParam("executorParam") String executorParam,
            @RequestParam("addressList") String addressList) {

        // login user
        XxlJobUser loginUser = PermissionInterceptor.getLoginUser(request);
        // trigger
        return xxlJobService.trigger(loginUser, id, executorParam, addressList);
    }

    @RequestMapping("/nextTriggerTime")
    @ResponseBody
    public ReturnT<List<String>> nextTriggerTime(
            @RequestParam("scheduleType") String scheduleType,
            @RequestParam("scheduleConf") String scheduleConf) {

        XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
        paramXxlJobInfo.setScheduleType(scheduleType);
        paramXxlJobInfo.setScheduleConf(scheduleConf);

        List<String> result = new ArrayList<>();
        try {
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(
                    "nextTriggerTime error. scheduleType = {}, scheduleConf= {}",
                    scheduleType,
                    scheduleConf,
                    e);
            return new ReturnT<List<String>>(
                    ReturnT.FAIL_CODE,
                    (I18nUtil.getString("schedule_type") + I18nUtil.getString("system_unvalid"))
                            + e.getMessage());
        }
        return new ReturnT<List<String>>(result);
    }
}
