package com.taotao.cloud.schedule.gloriaapi.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("printTask")
@Slf4j
public class PrintTask implements ITask {


    @Override
    public void run(String params) {
        log.info("定时任务执行，参数为：{}", params);
    }

}
