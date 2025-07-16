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

package com.taotao.cloud.job.common.enums;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Status of the job instance
 *
 * @author shuigedeng
 * @since 2020/3/17
 */
@Getter
@AllArgsConstructor
public enum InstanceStatus {
    /**
     *
     */
    WAITING_DISPATCH(1, "等待派发"),
    WAITING_WORKER_RECEIVE(2, "等待Worker接收"),
    RUNNING(3, "运行中"),
    FAILED(4, "失败"),
    SUCCEED(5, "成功"),
    CANCELED(9, "取消"),
    STOPPED(10, "手动停止");

    private final int v;
    private final String des;

    /**
     * 广义的运行状态
     */
    public static final List<Integer> GENERALIZED_RUNNING_STATUS =
            Lists.newArrayList(WAITING_DISPATCH.v, WAITING_WORKER_RECEIVE.v, RUNNING.v);

    /**
     * 结束状态
     */
    public static final List<Integer> FINISHED_STATUS =
            Lists.newArrayList(FAILED.v, SUCCEED.v, CANCELED.v, STOPPED.v);

    public static InstanceStatus of(int v) {
        for (InstanceStatus is : values()) {
            if (v == is.v) {
                return is;
            }
        }
        throw new IllegalArgumentException("InstanceStatus has no item for value " + v);
    }
}
