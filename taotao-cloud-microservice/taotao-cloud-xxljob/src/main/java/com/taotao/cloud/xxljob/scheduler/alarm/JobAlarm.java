package com.taotao.cloud.xxljob.scheduler.alarm;

import com.taotao.cloud.xxljob.model.XxlJobInfo;
import com.taotao.cloud.xxljob.model.XxlJobLog;

/**
 * @author xuxueli 2020-01-19
 */
public interface JobAlarm {

    /**
     * job alarm
     *
     * @param info
     * @param jobLog
     * @return
     */
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog);

}
