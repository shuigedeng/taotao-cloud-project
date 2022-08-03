package com.taotao.cloud.sys.biz.api.controller.tools.quartz.dtos;

import lombok.Data;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import java.util.ArrayList;
import java.util.List;

@Data
public class TriggerTask {
    private TriggerKey triggerKey;
    private JobKey jobKey;
    private Long startTime;
    private Long prevFireTime;
    private Long nextFireTime;
    private String cron;
    /**
     * 最近的执行时间列表
     */
    private List<String> nextTimes = new ArrayList<>();

    public TriggerTask() {
    }

    public TriggerTask(TriggerKey triggerKey, JobKey jobKey, Long startTime, Long prevFireTime, Long nextFireTime) {
        this.triggerKey = triggerKey;
        this.jobKey = jobKey;
        this.startTime = startTime;
        this.prevFireTime = prevFireTime;
        this.nextFireTime = nextFireTime;
    }
}
