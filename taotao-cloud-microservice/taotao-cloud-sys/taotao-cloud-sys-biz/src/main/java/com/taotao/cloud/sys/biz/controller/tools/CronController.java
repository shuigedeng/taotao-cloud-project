package com.taotao.cloud.sys.biz.controller.tools;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Validated
public class CronController {

    /**
     * 小工具 , 计算 cron 表达式下次执行时间
     * @param expression cron 表达式
     * @return 计算的后面所有执行时间
     */
    @PostMapping("/cron/nextExecutionTime")
    public List<String> cronNextExecutionTime(@NotNull String expression){
        List<String> nextTimes = new ArrayList<>();
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(expression);
        Date current = new Date();
        for (int i = 0; i < 10; i++) {
            current = cronSequenceGenerator.next(current);
            nextTimes.add(DateFormatUtils.format(current,"yyyy-MM-dd HH:mm:ss"));
        }

        return nextTimes;
    }
}
