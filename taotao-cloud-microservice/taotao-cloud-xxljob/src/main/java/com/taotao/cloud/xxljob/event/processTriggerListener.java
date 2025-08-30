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

package com.taotao.cloud.xxljob.event;

import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.dingtalk.entity.DingerRequest;
import com.taotao.boot.dingtalk.enums.MessageSubType;
import com.taotao.boot.dingtalk.model.DingerSender;
import com.taotao.cloud.xxljob.model.XxlJobGroup;
import com.taotao.cloud.xxljob.model.XxlJobInfo;
import com.taotao.cloud.xxljob.model.XxlJobLog;
import com.taotao.cloud.xxljob.scheduler.conf.XxlJobAdminConfig;
import com.xxl.job.core.biz.model.ReturnT;
import jakarta.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 进程触发事件
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:21:32
 */
@Component
public class processTriggerListener {

	@Autowired
	private DingerSender dingerSender;

	@Autowired
	private MailProperties mailProperties;

	@Async
	@EventListener(ProcessTriggerEvent.class)
	public void processTriggerEventListener(ProcessTriggerEvent event) {
		XxlJobLog xxlJobLog = event.getXxlJobLog();
		XxlJobInfo jobInfo = event.getJobInfo();
		long time = event.getTime();

		sendDingDing(xxlJobLog, jobInfo, time);
		sendEmail(xxlJobLog, jobInfo, time);
	}

	private void sendDingDing(XxlJobLog jobLog, XxlJobInfo info, long time) {
		Map<String, Object> data = new HashMap<>();
		data.put("执行日志信息", jobLog);
		data.put("执行job信息", info);
		data.put("执行时间", time);

		String jsonData = JsonUtils.toJSONString(data);

		dingerSender.send(MessageSubType.TEXT, DingerRequest.request(jsonData));
	}

	private void sendEmail(XxlJobLog jobLog, XxlJobInfo info, long time) {
		// alarmContent
		String alarmContent = "Job LogId=" + jobLog.getId();
		alarmContent += "<br>TriggerMsg=<br>" + jobLog.getTriggerMsg();
		alarmContent += "<br>HandleCode=" + jobLog.getHandleMsg();

		XxlJobGroup group =
			XxlJobAdminConfig.getAdminConfig().getXxlJobGroupMapper().load(info.getJobGroup());
		String title = "xxljob执行信息监控";
		String content =
			MessageFormat.format(
				loadEmailJobAlarmTemplate(),
				group != null ? group.getTitle() : "null",
				info.getId(),
				info.getJobDesc(),
				time,
				jobLog.getTriggerCode() == ReturnT.SUCCESS_CODE ? "执行成功" : "执行失败",
				alarmContent);

		// make mail
		try {
			MimeMessage mimeMessage =
				XxlJobAdminConfig.getAdminConfig().getMailSender().createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(mailProperties.getUsername());
			helper.setTo("981376577@qq.com");
			helper.setSubject(title);
			helper.setText(content, true);

			XxlJobAdminConfig.getAdminConfig().getMailSender().send(mimeMessage);
		} catch (Exception e) {
			LogUtils.error(
				">>>>>>>>>>> xxl-job, job fail alarm email send error, JobLogId:{}",
				jobLog.getId(),
				e);
		}
	}

	/**
	 * load email job alarm template
	 */
	private static String loadEmailJobAlarmTemplate() {
		return "<h5>"
			+ "任务执行信息"
			+ "：</span>"
			+ "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n"
			+ "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >"
			+ "      <tr>\n"
			+ "         <td width=\"20%\" >"
			+ "执行器名称"
			+ "</td>\n"
			+ "         <td width=\"10%\" >"
			+ "任务ID"
			+ "</td>\n"
			+ "         <td width=\"10%\" >"
			+ "任务描述"
			+ "</td>\n"
			+ "         <td width=\"10%\" >"
			+ "执行时间(毫秒)"
			+ "</td>\n"
			+ "         <td width=\"10%\" >"
			+ "执行状态"
			+ "</td>\n"
			+ "         <td width=\"40%\" >"
			+ "执行详细信息"
			+ "</td>\n"
			+ "      </tr>\n"
			+ "   </thead>\n"
			+ "   <tbody>\n"
			+ "      <tr>\n"
			+ "         <td>{0}</td>\n"
			+ "         <td>{1}</td>\n"
			+ "         <td>{2}</td>\n"
			+ "         <td>{3}</td>\n"
			+ "         <td>{4}</td>\n"
			+ "         <td>{5}</td>\n"
			+ "      </tr>\n"
			+ "   </tbody>\n"
			+ "</table>";
	}
}
