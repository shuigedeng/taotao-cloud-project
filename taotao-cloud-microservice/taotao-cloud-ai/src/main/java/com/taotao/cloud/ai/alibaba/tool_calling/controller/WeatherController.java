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

package com.taotao.cloud.ai.alibaba.tool_calling.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @date 2025/3/25:15:30
 */
@RestController
@RequestMapping("/chat/weather")
public class WeatherController {

    private final ChatClient chatClient;

    private final WeatherProperties weatherProperties;

    public WeatherController(
            ChatClient.Builder chatClientBuilder, WeatherProperties weatherProperties) {
        this.chatClient = chatClientBuilder.build();
        this.weatherProperties = weatherProperties;
    }

    /**
     * 无工具版
     */
    @GetMapping("/call")
    public String call(
            @RequestParam(value = "query", defaultValue = "请告诉我北京1天以后的天气") String query) {
        return chatClient.prompt(query).call().content();
    }

    /**
     * 调用工具版 - function
     */
    @GetMapping("/call/tool-function")
    public String callToolFunction(
            @RequestParam(value = "query", defaultValue = "请告诉我北京1天以后的天气") String query) {
        return chatClient.prompt(query).toolNames("getWeatherFunction").call().content();
    }

    /**
     * 调用工具版 - method
     */
    @GetMapping("/call/tool-method")
    public String callToolMethod(
            @RequestParam(value = "query", defaultValue = "请告诉我北京1天以后的天气") String query) {
        return chatClient.prompt(query).tools(new WeatherTools(weatherProperties)).call().content();
    }
}
