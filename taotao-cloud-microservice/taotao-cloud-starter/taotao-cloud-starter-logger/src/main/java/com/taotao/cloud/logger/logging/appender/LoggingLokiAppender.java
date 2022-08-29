/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.logger.logging.appender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.CoreConstants;
import com.github.loki4j.logback.*;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.CharPool;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.logger.logging.config.MicaLoggingProperties;
import com.taotao.cloud.logger.logging.loki.Loki4jOkHttpSender;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

/**
 * loki 日志接收
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 15:43:13
 */
public class LoggingLokiAppender implements ILoggingAppender {
	private static final String APPENDER_NAME = "LOKI";
	private final MicaLoggingProperties properties;
	private final String appName;
	private final String profile;

	public LoggingLokiAppender(Environment environment,
							   MicaLoggingProperties properties) {
		this.properties = properties;
		// 1. 服务名和环境
		this.appName = environment.getRequiredProperty(CommonConstant.SPRING_APP_NAME_KEY);
		this.profile = environment.getRequiredProperty(CommonConstant.ACTIVE_PROFILES_PROPERTY);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		this.start(context);
	}

	@Override
	public void start(LoggerContext context) {
		LogUtils.info("Loki logging start.");
		reload(context);
	}

	@Override
	public void reset(LoggerContext context) {
		LogUtils.info("Loki logging reset.");
		reload(context);
	}

	private void reload(LoggerContext context) {
		MicaLoggingProperties.Loki loki = properties.getLoki();
		if (loki.isEnabled()) {
			addLokiAppender(context, loki);
		}
	}

	private void addLokiAppender(LoggerContext context, MicaLoggingProperties.Loki properties) {
		Loki4jAppender lokiAppender = new Loki4jAppender();
		lokiAppender.setName(APPENDER_NAME);
		lokiAppender.setContext(context);
		// 通用配置
		lokiAppender.setBatchMaxItems(properties.getBatchMaxItems());
		lokiAppender.setBatchMaxBytes(properties.getBatchMaxBytes());
		lokiAppender.setBatchTimeoutMs(properties.getBatchTimeoutMs());
		lokiAppender.setSendQueueMaxBytes(properties.getSendQueueMaxBytes());
		lokiAppender.setUseDirectBuffers(properties.isUseDirectBuffers());
		lokiAppender.setDrainOnStop(properties.isDrainOnStop());
		lokiAppender.setMetricsEnabled(properties.isMetricsEnabled());
		lokiAppender.setVerbose(properties.isVerbose());
		// format
		Loki4jEncoder loki4jEncoder = getFormat(context, properties);
		lokiAppender.setFormat(loki4jEncoder);
		// http
		lokiAppender.setHttp(getSender(properties));
		lokiAppender.start();
		// 先删除，再添加
		context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(APPENDER_NAME);
		context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(lokiAppender);
	}

	private Loki4jEncoder getFormat(LoggerContext context,
									MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.LokiEncoder encoder = properties.getEncoder();
		AbstractLoki4jEncoder loki4jEncoder = MicaLoggingProperties.LokiEncoder.ProtoBuf == encoder ?
			new ProtobufEncoder() : new JsonEncoder();
		// label config
		AbstractLoki4jEncoder.LabelCfg labelCfg = new AbstractLoki4jEncoder.LabelCfg();
		labelCfg.setPattern(formatLabelPatternHandle(context, properties));
		labelCfg.setPairSeparator(properties.getFormatLabelPairSeparator());
		labelCfg.setKeyValueSeparator(properties.getFormatLabelKeyValueSeparator());
		labelCfg.setNopex(properties.isFormatLabelNoPex());
		loki4jEncoder.setLabel(labelCfg);
		// message config
		AbstractLoki4jEncoder.MessageCfg messageCfg = new AbstractLoki4jEncoder.MessageCfg();
		String formatMessagePattern = properties.getFormatMessagePattern();
		if (StringUtils.isNotBlank(formatMessagePattern)) {
			messageCfg.setPattern(formatMessagePattern);
		}
		loki4jEncoder.setMessage(messageCfg);
		// 其他配置
		loki4jEncoder.setStaticLabels(properties.isFormatStaticLabels());
		loki4jEncoder.setSortByTime(properties.isFormatSortByTime());
		loki4jEncoder.setContext(context);
		loki4jEncoder.start();
		return loki4jEncoder;
	}

	private static HttpSender getSender(MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.HttpSender httpSenderType = getHttpSender(properties);
		AbstractHttpSender httpSender;
		if (MicaLoggingProperties.HttpSender.OKHttp == httpSenderType) {
			httpSender = new Loki4jOkHttpSender();
		} else if (MicaLoggingProperties.HttpSender.ApacheHttp == httpSenderType) {
			httpSender = new ApacheHttpSender();
		} else {
			httpSender = new JavaHttpSender();
		}
		httpSender.setUrl(properties.getHttpUrl());
		httpSender.setConnectionTimeoutMs(properties.getHttpConnectionTimeoutMs());
		httpSender.setRequestTimeoutMs(properties.getHttpRequestTimeoutMs());
		String authUsername = properties.getHttpAuthUsername();
		String authPassword = properties.getHttpAuthPassword();
		if (StringUtils.isNotBlank(authUsername) && StringUtils.isNotBlank(authPassword)) {
			AbstractHttpSender.BasicAuth basicAuth = new AbstractHttpSender.BasicAuth();
			basicAuth.setUsername(authUsername);
			basicAuth.setPassword(authPassword);
			httpSender.setAuth(basicAuth);
		}
		httpSender.setTenantId(properties.getHttpTenantId());
		return httpSender;
	}

	private String formatLabelPatternHandle(LoggerContext context, MicaLoggingProperties.Loki properties) {
		String labelPattern = properties.getFormatLabelPattern();
		Assert.hasText(labelPattern, "MicaLoggingProperties mica.logging.loki.format-label-pattern is blank.");
		String labelPatternExtend = properties.getFormatLabelPatternExtend();
		if (StringUtils.isNotBlank(labelPatternExtend)) {
			labelPattern = labelPattern + CharPool.COMMA + labelPatternExtend;
		}
		return labelPattern
			.replace("${appName}", appName)
			.replace("${profile}", profile)
			.replace("${HOSTNAME}", context.getProperty(CoreConstants.HOSTNAME_KEY));
	}

	private static MicaLoggingProperties.HttpSender getHttpSender(MicaLoggingProperties.Loki properties) {
		MicaLoggingProperties.HttpSender httpSenderProp = properties.getHttpSender();
		if (httpSenderProp != null && httpSenderProp.isAvailable()) {
			LogUtils.debug("mica logging use {} HttpSender", httpSenderProp);
			return httpSenderProp;
		}
		if (httpSenderProp == null) {
			MicaLoggingProperties.HttpSender[] httpSenders = MicaLoggingProperties.HttpSender.values();
			for (MicaLoggingProperties.HttpSender httpSender : httpSenders) {
				if (httpSender.isAvailable()) {
					LogUtils.debug("mica logging use {} HttpSender", httpSender);
					return httpSender;
				}
			}
			throw new IllegalArgumentException("Not java11 and no okHttp or apache http dependency.");
		}
		throw new NoClassDefFoundError(httpSenderProp.getSenderClass());
	}
}
