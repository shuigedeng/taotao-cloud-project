package com.taotao.cloud.sys.biz.tools.quartz.dtos;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

import java.util.List;

public class TriggerTask {
    private TriggerKey triggerKey;
    private JobKey jobKey;
    private Long startTime;
    private Long prevFireTime;
    private Long nextFireTime;
    private String cron;
    // 最近的执行时间
    private List<String> nextTimes;

    public TriggerTask() {
    }

    public TriggerTask(TriggerKey triggerKey, JobKey jobKey, Long startTime, Long prevFireTime, Long nextFireTime) {
        this.triggerKey = triggerKey;
        this.jobKey = jobKey;
        this.startTime = startTime;
        this.prevFireTime = prevFireTime;
        this.nextFireTime = nextFireTime;
    }

	public TriggerKey getTriggerKey() {
		return triggerKey;
	}

	public void setTriggerKey(TriggerKey triggerKey) {
		this.triggerKey = triggerKey;
	}

	public JobKey getJobKey() {
		return jobKey;
	}

	public void setJobKey(JobKey jobKey) {
		this.jobKey = jobKey;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getPrevFireTime() {
		return prevFireTime;
	}

	public void setPrevFireTime(Long prevFireTime) {
		this.prevFireTime = prevFireTime;
	}

	public Long getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Long nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public List<String> getNextTimes() {
		return nextTimes;
	}

	public void setNextTimes(List<String> nextTimes) {
		this.nextTimes = nextTimes;
	}
}
