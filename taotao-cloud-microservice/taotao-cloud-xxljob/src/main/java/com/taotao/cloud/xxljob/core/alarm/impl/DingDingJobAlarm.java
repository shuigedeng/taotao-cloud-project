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

package com.taotao.cloud.xxljob.core.alarm.impl;

import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.dingtalk.entity.DingerRequest;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.model.DingerSender;
import com.taotao.cloud.xxljob.core.alarm.JobAlarm;
import com.taotao.cloud.xxljob.core.conf.XxlJobAdminConfig;
import com.taotao.cloud.xxljob.core.model.XxlJobGroup;
import com.taotao.cloud.xxljob.core.model.XxlJobInfo;
import com.taotao.cloud.xxljob.core.model.XxlJobLog;
import com.taotao.cloud.xxljob.core.util.I18nUtil;
import com.xxl.job.core.biz.model.ReturnT;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * job alarm by email
 *
 * @author xuxueli 2020-01-19
 */
@Component
public class DingDingJobAlarm implements JobAlarm {

    private static Logger logger = LoggerFactory.getLogger(DingDingJobAlarm.class);

    @Autowired
    private DingerSender sender;

    @Override
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog) {
        boolean alarmResult = true;

        // alarmContent
        String alarmContent = "Alarm Job LogId=" + jobLog.getId();
        if (jobLog.getTriggerCode() != ReturnT.SUCCESS_CODE) {
            alarmContent += "<br>TriggerMsg=<br>" + jobLog.getTriggerMsg();
        }
        if (jobLog.getHandleCode() > 0 && jobLog.getHandleCode() != ReturnT.SUCCESS_CODE) {
            alarmContent += "<br>HandleCode=" + jobLog.getHandleMsg();
        }

        // email info
        XxlJobGroup group =
                XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(info.getJobGroup());

        StringBuilder str = new StringBuilder();
        str.append("taotao-cloud-xxljob监控告警明细： \n");
        str.append("[时间戳]: ")
                .append(DateUtils.format(LocalDateTime.now(), DateUtils.DEFAULT_DATE_TIME_FORMAT))
                .append("\n");
        str.append("[" + I18nUtil.getString("jobinfo_field_jobgroup") + "] : ")
                .append(group != null ? group.getTitle() : "null")
                .append("\n");
        str.append("[" + I18nUtil.getString("jobinfo_field_id") + "] : ")
                .append(info.getId())
                .append("\n");
        str.append("[" + I18nUtil.getString("jobinfo_field_jobdesc") + "] : ")
                .append(info.getJobDesc())
                .append("\n");
        str.append("[" + I18nUtil.getString("jobconf_monitor_alarm_title") + "] : ")
                .append(I18nUtil.getString("jobconf_monitor_alarm_type"))
                .append("\n");
        str.append("[" + I18nUtil.getString("jobconf_monitor_alarm_content") + "] : ")
                .append(alarmContent)
                .append("\n");

        try {
            sender.send(MessageSubType.TEXT, DingerRequest.request(str.toString()));
        } catch (Exception e) {
            LogUtils.error(e);
            alarmResult = false;
        }

        return alarmResult;
    }
}
