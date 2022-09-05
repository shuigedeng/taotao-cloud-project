package com.taotao.cloud.schedule.gloriaapi.annotation;

import com.gloria.schedule.config.ScheduleManagerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启定时任务管理
 * @author Carlos  carlos_love_gloria@163.com
 * @since 2022/3/28
 * @version 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ScheduleManagerConfig.class})
public @interface EnableScheduleManager {
}
