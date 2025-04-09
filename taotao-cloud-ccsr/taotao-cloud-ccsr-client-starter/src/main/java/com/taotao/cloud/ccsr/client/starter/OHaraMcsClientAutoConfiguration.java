package com.taotao.cloud.ccsr.client.starter;

import org.ohara.msc.common.config.OHaraMcsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(EnableOHaraMcsCondition.class) // 核心控制
public class OHaraMcsClientAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "ohara-mcs")
    public OHaraMcsConfig oHaraMcsConfig() {
        return new OHaraMcsConfig();
    }

}
