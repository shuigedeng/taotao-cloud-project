package com.taotao.cloud.schedule.gloriaapi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务配置类
 * @author Carlos  carlos_love_gloria@163.com
 * @since 2022/3/28
 * @version 1.0.0
 */
@Configuration
@ComponentScan("com.gloria.schedule")
@MapperScan("com.gloria.schedule.dao")
public class ScheduleManagerConfig {

}
