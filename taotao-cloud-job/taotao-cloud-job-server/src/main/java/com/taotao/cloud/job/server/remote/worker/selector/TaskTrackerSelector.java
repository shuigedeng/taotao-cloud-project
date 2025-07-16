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

import com.taotao.cloud.job.common.enums.DispatchStrategy;
import com.taotao.cloud.job.server.common.module.WorkerInfo;
import com.taotao.cloud.job.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;
import java.util.List;

/**
 * 主节点选择方式
 *
 * @author shuigedeng
 * @since 2024/2/24
 */
public interface TaskTrackerSelector {

    /**
     * 支持的策略
     *
     * @return 派发策略
     */
    DispatchStrategy strategy();

    /**
     * 选择主节点
     *
     * @param jobInfoDO        任务信息
     * @param instanceInfoDO   任务实例
     * @param availableWorkers 可用 workers
     * @return 主节点 worker
     */
    WorkerInfo select(
            JobInfo jobInfoDO, InstanceInfo instanceInfoDO, List<WorkerInfo> availableWorkers);
}
