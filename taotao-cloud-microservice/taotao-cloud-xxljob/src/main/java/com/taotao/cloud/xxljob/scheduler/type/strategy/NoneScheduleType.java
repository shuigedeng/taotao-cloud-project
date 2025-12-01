package com.taotao.cloud.xxljob.scheduler.type.strategy;

import com.taotao.cloud.xxljob.model.XxlJobInfo;
import com.taotao.cloud.xxljob.scheduler.type.ScheduleType;

import java.util.Date;

public class NoneScheduleType extends ScheduleType {

    @Override
    public Date generateNextTriggerTime(XxlJobInfo jobInfo, Date fromTime) throws Exception {
        // generate none trigger-time
        return null;
    }

}
