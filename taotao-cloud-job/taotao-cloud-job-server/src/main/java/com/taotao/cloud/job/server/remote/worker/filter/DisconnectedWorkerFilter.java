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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * filter disconnected worker
 *
 * @author shuigedeng
 * @since 2021/2/19
 */
@Slf4j
@Component
public class DisconnectedWorkerFilter implements WorkerFilter {

    @Override
    public boolean filter(WorkerInfo workerInfo, JobInfo jobInfo) {
        boolean timeout = workerInfo.timeout();
        if (timeout) {
            log.info(
                    "[Job-{}] filter worker[{}] due to timeout(lastActiveTime={})",
                    jobInfo.getId(),
                    workerInfo.getAddress(),
                    workerInfo.getLastActiveTime());
        }
        return timeout;
    }
}
