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
package com.taotao.cloud.logger.logRecord.configuration;

import com.taotao.cloud.logger.logRecord.constants.LogConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Optional;


/**
 * 流发送器配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:44:31
 */
@Configuration
@ConditionalOnProperty(name = "log-record.data-pipeline", havingValue = LogConstants.DataPipeline.STREAM)
@EnableConfigurationProperties({LogRecordProperties.class})
@ConditionalOnClass(BindingServiceConfiguration.class)
@AutoConfigureBefore({BindingServiceConfiguration.class})
@EnableBinding(StreamSenderConfiguration.LogRecordChannel.class)
public class StreamSenderConfiguration {

	/**
	 * 应用程序名称
	 */
	@Value("${spring.application.name:}")
    private String applicationName;

	/**
	 * 当前文件
	 */
	@Value("${spring.profiles.active:}")
    private String activeProfile;

	/**
	 * 属性
	 */
	private final LogRecordProperties properties;

	/**
	 * 绑定
	 */
	private final BindingServiceProperties bindings;

	/**
	 * 流发送器配置
	 *
	 * @param bindings            绑定
	 * @param logRecordProperties 日志记录属性
	 * @return
	 * @since 2022-04-26 14:44:31
	 */
	public StreamSenderConfiguration(BindingServiceProperties bindings, LogRecordProperties logRecordProperties) {
        this.properties = logRecordProperties;
        this.bindings = bindings;
    }

	/**
	 * 初始化
	 *
	 * @since 2022-04-26 14:44:31
	 */
	@PostConstruct
    public void init() {
        BindingProperties inputBinding = this.bindings.getBindings().get(LogRecordChannel.OUTPUT);
        if (inputBinding == null) {
            this.bindings.getBindings().put(LogRecordChannel.OUTPUT, new BindingProperties());
        }

        BindingProperties input = this.bindings.getBindings().get(LogRecordChannel.OUTPUT);
        if (input.getDestination() == null || input.getDestination().equals(LogRecordChannel.OUTPUT)) {
            input.setDestination(Optional.ofNullable(properties.getStream().getDestination()).orElse("stream_logging_" + applicationName + "_" + activeProfile ));
        }
        if (!StringUtils.hasText(input.getGroup())) {
            input.setGroup(Optional.ofNullable(properties.getStream().getGroup()).orElse(applicationName));
        }

        if (StringUtils.hasText(properties.getStream().getBinder())) {
            input.setBinder(properties.getStream().getBinder());
        }

    }

	/**
	 * 日志记录频道
	 *
	 * @author shuigedeng
	 * @version 2022.04
	 * @since 2022-04-26 14:44:32
	 */
	public interface LogRecordChannel {

		/**
		 * 输出
		 */
		String OUTPUT = "LogRecordChannel";

		/**
		 * 日志输出
		 *
		 * @return {@link MessageChannel }
		 * @since 2022-04-26 14:44:32
		 */
		@Output(OUTPUT)
        MessageChannel messageLoggingQueueInput();

    }
}
