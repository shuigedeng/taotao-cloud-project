package com.taotao.cloud.schedule.dynamicschedule.controller;

import com.example.dynamicschedule.base.ResultDTO;
import com.example.dynamicschedule.bean.ScheduleJob;
import com.example.dynamicschedule.service.ScheduleJobLogService;
import com.example.dynamicschedule.service.ScheduleJobService;
import com.example.dynamicschedule.utils.CronUtil;
import com.example.dynamicschedule.utils.DateUtil;
import com.example.dynamicschedule.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 定时任务controller
 */
@RestController
@RequestMapping("/job")
public class ScheduleJobController {


    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    private ScheduleJobLogService scheduleJobLogService;


    /**
     * 添加
     *
     * @param scheduleJob
     * @return
     */
    @PostMapping("/add")
    public ResultDTO add(ScheduleJob scheduleJob) {
        scheduleJobService.save(scheduleJob);
        return ResultUtils.getSuccess(null);
    }


    /**
     * 列表
     *
     * @param params
     * @return
     */
    @PostMapping("/list")
    public ResultDTO list(@RequestParam Map<String, Object> params) {
        return ResultUtils.getSuccess(scheduleJobService.queryPage(params));
    }

    /**
     * 列表
     *
     * @param params
     * @return
     */
    @PostMapping("/log/list")
    public ResultDTO logList(@RequestParam Map<String, Object> params) {
        return ResultUtils.getSuccess(scheduleJobLogService.queryPage(params));
    }


    /**
     * 更新
     *
     * @param scheduleJob
     * @return
     */

    @PostMapping("/update")
    public ResultDTO update(ScheduleJob scheduleJob) {
        scheduleJobService.update(scheduleJob);
        return ResultUtils.getSuccess(null);
    }

    /**
     * 更新
     *
     * @param scheduleJob
     * @return
     */

    @PostMapping("/saveOrUpdate")
    public ResultDTO saveOrUpdate(ScheduleJob scheduleJob) {
        if (Objects.nonNull(scheduleJob.getJobId())) {
            scheduleJobService.update(scheduleJob);
        } else {
            scheduleJobService.save(scheduleJob);
        }
        return ResultUtils.getSuccess(null);
    }

    /**
     * 批量更新
     *
     * @param ids
     * @param status
     * @return
     */

    @PostMapping("/updateBatch")
    public ResultDTO updateBatch(String ids, int status) {
        String[] idsString = ids.split(",");
        scheduleJobService.updateBatch(Stream.of(idsString).map(Long::parseLong).collect(Collectors.toList()), status);
        return ResultUtils.getSuccess(null);
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @PostMapping("/deleteBatch")
    public ResultDTO deleteBatch(String ids) {
        String[] idsString = ids.split(",");
        scheduleJobService.deleteBatch(Stream.of(idsString).map(Long::parseLong).collect(Collectors.toList()));
        return ResultUtils.getSuccess(null);
    }


    /**
     * 启动定时器
     *
     * @param ids
     * @return
     */
    @PostMapping("/run")
    public ResultDTO run(String ids) {
        String[] idsString = ids.split(",");
        scheduleJobService.run(Stream.of(idsString).map(Long::parseLong).collect(Collectors.toList()));
        return ResultUtils.getSuccess(null);
    }

    /**
     * 暂停定时器
     *
     * @param ids
     * @return
     */

    @PostMapping("/pause")
    public ResultDTO pause(String ids) {
        String[] idsString = ids.split(",");
        scheduleJobService.pause(Stream.of(idsString).map(Long::parseLong).collect(Collectors.toList()));
        return ResultUtils.getSuccess(null);
    }

    /**
     * 激活定时器
     *
     * @param ids
     * @return
     */

    @PostMapping("/resume")
    public ResultDTO resume(String ids) {
        String[] idsString = ids.split(",");
        scheduleJobService.resume(Stream.of(idsString).map(Long::parseLong).collect(Collectors.toList()));
        return ResultUtils.getSuccess(null);
    }


    @PostMapping("/preview")
    public ResultDTO preview(Long id, String cron, String dateStr) {
        ArrayList<Object> list = new ArrayList<>();
        if (StringUtils.isNotBlank(cron)) {
            String time = "";
            for (int i = 0; i < 10; i++) {
                if (StringUtils.isNotBlank(time)) {
                    dateStr = time;
                }
                time = addTimeList(dateStr, cron);
                list.add(time);
            }
        } else if (Objects.nonNull(id) && StringUtils.isBlank(cron)) {
            ScheduleJob scheduleJob = scheduleJobService.getScheduleJobByJobId(id);
            if (Objects.nonNull(scheduleJob)) {
                String cronExpression = scheduleJob.getCronExpression();
                String time = "";
                for (int i = 0; i < 10; i++) {
                    if (StringUtils.isNotBlank(time)) {
                        dateStr = time;
                    }
                    time = addTimeList(dateStr, cronExpression);
                    list.add(time);
                }
            }

        }
        return ResultUtils.getSuccess(list);

    }

    private String addTimeList(String dateStr, String cronExpression) {
        Date date = DateUtil.toDate(dateStr);
        Date next = CronUtil.next(cronExpression, date);
        String str = DateUtil.toStr(next);
        return str;

    }

}
