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

package com.taotao.cloud.mq.common.retry.core.model;

import com.taotao.cloud.mq.common.retry.api.model.AttemptTime;
import java.util.Date;

/**
 * 尝试执行的时候消耗时间
 * @author shuigedeng
 * @since 0.0.1
 */
public class DefaultAttemptTime implements AttemptTime {

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 消耗的时间
     */
    private long costTimeInMills;

    @Override
    public Date startTime() {
        return startTime;
    }

    public DefaultAttemptTime startTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public Date endTime() {
        return endTime;
    }

    public DefaultAttemptTime endTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public long costTimeInMills() {
        return costTimeInMills;
    }

    public DefaultAttemptTime costTimeInMills(long costTimeInMills) {
        this.costTimeInMills = costTimeInMills;
        return this;
    }
}
