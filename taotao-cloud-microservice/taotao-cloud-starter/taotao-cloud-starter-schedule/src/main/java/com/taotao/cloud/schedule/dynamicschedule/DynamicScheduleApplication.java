package com.taotao.cloud.schedule.dynamicschedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.dynamicschedule.dao")
public class DynamicScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicScheduleApplication.class, args);
    }

}

