package com.taotao.cloud.xxljob.core.alarm.impl;

import com.taotao.cloud.common.utils.common.JsonUtil;
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
import java.text.MessageFormat;
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

		//StringBuilder str = new StringBuilder();
		//str.append("taotaocloud微服务监控 \n");
		//str.append("[时间戳]: ")
		//	.append(DateUtil.format(LocalDateTime.now(), DateUtil.DEFAULT_DATE_TIME_FORMAT))
		//	.append("\n");
		//str.append("[服务名] : ").append(serviceName).append("\n");
		//str.append("[服务ip]: ").append(serviceUrl).append("\n");
		//str.append("[服务详情]: ").append(JsonUtil.toJSONString(details));

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
		String personal = I18nUtil.getString("admin_name_full");
		String title = I18nUtil.getString("jobconf_monitor");
		String content = MessageFormat.format(loadEmailJobAlarmTemplate(),
			group != null ? group.getTitle() : "null",
			info.getId(),
			info.getJobDesc(),
			alarmContent);

		try {
			sender.send(MessageSubType.TEXT,
				DingerRequest.request(content));
		} catch (Exception e) {
			LogUtil.error(e);
			alarmResult = false;
		}

		return alarmResult;
	}

	/**
	 * load email job alarm template
	 *
	 * @return
	 */
	private static final String loadEmailJobAlarmTemplate() {
		String mailBodyTemplate =
			"<h5>" + I18nUtil.getString("jobconf_monitor_detail") + "：</span>" +
				"<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n"
				+
				"   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >"
				+
				"      <tr>\n" +
				"         <td width=\"20%\" >" + I18nUtil.getString("jobinfo_field_jobgroup")
				+ "</td>\n" +
				"         <td width=\"10%\" >" + I18nUtil.getString("jobinfo_field_id") + "</td>\n"
				+
				"         <td width=\"20%\" >" + I18nUtil.getString("jobinfo_field_jobdesc")
				+ "</td>\n" +
				"         <td width=\"10%\" >" + I18nUtil.getString("jobconf_monitor_alarm_title")
				+ "</td>\n" +
				"         <td width=\"40%\" >" + I18nUtil.getString("jobconf_monitor_alarm_content")
				+ "</td>\n" +
				"      </tr>\n" +
				"   </thead>\n" +
				"   <tbody>\n" +
				"      <tr>\n" +
				"         <td>{0}</td>\n" +
				"         <td>{1}</td>\n" +
				"         <td>{2}</td>\n" +
				"         <td>" + I18nUtil.getString("jobconf_monitor_alarm_type") + "</td>\n" +
				"         <td>{3}</td>\n" +
				"      </tr>\n" +
				"   </tbody>\n" +
				"</table>";

		return mailBodyTemplate;
	}

}
