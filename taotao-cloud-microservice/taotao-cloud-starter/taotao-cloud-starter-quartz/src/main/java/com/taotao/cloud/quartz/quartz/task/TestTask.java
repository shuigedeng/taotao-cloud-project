package com.taotao.cloud.quartz.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务
 *
 * @author 阿沐 babamu@126.com
 */
@Slf4j
@Component
public class TestTask {

    public void run(String params) throws InterruptedException {
        log.info("我是testTask.run()，参数：{}，正在被执行。", params);
        Thread.sleep(1000);
    }
}
