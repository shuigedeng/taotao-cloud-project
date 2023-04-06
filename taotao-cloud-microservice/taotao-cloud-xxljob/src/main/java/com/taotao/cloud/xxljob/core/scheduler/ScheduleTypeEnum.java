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

package com.taotao.cloud.xxljob.core.scheduler;

import com.taotao.cloud.xxljob.core.util.I18nUtil;

/**
 * @author xuxueli 2020-10-29 21:11:23
 */
public enum ScheduleTypeEnum {
    NONE(I18nUtil.getString("schedule_type_none")),

    /**
     * schedule by cron
     */
    CRON(I18nUtil.getString("schedule_type_cron")),

    /**
     * schedule by fixed rate (in seconds)
     */
    FIX_RATE(I18nUtil.getString("schedule_type_fix_rate")),

/**
 * schedule by fix delay (in seconds)， after the last time
 */
/*FIX_DELAY(I18nUtil.getString("schedule_type_fix_delay"))*/ ;

    private String title;

    ScheduleTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static ScheduleTypeEnum match(String name, ScheduleTypeEnum defaultItem) {
        for (ScheduleTypeEnum item : ScheduleTypeEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }
}
