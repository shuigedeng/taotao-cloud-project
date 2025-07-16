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

package com.taotao.cloud.xxljob.admin.core.util;

import com.xxl.job.admin.core.cron.CronExpression;
import com.xxl.job.core.util.DateUtil;
import java.text.ParseException;
import java.util.Date;
import org.junit.jupiter.api.Test;

public class CronExpressionTest {

    @Test
    public void shouldWriteValueAsString() throws ParseException {
        CronExpression cronExpression = new CronExpression("0 0 0 ? * 1");
        Date lastTriggerTime = new Date();
        for (int i = 0; i < 5; i++) {
            Date nextTriggerTime = cronExpression.getNextValidTimeAfter(lastTriggerTime);
            System.out.println(DateUtil.formatDateTime(nextTriggerTime));

            lastTriggerTime = nextTriggerTime;
        }
    }
}
