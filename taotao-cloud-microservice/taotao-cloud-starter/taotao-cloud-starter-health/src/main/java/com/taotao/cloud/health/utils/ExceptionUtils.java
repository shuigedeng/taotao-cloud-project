package com.taotao.cloud.health.utils;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.core.enums.ExceptionTypeEnum;
import com.taotao.cloud.core.http.DefaultHttpClient;
import com.taotao.cloud.core.http.HttpClient;
import com.taotao.cloud.core.monitor.MonitorThreadPool;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.utils.PropertyUtil;
import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.health.model.EnumWarnType;
import com.taotao.cloud.health.model.Level;
import com.taotao.cloud.health.model.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

public class ExceptionUtils {

	private final static String exceptionUrl = "taotao.cloud.health.report.exception.url";

	/**
	 * @描述 上报异常
	 * @参数 [message, applictionName]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/25
	 * @修改历史：
	 */
	public static void reportException(Message message, String applictionName) {
		if (message.getWarnType() == EnumWarnType.ERROR) {
			AtomicReference<String> title = new AtomicReference<>(message.getTitle());
			MonitorThreadPool monitorThreadPool = ContextUtil.getBean(MonitorThreadPool.class,
				false);
			if (Objects.nonNull(monitorThreadPool)) {
				monitorThreadPool.monitorSubmit("系统任务:异常上报", () -> {
					Map<String, Object> param = new HashMap();
					param.put("exceptionTitle", title.get());
					param.put("exceptionType", message.getExceptionType().getCode());
					param.put("exceptionLevel", message.getLevelType().getLevel());
					if (StringUtils.isNotBlank(message.getExceptionCode())) {
						param.put("exceptionCode", message.getExceptionCode());
					}
					if (StringUtils.isNotBlank(message.getBizScope())) {
						param.put("bizScope", message.getBizScope());
					}
					CoreProperties coreProperties = ContextUtil.getBean(CoreProperties.class, true);
					param.put("exceptionContent", String.format("[%s][%s][%s]%s",
						RequestUtil.getIpAddress(),
						PropertyUtil.getPropertyCache(coreProperties.getEnv().getName(), ""),
						StringUtil.nullToEmpty(
							PropertyUtil.getPropertyCache(CoreProperties.SpringApplicationName,
								"")),
						message.getContent()));
					if (StringUtils.isNotBlank(applictionName)) {
						param.put("projectBeName", applictionName);
					} else {
						param.put("projectBeName",
							PropertyUtil.getPropertyCache(CoreProperties.SpringApplicationName,
								StringUtils.EMPTY));
					}
					HttpClient.Params params = HttpClient.Params.custom()
						.setContentType(ContentType.APPLICATION_JSON).add(param).build();
					DefaultHttpClient defaultHttpClient = ContextUtil.getBean(
						DefaultHttpClient.class, false);
					if (Objects.nonNull(defaultHttpClient)) {
						defaultHttpClient.post(
							PropertyUtil.getPropertyCache(exceptionUrl, StringUtils.EMPTY), params);
					}
				});
			}
		}
	}

	/**
	 * @描述 上报异常
	 * @参数 [message]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/30
	 * @修改历史：
	 */
	public static void reportException(Message message) {
		reportException(message, null);
	}

	/**
	 * @描述 上报异常
	 * @参数 [levelType, title, content]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/25
	 * @修改历史：
	 */
	public static void reportException(Level levelType, String title, String content) {
		reportException(
			new Message(EnumWarnType.ERROR, title, content, levelType, ExceptionTypeEnum.BE, null,
				null), null);
	}

	/**
	 * @描述 上报异常
	 * @参数 [levelType, title, content, applictionName]
	 * @返回值 void
	 * @创建人 霍钧城
	 * @创建时间 2020/12/25
	 * @修改历史：
	 */
	public static void reportException(Level levelType, String title, String content,
		String applictionName) {
		reportException(
			new Message(EnumWarnType.ERROR, title, content, levelType, ExceptionTypeEnum.BE, null,
				null), applictionName);
	}
}

