package com.taotao.cloud.health.warn;


import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.model.Message;
import com.taotao.cloud.health.properties.WarnProperties;
import com.taotao.cloud.sms.service.SmsService;

public class SmsWarn extends AbstractWarn {

	private WarnProperties warnProperties;
	private SmsService smsService;

	public SmsWarn(WarnProperties warnProperties, SmsService smsService) {
		this.warnProperties = warnProperties;
		this.smsService = smsService;
	}

	@Override
	public void notify(Message message) {
		if (this.smsService != null) {
			String ip = RequestUtil.getIpAddress();

			String dingDingFilterIP = this.warnProperties.getDingdingFilterIP();
			if (!StringUtil.isEmpty(ip) && !dingDingFilterIP.contains(ip)) {
				String context = StringUtil.subString3(message.getTitle(), 100) + "\n" +
					"详情: " + RequestUtil.getBaseUrl() + "/taotao/cloud/health/\n" +
					StringUtil.subString3(message.getContent(), 500);

				try {
					smsService.sendSms("", "", "", "");
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}
	}
}
