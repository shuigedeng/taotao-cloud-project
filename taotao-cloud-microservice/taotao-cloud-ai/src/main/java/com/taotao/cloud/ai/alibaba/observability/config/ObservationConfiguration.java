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

package com.taotao.cloud.ai.alibaba.observability.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.observation.ChatClientObservationContext;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationContext;
import org.springframework.ai.observation.AiOperationMetadata;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.observation.ToolCallingObservationContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yingzi
 * @since 2025/6/12
 */
@Configuration
public class ObservationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ObservationConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(name = "observationRegistry")
    public ObservationRegistry observationRegistry(
            ObjectProvider<ObservationHandler<?>> observationHandlerObjectProvider) {
        ObservationRegistry observationRegistry = ObservationRegistry.create();
        ObservationRegistry.ObservationConfig observationConfig =
                observationRegistry.observationConfig();
        observationHandlerObjectProvider
                .orderedStream()
                .forEach(
                        handler -> {
                            Type[] genericInterfaces = handler.getClass().getGenericInterfaces();
                            for (Type type : genericInterfaces) {
                                if (type instanceof ParameterizedType parameterizedType
                                        && parameterizedType.getRawType() instanceof Class<?> clazz
                                        && ObservationHandler.class.isAssignableFrom(clazz)) {

                                    Type actualTypeArgument =
                                            parameterizedType.getActualTypeArguments()[0];
                                    logger.info(
                                            "load observation handler, supports context type: {}",
                                            actualTypeArgument);
                                }
                            }

                            // 将handler添加到observationRegistry中
                            observationConfig.observationHandler(handler);
                        });
        return observationRegistry;
    }

    /**
     * 监听chat client调用
     */
    @Bean
    ObservationHandler<ChatClientObservationContext>
            chatClientObservationContextObservationHandler() {
        logger.info("ChatClientObservation start");
        return new ObservationHandler<>() {

            @Override
            public boolean supportsContext(Observation.Context context) {
                return context instanceof ChatClientObservationContext;
            }

            @Override
            public void onStart(ChatClientObservationContext context) {
                ChatClientRequest request = context.getRequest();
                List<? extends Advisor> advisors = context.getAdvisors();
                boolean stream = context.isStream();
                logger.info(
                        "💬ChatClientObservation start: ChatClientRequest : {}, Advisors : {}, stream : {}",
                        request,
                        advisors,
                        stream);
            }

            @Override
            public void onStop(ChatClientObservationContext context) {
                ObservationHandler.super.onStop(context);
            }
        };
    }

    /**
     * 监听chat model调用
     */
    @Bean
    ObservationHandler<ChatModelObservationContext>
            chatModelObservationContextObservationHandler() {
        logger.info("ChatModelObservation start");
        return new ObservationHandler<>() {

            @Override
            public boolean supportsContext(Observation.Context context) {
                return context instanceof ChatModelObservationContext;
            }

            @Override
            public void onStart(ChatModelObservationContext context) {
                AiOperationMetadata operationMetadata = context.getOperationMetadata();
                Prompt request = context.getRequest();
                logger.info(
                        "🤖ChatModelObservation start: AiOperationMetadata : {}",
                        operationMetadata);
                logger.info("🤖ChatModelObservation start: Prompt : {}", request);
            }

            @Override
            public void onStop(ChatModelObservationContext context) {
                ChatResponse response = context.getResponse();
                logger.info("🤖ChatModelObservation start: ChatResponse : {}", response);
            }
        };
    }

    /**
     * 监听工具调用
     */
    @Bean
    public ObservationHandler<ToolCallingObservationContext>
            toolCallingObservationContextObservationHandler() {
        logger.info("ToolCallingObservation start");
        return new ObservationHandler<>() {
            @Override
            public boolean supportsContext(Observation.Context context) {
                return context instanceof ToolCallingObservationContext;
            }

            @Override
            public void onStart(ToolCallingObservationContext context) {
                ToolDefinition toolDefinition = context.getToolDefinition();
                logger.info(
                        "🔨ToolCalling start: {} - {}",
                        toolDefinition.name(),
                        context.getToolCallArguments());
            }

            @Override
            public void onStop(ToolCallingObservationContext context) {
                ToolDefinition toolDefinition = context.getToolDefinition();
                logger.info(
                        "✅ToolCalling done: {} - {}",
                        toolDefinition.name(),
                        context.getToolCallResult());
            }
        };
    }

    /**
     * 监听embedding model调用
     */
    @Bean
    public ObservationHandler<EmbeddingModelObservationContext>
            embeddingModelObservationContextObservationHandler() {
        logger.info("EmbeddingModelObservation start");
        return new ObservationHandler<>() {
            @Override
            public boolean supportsContext(Observation.Context context) {
                return context instanceof EmbeddingModelObservationContext;
            }

            @Override
            public void onStart(EmbeddingModelObservationContext context) {
                logger.info(
                        "📚EmbeddingModelObservation start: {} - {}",
                        context.getOperationMetadata().operationType(),
                        context.getOperationMetadata().provider());
            }
        };
    }
}
