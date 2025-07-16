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

package com.taotao.cloud.job.server.core.instance;

import com.taotao.cloud.job.common.enums.InstanceStatus;
import com.taotao.cloud.job.server.core.uid.IdGenerateService;
import com.taotao.cloud.job.server.persistence.domain.InstanceInfo;
import com.taotao.cloud.job.server.persistence.mapper.InstanceInfoMapper;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstanceService {
    private final InstanceInfoMapper instanceInfoMapper;
    private final IdGenerateService idGenerateService;

    public InstanceInfo create(
            Long jobId,
            String appName,
            String jobParams,
            String instanceParams,
            Long wfInstanceId,
            Long expectTriggerTime) {

        Long instanceId = idGenerateService.allocate();
        Date now = new Date();
        InstanceInfo newInstanceInfo = new InstanceInfo();
        newInstanceInfo.setJobId(jobId);
        newInstanceInfo.setAppName(appName);
        newInstanceInfo.setInstanceId(instanceId);
        newInstanceInfo.setJobParams(jobParams);
        newInstanceInfo.setInstanceParams(instanceParams);
        newInstanceInfo.setWfInstanceId(wfInstanceId);

        newInstanceInfo.setStatus(InstanceStatus.WAITING_DISPATCH.getV());
        newInstanceInfo.setRunningTimes(0L);
        newInstanceInfo.setExpectedTriggerTime(expectTriggerTime);
        newInstanceInfo.setLastReportTime(-1L);
        newInstanceInfo.setGmtCreate(now);
        newInstanceInfo.setGmtModified(now);

        instanceInfoMapper.insert(newInstanceInfo);
        return newInstanceInfo;
    }
}
