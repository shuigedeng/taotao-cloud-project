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

package com.taotao.cloud.job.nameserver.module.sync;

import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * FullSyncInfo
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Getter
public class FullSyncInfo extends SyncInfo {

    public FullSyncInfo(
            Set<String> serverAddressSet,
            Set<String> workerIpAddressSet,
            Map<String, Integer> appName2WorkerNumMap,
            Map<String, Long> serverAddress2ScheduleTimesMap ) {
        super(null);
        this.serverAddressSet = serverAddressSet;
        this.workerIpAddressSet = workerIpAddressSet;
        this.appName2WorkerNumMap = appName2WorkerNumMap;
        this.serverAddress2ScheduleTimesMap = serverAddress2ScheduleTimesMap;
    }

    private Set<String> serverAddressSet;
    private Set<String> workerIpAddressSet;

    /**
     * for split group
     */
    private Map<String, Integer> appName2WorkerNumMap;

    /**
     * for dynamic change group
     */
    private Map<String, Long> serverAddress2ScheduleTimesMap;
}
