/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.utils;

import com.taotao.boot.job.quartz.utils.CronUtils;
import com.taotao.boot.job.schedule.constant.CycleTypeConstant;
import com.taotao.cloud.sys.api.model.dto.ScheduledJobDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * JobUtils
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class JobUtils {

    /**
     * 将用户选择的时候转换未cron表达式
     *
     * @return str
     */
    public static String dateConvertToCron( ScheduledJobDTO param ) {
        // 秒 分 时 日 月 默认为'*'，周为'?'
        // 特殊处理，选择周 就不能拼接月、日；选择月就不能拼接周；选择日不能拼接周,并且默认每月
        // 周不能选择为每周

        String finalWeek = "?", finalMonth = "*", finalDay = "*", finalHour = "*", finalMinute = "*", finalSecods = "*";

        switch (param.getCycle()) {
            case CycleTypeConstant.WEEK -> {
                // 选择周 就不能拼接月、日
                // 周日为1，周六为7，需要将前端传递的数字加1处理
                String[] split = param.getWeek().split(",");
                List<String> newWeek = new ArrayList<>();
                for (String s : split) {
                    if ("7".equals(s)) {
                        newWeek.add("6");
                    } else {
                        newWeek.add(Integer.valueOf(s) + 1 + "");
                    }
                }
                // 判断类型
                finalWeek = String.join(",", newWeek);
                finalMonth = "*";
                finalDay = "?";
                finalHour = param.getHour();
                finalMinute = param.getMinute();
                finalSecods = param.getSecods();
            }
            case CycleTypeConstant.MONTH -> {
                finalMonth = param.getMonth();
                finalDay = param.getDay();
                finalHour = param.getHour();
                finalMinute = param.getMinute();
                finalSecods = param.getSecods();
            }
            case CycleTypeConstant.DAY -> {
                finalDay = param.getDay();
                finalHour = param.getHour();
                finalMinute = param.getMinute();
                finalSecods = param.getSecods();
            }
            case CycleTypeConstant.HOUR -> {
                finalHour = param.getHour();
                finalMinute = param.getMinute();
                finalSecods = param.getSecods();
            }
            case CycleTypeConstant.MINUTE -> {
                finalMinute = param.getMinute();
                finalSecods = param.getSecods();
            }
            case CycleTypeConstant.SECODS -> finalSecods = param.getSecods();
            default -> throw new RuntimeException("周期解析出错!");
        }
        // 已空格拼接
        String cron =
                finalSecods + " " + finalMinute + " " + finalHour + " " + finalDay + " " + finalMonth + " " + finalWeek;
        if (!CronUtils.isValid(cron)) {
            throw new RuntimeException("表达式解析出错!");
        }
        return cron;
    }
}
