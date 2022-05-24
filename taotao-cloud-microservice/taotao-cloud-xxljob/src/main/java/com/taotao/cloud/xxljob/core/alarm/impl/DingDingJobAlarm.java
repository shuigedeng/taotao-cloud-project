package com.taotao.cloud.xxljob.core.alarm.impl;

import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
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
		XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao()
			.load(Integer.valueOf(info.getJobGroup()));

		StringBuilder str = new StringBuilder();
		str.append("taotao-cloud-xxljob监控告警明细： \n");
		str.append("[时间戳]: ").append(DateUtil.format(LocalDateTime.now(), DateUtil.DEFAULT_DATE_TIME_FORMAT)).append("\n");
		str.append("["+I18nUtil.getString("jobinfo_field_jobgroup")+"] : ").append(group != null ? group.getTitle() : "null").append("\n");
		str.append("["+I18nUtil.getString("jobinfo_field_id")+"] : ").append(info.getId()).append("\n");
		str.append("["+I18nUtil.getString("jobinfo_field_jobdesc")+"] : ").append(info.getJobDesc()).append("\n");
		str.append("["+I18nUtil.getString("jobconf_monitor_alarm_title")+"] : ").append(I18nUtil.getString("jobconf_monitor_alarm_type")).append("\n");
		str.append("["+I18nUtil.getString("jobconf_monitor_alarm_content")+"] : ").append(alarmContent).append("\n");

		try {
			sender.send(MessageSubType.TEXT,
				DingerRequest.request(str.toString()));
		} catch (Exception e) {
			LogUtil.error(e);
			alarmResult = false;
		}

		return alarmResult;
	}
}
