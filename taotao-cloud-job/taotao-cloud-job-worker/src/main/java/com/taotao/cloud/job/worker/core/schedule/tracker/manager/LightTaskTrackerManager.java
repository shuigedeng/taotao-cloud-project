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

package com.taotao.cloud.job.worker.core.schedule.tracker.manager;

import com.google.common.collect.Maps;
import com.taotao.cloud.job.worker.core.schedule.tracker.task.light.LightTaskTracker;
import java.util.Map;
import java.util.function.Function;

/**
 * @author shuigedeng
 * @since 2022/9/23
 */
public class LightTaskTrackerManager {

    public static final double OVERLOAD_FACTOR = 1.3d;

    private static final Map<Long, LightTaskTracker> INSTANCE_ID_2_TASK_TRACKER =
            Maps.newConcurrentMap();

    public static LightTaskTracker getTaskTracker(Long instanceId) {
        return INSTANCE_ID_2_TASK_TRACKER.get(instanceId);
    }

    public static void removeTaskTracker(Long instanceId) {
        // 忽略印度的 IDE 警告，这个判断非常有用！！！不加这个判断会导致：如果创建 TT（先执行 computeIfAbsent 正在将TT添加到 HashMap） 时报错，TT
        // 主动调用 destroy 销毁（从 HashMap移除该 TT）时死锁
        if (INSTANCE_ID_2_TASK_TRACKER.containsKey(instanceId)) {
            INSTANCE_ID_2_TASK_TRACKER.remove(instanceId);
        }
    }

    public static void atomicCreateTaskTracker(
            Long instanceId, Function<Long, LightTaskTracker> creator) {
        INSTANCE_ID_2_TASK_TRACKER.computeIfAbsent(instanceId, creator);
    }

    public static int currentTaskTrackerSize() {
        return INSTANCE_ID_2_TASK_TRACKER.size();
    }
}
