//package com.taotao.cloud.health.warn;
//
//
//import com.taotao.cloud.common.utils.ContextUtil;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.common.utils.ReflectionUtil;
//import com.taotao.cloud.common.utils.StringUtil;
//import com.taotao.cloud.core.utils.RequestUtil;
//import com.taotao.cloud.health.base.Message;
//import com.taotao.cloud.health.properties.WarnProperties;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class FlyBookWarn extends AbstractWarn {
//
//	boolean driverExsit = false;
//
//	public FlyBookWarn() {
//		driverExsit =
//			ReflectionUtil.tryClassForName("com.yh.csx.bsf.message.flybook.FlyBookProvider")
//				!= null;
//	}
//
//	@Override
//	public void notify(Message message) {
//		if (!driverExsit) {
//			LogUtil.error("health", "未找到FlyBookProvider",
//				new Exception("不支持飞书预警"));
//			return;
//		}
//
//		FlyBookProvider flyBookProvider = ContextUtil.getBean(FlyBookProvider.class, false);
//		if (flyBookProvider != null) {
//			String ip = RequestUtil.getIpAddress();
//			if (!StringUtil.isEmpty(ip) && !WarnProperties.Default()
//				.getBsfHealthWarnFlybookFilterIP().contains(ip)) {
//				List<String> tokens = new ArrayList<>();
//				tokens.addAll(Arrays.asList(
//					WarnProperties.Default().getBsfHealthWarnFlybookSystemAccessToken()
//						.split(",")));
//				tokens.addAll(Arrays.asList(
//					WarnProperties.Default().getBsfHealthWarnFlybookProjectAccessToken()
//						.split(",")));
//				flyBookProvider.sendText(tokens.toArray(new String[tokens.size()]),
//					StringUtil.subString3(message.getTitle(), 100),
//					StringUtil.subString3(message.getTitle(), 100) + "\n" +
//						"详情:" + RequestUtil.getBaseUrl() + "/bsf/health/\n" +
//						"【" + message.getWarnType().getDescription() + "】" + StringUtil.subString3(
//						message.getContent(), 500));
//			}
//		}
//	}
//}
