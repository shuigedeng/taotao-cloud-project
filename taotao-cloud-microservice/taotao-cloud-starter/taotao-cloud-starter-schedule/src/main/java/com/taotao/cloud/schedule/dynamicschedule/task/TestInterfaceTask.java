package com.taotao.cloud.schedule.dynamicschedule.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("test2task")
@Slf4j
public class TestInterfaceTask {

    public void consoleLog(){
        log.info("通过测试接口 来控制定时任务");
    }
}
