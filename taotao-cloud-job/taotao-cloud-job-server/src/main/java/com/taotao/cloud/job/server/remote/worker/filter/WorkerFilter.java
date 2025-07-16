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

package com.taotao.cloud.job.server.remote.worker.filter;

import com.taotao.cloud.job.server.common.module.WorkerInfo;
import com.taotao.cloud.job.server.persistence.domain.JobInfo;

/**
 * filter worker by system metrics or other info
 *
 * @author shuigedeng
 * @since 2021/2/16
 */
public interface WorkerFilter {

    /**
     * @param workerInfo worker info, maybe you need to use your customized info in SystemMetrics#extra
     * @param jobInfoDO  job info
     * @return true will remove the worker in process list
     */
    boolean filter(WorkerInfo workerInfo, JobInfo jobInfoDO);
}
