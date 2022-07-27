package com.taotao.cloud.common.support.cron.parser;


import com.taotao.cloud.common.support.cron.pojo.TimeOfDay;
import java.util.Date;
import java.util.List;

/**
 * CRON表达式解析
 */
public interface CronParser {
    /**
     * 某个时刻的下一个时刻
     * @param date 给定时刻
     * @return 下一个执行时刻
     */
    Date next(Date date);

    /**
     * 计算一天中的哪些时刻[时分秒]执行
     * @param date 给定日期
     * @return 哪些时刻[时分秒]
     */
    List<TimeOfDay> timesOfDay(Date date);
}
