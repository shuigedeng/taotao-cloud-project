package com.taotao.cloud.xxljob.scheduler.type.strategy;

import com.taotao.cloud.xxljob.model.XxlJobInfo;
import com.taotao.cloud.xxljob.scheduler.cron.CronExpression;
import com.taotao.cloud.xxljob.scheduler.type.ScheduleType;

import java.util.Date;

public class CronScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(XxlJobInfo jobInfo, Date fromTime) throws Exception {
        // generate next trigger time, with cron
        return new CronExpression(jobInfo.getScheduleConf()).getNextValidTimeAfter(fromTime);
    }

}
