package com.taotao.cloud.logger.logRecord.configuration;

import com.taotao.cloud.logger.logRecord.function.CustomFunctionRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFunctionConfiguration {

    @Bean
    public CustomFunctionRegistrar registrar() {
        return new CustomFunctionRegistrar();
    }
}
