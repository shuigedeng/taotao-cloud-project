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

package com.taotao.cloud.ai.alibaba.rag.rag_evaluation.config;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeChatProperties;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionProperties;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionUtils;
import com.alibaba.cloud.ai.autoconfigure.dashscope.ResolvedConnectionProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import io.micrometer.observation.ObservationRegistry;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.observation.ChatModelObservationConvention;
import org.springframework.ai.model.tool.DefaultToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionEligibilityPredicate;
import org.springframework.ai.model.tool.autoconfigure.ToolCallingAutoConfiguration;
import org.springframework.ai.retry.autoconfigure.SpringAiRetryAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author yingzi
 * @since 2025/6/11
 */
@ConditionalOnClass({DashScopeApi.class})
@AutoConfiguration(
        after = {
            RestClientAutoConfiguration.class,
            SpringAiRetryAutoConfiguration.class,
            ToolCallingAutoConfiguration.class
        })
@ImportAutoConfiguration(
        classes = {
            SpringAiRetryAutoConfiguration.class,
            RestClientAutoConfiguration.class,
            ToolCallingAutoConfiguration.class,
            WebClientAutoConfiguration.class
        })
@EnableConfigurationProperties({DashScopeConnectionProperties.class, DashScopeChatProperties.class})
public class ChatModelAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ChatModelAutoConfiguration.class);

    @Bean(name = "qwen-max")
    public DashScopeChatModel qwenMaxChatModel(
            RetryTemplate retryTemplate,
            ToolCallingManager toolCallingManager,
            DashScopeChatProperties chatProperties,
            ResponseErrorHandler responseErrorHandler,
            DashScopeConnectionProperties commonProperties,
            ObjectProvider<ObservationRegistry> observationRegistry,
            ObjectProvider<WebClient.Builder> webClientBuilderProvider,
            ObjectProvider<RestClient.Builder> restClientBuilderProvider,
            ObjectProvider<ChatModelObservationConvention> observationConvention,
            ObjectProvider<ToolExecutionEligibilityPredicate>
                    dashscopeToolExecutionEligibilityPredicate) {
        chatProperties.getOptions().setModel("qwen-max");
        DashScopeApi dashscopeApi =
                this.dashscopeChatApi(
                        commonProperties,
                        chatProperties,
                        (RestClient.Builder)
                                restClientBuilderProvider.getIfAvailable(RestClient::builder),
                        (WebClient.Builder)
                                webClientBuilderProvider.getIfAvailable(WebClient::builder),
                        responseErrorHandler,
                        "chat");
        DashScopeChatModel dashscopeModel =
                DashScopeChatModel.builder()
                        .dashScopeApi(dashscopeApi)
                        .retryTemplate(retryTemplate)
                        .toolCallingManager(toolCallingManager)
                        .defaultOptions(chatProperties.getOptions())
                        .observationRegistry(
                                (ObservationRegistry)
                                        observationRegistry.getIfUnique(
                                                () -> ObservationRegistry.NOOP))
                        .toolExecutionEligibilityPredicate(
                                (ToolExecutionEligibilityPredicate)
                                        dashscopeToolExecutionEligibilityPredicate.getIfUnique(
                                                DefaultToolExecutionEligibilityPredicate::new))
                        .build();
        Objects.requireNonNull(dashscopeModel);
        observationConvention.ifAvailable(dashscopeModel::setObservationConvention);

        logger.info("load qwenMaxChatModel success");
        return dashscopeModel;
    }

    private DashScopeApi dashscopeChatApi(
            DashScopeConnectionProperties commonProperties,
            DashScopeChatProperties chatProperties,
            RestClient.Builder restClientBuilder,
            WebClient.Builder webClientBuilder,
            ResponseErrorHandler responseErrorHandler,
            String modelType) {
        ResolvedConnectionProperties resolved =
                DashScopeConnectionUtils.resolveConnectionProperties(
                        commonProperties, chatProperties, modelType);
        return DashScopeApi.builder()
                .apiKey(resolved.apiKey())
                .headers(resolved.headers())
                .baseUrl(resolved.baseUrl())
                .webClientBuilder(webClientBuilder)
                .workSpaceId(resolved.workspaceId())
                .restClientBuilder(restClientBuilder)
                .responseErrorHandler(responseErrorHandler)
                .build();
    }

    @Bean(name = "qwen-plus")
    public DashScopeChatModel qwenPlusChatModel(
            RetryTemplate retryTemplate,
            ToolCallingManager toolCallingManager,
            DashScopeChatProperties chatProperties,
            ResponseErrorHandler responseErrorHandler,
            DashScopeConnectionProperties commonProperties,
            ObjectProvider<ObservationRegistry> observationRegistry,
            ObjectProvider<WebClient.Builder> webClientBuilderProvider,
            ObjectProvider<RestClient.Builder> restClientBuilderProvider,
            ObjectProvider<ChatModelObservationConvention> observationConvention,
            ObjectProvider<ToolExecutionEligibilityPredicate>
                    dashscopeToolExecutionEligibilityPredicate) {
        chatProperties.getOptions().setModel("qwen-plus");
        DashScopeApi dashscopeApi =
                this.dashscopeChatApi(
                        commonProperties,
                        chatProperties,
                        (RestClient.Builder)
                                restClientBuilderProvider.getIfAvailable(RestClient::builder),
                        (WebClient.Builder)
                                webClientBuilderProvider.getIfAvailable(WebClient::builder),
                        responseErrorHandler,
                        "chat");
        DashScopeChatModel dashscopeModel =
                DashScopeChatModel.builder()
                        .dashScopeApi(dashscopeApi)
                        .retryTemplate(retryTemplate)
                        .toolCallingManager(toolCallingManager)
                        .defaultOptions(chatProperties.getOptions())
                        .observationRegistry(
                                (ObservationRegistry)
                                        observationRegistry.getIfUnique(
                                                () -> ObservationRegistry.NOOP))
                        .toolExecutionEligibilityPredicate(
                                (ToolExecutionEligibilityPredicate)
                                        dashscopeToolExecutionEligibilityPredicate.getIfUnique(
                                                DefaultToolExecutionEligibilityPredicate::new))
                        .build();
        Objects.requireNonNull(dashscopeModel);
        observationConvention.ifAvailable(dashscopeModel::setObservationConvention);

        logger.info("load qwenPlusChatModel success");
        return dashscopeModel;
    }
}
