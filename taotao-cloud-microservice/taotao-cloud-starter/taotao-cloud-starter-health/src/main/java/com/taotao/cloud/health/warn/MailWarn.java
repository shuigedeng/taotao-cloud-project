package com.taotao.cloud.health.warn;


import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.mail.template.MailTemplate;

public class MailWarn extends AbstractWarn {

	private MailTemplate mailTemplate;
	private WarnProperties warnProperties;

	public MailWarn(WarnProperties warnProperties, MailTemplate mailTemplate) {
		this.mailTemplate = mailTemplate;
		this.warnProperties = warnProperties;
	}

	@Override
	public void notify(Message message) {
		if (this.mailTemplate != null) {
			String ip = RequestUtil.getIpAddress();

			String dingDingFilterIP = this.warnProperties.getDingdingFilterIP();
			if (!StringUtil.isEmpty(ip) && !dingDingFilterIP.contains(ip)) {
				String context = StringUtil.subString3(message.getTitle(), 100) + "\n" +
					"详情: " + RequestUtil.getBaseUrl() + "/taotao/cloud/health/\n" +
					StringUtil.subString3(message.getContent(), 500);

				try {
					mailTemplate.sendSimpleMail("981376577@qq.com", "服务状态预警", context);
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}
	}
}
