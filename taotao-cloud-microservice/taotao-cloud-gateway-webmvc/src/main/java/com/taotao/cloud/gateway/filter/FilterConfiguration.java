package com.taotao.cloud.gateway.filter;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Mr han
 * @date 2025/12/17
 * @description 注册自定义的过滤器，用于配置在application.yml文件中，目前使用此配置方式无实际效果 TODO
*/
@Configuration
@Profile("fileconfig")
public class FilterConfiguration {
    private static final Logger log = LoggerFactory.getLogger(FilterConfiguration.class);

    @PostConstruct
    public void init(){
        log.error("========开始加载FilterConfiguration配置============");
    }
    @Bean
    public CustomFilterSupplier customFilterSupplier() {
        return new CustomFilterSupplier();
    }
}

