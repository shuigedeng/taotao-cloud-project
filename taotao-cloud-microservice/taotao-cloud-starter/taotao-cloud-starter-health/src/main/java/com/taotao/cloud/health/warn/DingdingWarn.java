package com.taotao.cloud.health.warn;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ReflectionUtil;
import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.health.base.AbstractWarn;
import com.taotao.cloud.health.base.Message;
import com.taotao.cloud.health.base.dingding.DingdingProvider;
import com.taotao.cloud.health.config.WarnProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 20:13
 **/
public class DingdingWarn extends AbstractWarn {

	boolean driverExsit = false;

	public DingdingWarn() {
		driverExsit =
			ReflectionUtil.tryClassForName("com.yh.csx.bsf.message.dingding.DingdingProvider")
				!= null;
	}

	@Override
	public void notify(Message message) {
		if (!driverExsit) {
			LogUtil.error("health", "未找到DingdingProvider", new Exception("不支持钉钉预警"));
			return;
		}
		DingdingProvider dingdingProvider = ContextUtil.getBean(DingdingProvider.class, false);
		if (dingdingProvider != null) {
			String ip = RequestUtil.getIpAddress();
			if (!StringUtil.isEmpty(ip) && !WarnProperties.Default()
				.getBsfHealthWarnDingdingFilterIP().contains(ip)) {
				List<String> tokens = new ArrayList<>();
				tokens.addAll(Arrays.asList(
					WarnProperties.Default().getBsfHealthWarnDingdingSystemAccessToken()
						.split(",")));
				tokens.addAll(Arrays.asList(
					WarnProperties.Default().getBsfHealthWarnDingdingProjectAccessToken()
						.split(",")));
				dingdingProvider.sendText(tokens.toArray(new String[tokens.size()]),
					"【" + message.getWarnType().getDescription() + "】" + StringUtil.subString3(
						message.getTitle(), 100),
					StringUtil.subString3(message.getTitle(), 100) + "\n" +
						"详情:" + RequestUtil.getBaseUrl() + "/bsf/health/\n" +
						StringUtil.subString3(message.getContent(), 500));
			}
		}
	}
}
