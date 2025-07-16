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

package com.taotao.cloud.job.common.domain;

import com.taotao.cloud.job.common.module.SystemMetrics;
import lombok.Data;

/**
 * Worker 上报健康信息（worker定时发送的heartbeat）
 *
 * @author shuigedeng
 * @since 2020/3/25
 */
@Data
public class WorkerHeartbeat {

    /**
     * 本机地址 -> IP:port
     */
    private String workerAddress;

    /**
     * server地址 -> IP
     */
    private String serverIpAddress;

    /**
     * 当前 appName
     */
    private String appName;

    /**
     * 当前 appId
     */
    private Long appId;

    /**
     * 当前时间
     */
    private long heartbeatTime;

    /**
     * 客户端名称
     */
    private String client;

    /**
     * 是否已经超载，超载的情况下 Server 一段时间内不会再向其派发任务
     */
    private boolean isOverload;

    private int lightTaskTrackerNum;

    private SystemMetrics systemMetrics;
}
