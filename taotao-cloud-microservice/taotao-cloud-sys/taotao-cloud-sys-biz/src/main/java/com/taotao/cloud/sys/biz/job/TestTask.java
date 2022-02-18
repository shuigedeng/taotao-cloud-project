package com.taotao.cloud.sys.biz.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestTask {
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "0 */1 * * * ?")
    public void robReceiveExpireTask() {
        System.out.println(df.format(LocalDateTime.now()) + "测试测试");
    }
}
