package com.taotao.cloud.xxljob.scheduler.type.strategy;

import com.taotao.cloud.xxljob.model.XxlJobInfo;
import com.taotao.cloud.xxljob.scheduler.type.ScheduleType;

import java.util.Date;

public class FixRateScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(XxlJobInfo jobInfo, Date fromTime) throws Exception {
        // generate next trigger time, fix rate delay
        return new Date(fromTime.getTime() + Long.parseLong(jobInfo.getScheduleConf()) * 1000L);
    }

}
