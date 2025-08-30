package com.taotao.cloud.xxljob.web;

import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PidConfig {
    
    @Bean
    public ApplicationListener pidFileWriter() {
        return new ApplicationPidFileWriter();
    }
}
