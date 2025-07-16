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

package com.taotao.cloud.xxljob.event;

import com.taotao.cloud.xxljob.core.model.XxlJobGroup;
import com.taotao.cloud.xxljob.core.model.XxlJobInfo;
import com.taotao.cloud.xxljob.core.model.XxlJobLog;
import org.springframework.context.ApplicationEvent;

/**
 * 进程触发事件
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 20:24:42
 */
public class ProcessTriggerEvent extends ApplicationEvent {

    private final XxlJobLog xxlJobLog;
    private final XxlJobGroup group;
    private final XxlJobInfo jobInfo;

    private final long time;

    public ProcessTriggerEvent(
            XxlJobGroup group, XxlJobInfo jobInfo, XxlJobLog xxlJobLog, long time) {
        super(xxlJobLog);

        this.xxlJobLog = xxlJobLog;
        this.group = group;
        this.jobInfo = jobInfo;

        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public XxlJobLog getXxlJobLog() {
        return xxlJobLog;
    }

    public XxlJobGroup getGroup() {
        return group;
    }

    public XxlJobInfo getJobInfo() {
        return jobInfo;
    }
}
