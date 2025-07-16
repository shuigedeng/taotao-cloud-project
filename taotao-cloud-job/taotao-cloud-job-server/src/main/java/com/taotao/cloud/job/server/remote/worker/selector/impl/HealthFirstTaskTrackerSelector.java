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

package com.taotao.cloud.job.server.remote.worker.selector.impl;

import com.google.common.collect.Lists;
import com.taotao.cloud.job.common.enums.DispatchStrategy;
import com.taotao.cloud.job.server.common.module.WorkerInfo;
import com.taotao.cloud.job.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import com.taotao.cloud.job.server.remote.worker.selector.TaskTrackerSelector;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * HealthFirst
 *
 * @author （疑似）新冠帕鲁
 * @since 2024/2/24
 */
@Component
public class HealthFirstTaskTrackerSelector implements TaskTrackerSelector {

    @Override
    public DispatchStrategy strategy() {
        return DispatchStrategy.HEALTH_FIRST;
    }

    @Override
    public WorkerInfo select(
            JobInfo jobInfoDO, InstanceInfo instanceInfoDO, List<WorkerInfo> availableWorkers) {
        List<WorkerInfo> workers = Lists.newArrayList(availableWorkers);
        workers.sort(
                (o1, o2) ->
                        o2.getSystemMetrics().calculateScore()
                                - o1.getSystemMetrics().calculateScore());
        return workers.get(0);
    }
}
