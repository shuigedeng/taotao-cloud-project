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

package com.taotao.cloud.job.server.remote.worker;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.taotao.cloud.job.common.domain.WorkerHeartbeat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理 worker 集群信息
 *
 * @author shuigedeng
 * @since 2020/4/5
 */
@Slf4j
public class WorkerClusterManagerService {

    /**
     * 存储Worker健康信息，appId -> ClusterStatusHolder
     */
    private static final Map<String, ClusterStatusHolder> APP_NAME_2_CLUSTER_STATUS =
            Maps.newConcurrentMap();

    /**
     * 更新状态
     *
     * @param heartbeat Worker的心跳包
     */
    public static void updateStatus(WorkerHeartbeat heartbeat) {
        String appName = heartbeat.getAppName();
        ClusterStatusHolder clusterStatusHolder =
                APP_NAME_2_CLUSTER_STATUS.computeIfAbsent(
                        appName, ignore -> new ClusterStatusHolder(appName));
        clusterStatusHolder.updateStatus(heartbeat);
    }

    /**
     * 清理不需要的worker信息
     *
     * @param usingAppIds 需要维护的appId，其余的数据将被删除
     */
    public static void clean(List<Long> usingAppIds) {
        Set<Long> keys = Sets.newHashSet(usingAppIds);
        APP_NAME_2_CLUSTER_STATUS.entrySet().removeIf(entry -> !keys.contains(entry.getKey()));
    }

    /**
     * 清理缓存信息，防止 OOM
     */
    public static void cleanUp() {
        APP_NAME_2_CLUSTER_STATUS.values().forEach(ClusterStatusHolder::release);
    }

    protected static Map<String, ClusterStatusHolder> getAppName2ClusterStatus() {
        return APP_NAME_2_CLUSTER_STATUS;
    }
}
