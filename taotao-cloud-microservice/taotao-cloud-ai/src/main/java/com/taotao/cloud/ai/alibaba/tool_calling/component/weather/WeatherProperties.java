package com.taotao.cloud.ai.alibaba.tool_calling.component.weather;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yingzi
 * @date 2025/3/25:15:24
 */
@ConfigurationProperties(prefix = "spring.ai.toolcalling.weather")
public class WeatherProperties {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
