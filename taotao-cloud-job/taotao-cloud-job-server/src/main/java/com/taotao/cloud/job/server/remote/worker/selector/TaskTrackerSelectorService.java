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

package com.taotao.cloud.job.server.remote.worker.selector;

import com.google.common.collect.Maps;
import com.taotao.cloud.job.server.common.module.WorkerInfo;
import com.taotao.cloud.job.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TaskTrackerSelectorService
 *
 * @author shuigedeng
 * @since 2024/2/24
 */
@Service
public class TaskTrackerSelectorService {

    private final Map<Integer, TaskTrackerSelector> taskTrackerSelectorMap = Maps.newHashMap();

    @Autowired
    public TaskTrackerSelectorService(List<TaskTrackerSelector> taskTrackerSelectors) {
        taskTrackerSelectors.forEach(ts -> taskTrackerSelectorMap.put(ts.strategy().getV(), ts));
    }

    public WorkerInfo select(
            JobInfo jobInfoDO, InstanceInfo instanceInfoDO, List<WorkerInfo> availableWorkers) {
        TaskTrackerSelector taskTrackerSelector =
                taskTrackerSelectorMap.get(jobInfoDO.getDispatchStrategy());
        return taskTrackerSelector.select(jobInfoDO, instanceInfoDO, availableWorkers);
    }
}
