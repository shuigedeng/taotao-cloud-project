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

package com.taotao.cloud.ai.alibaba.tool_calling.component.weather.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author yingzi
 * @date 2025/3/25:15:24
 */
public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private static final String WEATHER_API_URL = "https://api.weatherapi.com/v1/forecast.json";

    private final WebClient webClient;

    private final JsonMapper jsonMapper = new JsonMapper();

    public WeatherService(WeatherProperties properties) {
        this.webClient =
                WebClient.builder()
                        .defaultHeader(
                                HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                        .defaultHeader("key", properties.getApiKey())
                        .build();
    }

    public static Response fromJson(Map<String, Object> json) {
        Map<String, Object> location = (Map<String, Object>) json.get("location");
        Map<String, Object> current = (Map<String, Object>) json.get("current");
        Map<String, Object> forecast = (Map<String, Object>) json.get("forecast");
        List<Map<String, Object>> forecastDays =
                (List<Map<String, Object>>) forecast.get("forecastday");
        String city = (String) location.get("name");
        return new Response(city, current, forecastDays);
    }

    @Override
    public Response apply(Request request) {
        if (request == null || !StringUtils.hasText(request.city())) {
            logger.error("Invalid request: city is required.");
            return null;
        }
        String location = WeatherUtils.preprocessLocation(request.city());
        String url =
                UriComponentsBuilder.fromHttpUrl(WEATHER_API_URL)
                        .queryParam("q", location)
                        .queryParam("days", request.days())
                        .toUriString();
        logger.info("url : {}", url);
        try {
            Mono<String> responseMono =
                    webClient.get().uri(url).retrieve().bodyToMono(String.class);
            String jsonResponse = responseMono.block();
            assert jsonResponse != null;

            Response response =
                    fromJson(
                            jsonMapper.readValue(
                                    jsonResponse, new TypeReference<Map<String, Object>>() {}));
            logger.info("Weather data fetched successfully for city: {}", response.city());
            return response;
        } catch (Exception e) {
            logger.error("Failed to fetch weather data: {}", e.getMessage());
            return null;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonClassDescription("Weather Service API request")
    public record Request(
            @JsonProperty(required = true, value = "city") @JsonPropertyDescription("city name")
                    String city,
            @JsonProperty(required = true, value = "days")
                    @JsonPropertyDescription(
                            "Number of days of weather forecast. Value ranges from 1 to 14")
                    int days) {}

    public record Response(
            String city, Map<String, Object> current, List<Map<String, Object>> forecastDays) {}
}
